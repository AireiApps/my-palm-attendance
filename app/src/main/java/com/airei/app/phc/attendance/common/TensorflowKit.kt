package com.airei.app.phc.attendance.common

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.YuvImage
import android.media.Image
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import androidx.core.graphics.scale
import com.airei.app.phc.attendance.entity.EmployeeBioTable
import kotlin.math.sqrt

class TensorflowKit(activity: Activity, modelFile: String) {

    companion object {
        private const val TAG = "TensorflowKit"
        private const val IMAGE_MEAN = 128.0f
        private const val IMAGE_STD = 128.0f
        private const val OUTPUT_SIZE = 192
        private const val INPUT_SIZE = 300
    }

    private val interpreter: Interpreter = Interpreter(loadModelFile(activity, modelFile))

    fun closeTensorflow() {
        interpreter.close()
    }

    fun processImage(
        mediaImage: Image, rotationDegrees: Int, boundingBox: RectF, flipX: Boolean,
    ): Bitmap {
        // Convert Image to Bitmap
        val frameBmp = mediaImage.toBitmap(rotationDegrees)
        // Crop out bounding box from whole Bitmap(image)
        var croppedFace: Bitmap = getCropBitmapByCPU(frameBmp, boundingBox)
        // Flip the cropped face if needed
        if (flipX) croppedFace = rotateBitmap(croppedFace, 0f, flipX = true, flipY = false)
        // Scale the acquired face to 200*200 which is required input for model
        return getResizedBitmap(croppedFace, 200, 200)
    }
    fun Image.toBitmap(rotationDegrees: Int, flipX: Boolean = false): Bitmap {
        val yBuffer = planes[0].buffer // Y
        val uBuffer = planes[1].buffer // U
        val vBuffer = planes[2].buffer // V

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, width, height), 100, out)
        val yuv = out.toByteArray()
        val bitmap = BitmapFactory.decodeByteArray(yuv, 0, yuv.size)

        val matrix = Matrix().apply {
            postRotate(rotationDegrees.toFloat())
            if (flipX) {
                postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
            }
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun rotateBitmap(
        bitmap: Bitmap, rotationDegrees: Float, flipX: Boolean, flipY: Boolean,
    ): Bitmap {
        val matrix = Matrix().apply {
            postRotate(rotationDegrees.toFloat())
            postScale(if (flipX) -1.0f else 1.0f, if (flipY) -1.0f else 1.0f)
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true).apply {
            if (this != bitmap) bitmap.recycle()
        }
    }

    fun getCropBitmapByCPU(source: Bitmap, cropRectF: RectF): Bitmap {
        val resultBitmap = Bitmap.createBitmap(
            cropRectF.width().toInt(), cropRectF.height().toInt(), Bitmap.Config.ARGB_8888
        )
        Canvas(resultBitmap).apply {
            drawColor(Color.WHITE)
            drawBitmap(
                source,
                Matrix().apply { postTranslate(-cropRectF.left, -cropRectF.top) },
                Paint(Paint.FILTER_BITMAP_FLAG)
            )
        }
        if (!source.isRecycled) source.recycle()
        return resultBitmap
    }

    private fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        return Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, Matrix().apply {
            postScale(newWidth.toFloat() / bm.width, newHeight.toFloat() / bm.height)
        }, false).apply {
            bm.recycle()
        }
    }

    fun getFaceEmbeddings(image: Bitmap): FloatArray {
        try {
            val imgData = ByteBuffer.allocateDirect(1 * INPUT_SIZE * INPUT_SIZE * 3 * 4)
            imgData.order(ByteOrder.nativeOrder())

            val resizedImage = image.scale(INPUT_SIZE, INPUT_SIZE)
            val intValues = IntArray(INPUT_SIZE * INPUT_SIZE)
            resizedImage.getPixels(
                intValues, 0, resizedImage.width, 0, 0, resizedImage.width, resizedImage.height
            )

            imgData.rewind()
            for (i in 0 until INPUT_SIZE) {
                for (j in 0 until INPUT_SIZE) {
                    val pixelValue = intValues[i * INPUT_SIZE + j]
                    imgData.putFloat(((pixelValue shr 16 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                    imgData.putFloat(((pixelValue shr 8 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                    imgData.putFloat(((pixelValue and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                }
            }
            val inputArray = arrayOf<Any>(imgData)
            val outputMap: MutableMap<Int, Any> = HashMap()
            val embeddings = Array(1) { FloatArray(OUTPUT_SIZE) }
            outputMap[0] = embeddings
            interpreter.runForMultipleInputsOutputs(inputArray, outputMap)
            return embeddings[0]
        } catch (e: Exception) {
            Log.w(TAG, "getFaceEmbeddings: ${e.message}")
            e.printStackTrace()
            return floatArrayOf() // Return empty array on exception
        }
    }

    fun recognizeFace(
        faceEmp: FloatArray,
        registeredList: List<EmployeeBioTable>,
        threshold: Float = 0.7f,
        matchRate: Int = 1,
    ): List<Pair<Int, EmployeeBioTable>> {
        return registeredList.mapNotNull { faceData ->
            val matchRateLocal = faceData.empFaceData.count { knownEmb ->
                val distance = sqrt(
                    knownEmb.zip(faceEmp).sumOf { (a, b) -> (a - b).toDouble().let { it * it } })
                    .toFloat()
                distance < threshold
            }

            if (matchRateLocal >= matchRate) matchRateLocal to faceData else null
        }.sortedByDescending { it.first }
    }

}

enum class FaceAngleRange {
    MINUS_60_TO_MINUS_40, MINUS_40_TO_MINUS_30, MINUS_30_TO_MINUS_20, MINUS_20_TO_MINUS_10, MINUS_10_TO_0, ZERO_TO_10, TEN_TO_20, TWENTY_TO_30, THIRTY_TO_40, FORTY_TO_60
}

fun categorizeFaceAngle(angle: Float): FaceAngleRange? {
    return when (angle) {
        in -60.0..-40.0 -> FaceAngleRange.MINUS_60_TO_MINUS_40
        in -40.0..-30.0 -> FaceAngleRange.MINUS_40_TO_MINUS_30
        in -30.0..-20.0 -> FaceAngleRange.MINUS_30_TO_MINUS_20
        in -20.0..-10.0 -> FaceAngleRange.MINUS_20_TO_MINUS_10
        in -10.0..0.0 -> FaceAngleRange.MINUS_10_TO_0
        in 0.0..10.0 -> FaceAngleRange.ZERO_TO_10
        in 10.0..20.0 -> FaceAngleRange.TEN_TO_20
        in 20.0..30.0 -> FaceAngleRange.TWENTY_TO_30
        in 30.0..40.0 -> FaceAngleRange.THIRTY_TO_40
        in 40.0..60.0 -> FaceAngleRange.FORTY_TO_60
        else -> null
    }
}

@Throws(IOException::class)
fun loadModelFile(activity: Activity, modelFile: String): MappedByteBuffer {
    val fileDescriptor = activity.assets.openFd(modelFile)
    val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
    val fileChannel = inputStream.channel
    val startOffset = fileDescriptor.startOffset
    val declaredLength = fileDescriptor.declaredLength
    return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
}
