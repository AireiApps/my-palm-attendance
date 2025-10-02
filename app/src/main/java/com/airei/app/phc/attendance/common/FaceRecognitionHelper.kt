package com.airei.app.phc.attendance.common


import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.YuvImage
import android.media.Image
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.airei.app.phc.attendance.entity.EmployeeBioTable
import com.airei.app.phc.attendance.entity.MatchData
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import org.tensorflow.lite.Interpreter
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.UUID
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.sqrt

class FaceRecognitionHelper(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val previewView: androidx.camera.view.PreviewView,
    private var lensFacing: Int = CameraSelector.LENS_FACING_FRONT,
    private val inputSize: Int = 112,
    private val outputSize: Int = 192, // MobileFaceNet default
    private val modelFile: String = "mobile_face_net.tflite",
)
{

    private var cameraProvider: ProcessCameraProvider? = null
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()
    private lateinit var tfLite: Interpreter

    private var detectionEnabled = true

    // Callbacks
    var onFaceDetected: ((Bitmap, FloatArray) -> Unit)? = null
    var onMultipleFaces: (() -> Unit)? = null
    var onNoFace: (() -> Unit)? = null

    init {
        tfLite = Interpreter(loadModelFile(context as Activity))
    }

    /** Start CameraX */
    fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.surfaceProvider = previewView.surfaceProvider
            }

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(executor) { imageProxy ->
                if (detectionEnabled) {
                    processImageProxy(imageProxy)
                } else {
                    imageProxy.close()
                }
            }

            try {
                cameraProvider?.unbindAll()
                cameraProvider?.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview, imageAnalysis
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(context))
    }

    /** Switch camera (front/back) */
    fun switchCamera() {
        lensFacing = if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
            CameraSelector.LENS_FACING_BACK
        } else {
            CameraSelector.LENS_FACING_FRONT
        }
        startCamera() // restart camera with new lensFacing
    }

    /** Enable/Disable face detection */
    fun setDetectionEnabled(enabled: Boolean) {
        detectionEnabled = enabled
    }

    /** Get face detection status */
    fun getDetectionEnabled(): Boolean {
        return detectionEnabled
    }

    /** Process frame with MLKit Face Detection */
    @OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val rotation = imageProxy.imageInfo.rotationDegrees
            val image = InputImage.fromMediaImage(mediaImage, rotation)

            val detector = FaceDetection.getClient(
                FaceDetectorOptions.Builder()
                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                    .build()
            )

            detector.process(image).addOnSuccessListener { faces ->
                if (detectionEnabled) {
                    val forwardFaces = faces.filter { face ->
                        val rotY = face.headEulerAngleY   // Yaw
                        val rotX = face.headEulerAngleX   // Pitch
                        rotY in -15.0..15.0 && rotX in -30.0..30.0
                    }

                    when {
                        forwardFaces.isEmpty() -> onNoFace?.invoke()
                        forwardFaces.size > 1 -> onMultipleFaces?.invoke()
                        else -> {
                            val face = forwardFaces.first()
                            val faceBitmap = cropFace(mediaImage, face.boundingBox, rotation)
                            val resized = getResizedBitmap(faceBitmap, inputSize, inputSize)
                            val embedding = getFaceEmbeddings(resized)
                            onFaceDetected?.invoke(resized, embedding)
                        }
                    }
                }
            }.addOnCompleteListener {
                imageProxy.close()
            }
        } else {
            imageProxy.close()
        }
    }

    /** Crop + preprocess face */
    private fun cropFace(mediaImage: Image, boundingBox: Rect, rotation: Int): Bitmap {
        val bitmap = mediaImage.toBitmap() ?: throw Exception("Bitmap null")
        val rotated = rotateBitmap(bitmap, rotation)

        var faceBitmap = Bitmap.createBitmap(
            rotated,
            boundingBox.left.coerceAtLeast(0),
            boundingBox.top.coerceAtLeast(0),
            boundingBox.width().coerceAtMost(rotated.width - boundingBox.left),
            boundingBox.height().coerceAtMost(rotated.height - boundingBox.top)
        )

        if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
            faceBitmap = flipBitmapHorizontally(faceBitmap)
        }
        return faceBitmap
    }

    /** Generate Face Embeddings */
    fun getFaceEmbeddings(image: Bitmap): FloatArray {
        val imgData = ByteBuffer.allocateDirect(1 * inputSize * inputSize * 3 * 4)
        imgData.order(ByteOrder.nativeOrder())
        val intValues = IntArray(inputSize * inputSize)

        image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
        imgData.rewind()

        for (i in 0 until inputSize) {
            for (j in 0 until inputSize) {
                val pixelValue = intValues[i * inputSize + j]
                imgData.putFloat(((pixelValue shr 16 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                imgData.putFloat(((pixelValue shr 8 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                imgData.putFloat(((pixelValue and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
            }
        }

        val inputArray = arrayOf<Any>(imgData)
        val outputMap: MutableMap<Int, Any> = HashMap()
        val localEmb = Array(1) { FloatArray(outputSize) }
        outputMap[0] = localEmb

        tfLite.runForMultipleInputsOutputs(inputArray, outputMap)
        return localEmb[0]
    }

    /** Utils */
    private fun loadModelFile(activity: Activity): MappedByteBuffer {
        val fileDescriptor = activity.assets.openFd(modelFile)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun flipBitmapHorizontally(src: Bitmap): Bitmap {
        val matrix = Matrix().apply { preScale(-1f, 1f) }
        return Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
    }

    private fun Image.toBitmap(): Bitmap? {
        val nv21 = yuv420ToNv21(this)
        val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, width, height), 100, out)
        val imageBytes = out.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    private fun yuv420ToNv21(image: Image): ByteArray {
        val yBuffer = image.planes[0].buffer
        val uBuffer = image.planes[1].buffer
        val vBuffer = image.planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)
        return nv21
    }

    private fun rotateBitmap(bitmap: Bitmap, rotationDegrees: Int): Bitmap {
        if (rotationDegrees == 0) return bitmap
        val matrix = Matrix()
        matrix.postRotate(rotationDegrees.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val scaleWidth = newWidth.toFloat() / bm.width
        val scaleHeight = newHeight.toFloat() / bm.height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, matrix, false)
    }

    /**
     * Find nearest employee(s) based on embedding.
     * Returns a list of nearest MatchData (currently only best match).
     */
    fun findNearest(
        emb: FloatArray,
        empBioList: List<EmployeeBioTable>,
    ): List<MatchData> {
        return try {
            val neighbourList = arrayListOf<MatchData>()
            var bestMatch: MatchData? = null
            var secondBest: MatchData? = null

            empBioList.forEach { data ->
                val faceData = data.empFaceData.firstOrNull() ?: return@forEach
                var distance = 0f
                for (i in emb.indices) {
                    val diff = emb[i] - faceData[i]
                    distance += diff * diff
                }
                distance = sqrt(distance.toDouble()).toFloat()

                val temp = MatchData(
                    id = UUID.randomUUID().toString(),
                    empUserId = data.empUserId,
                    name = "",  // replace with employee name if available
                    data = "",
                    distance = distance
                )

                if (bestMatch == null || distance < bestMatch!!.distance) {
                    secondBest = bestMatch
                    bestMatch = temp
                } else if (secondBest == null || distance < secondBest!!.distance) {
                    secondBest = temp
                }
            }

            bestMatch?.let { neighbourList.add(it) }
            // Uncomment to add second best
            // secondBest?.let { neighbourList.add(it) }
            neighbourList
        } catch (e: Exception) {
            Log.e(TAG, "findNearest: ${e.message}")
            arrayListOf()
        }
    }

    companion object {
        private const val TAG = "FaceRecognitionHelper"
        private const val IMAGE_MEAN = 128f
        private const val IMAGE_STD = 128f
    }
}
