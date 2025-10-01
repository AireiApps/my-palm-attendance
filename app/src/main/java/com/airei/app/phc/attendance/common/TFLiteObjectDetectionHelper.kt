package com.airei.app.phc.attendance.common

import android.app.Activity
import android.graphics.Bitmap
import android.util.Log
import androidx.core.graphics.scale
import org.json.JSONArray
import org.json.JSONObject
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

class TFLiteObjectDetectionHelper(private val activity: Activity) {

    private lateinit var interpreter: Interpreter

    private val TAG = "TFLiteObjectDetectionHelper"

    fun loadModelFile(modelName: String) {
        val fileDescriptor = activity.assets.openFd(modelName)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileChannel.map(
            FileChannel.MapMode.READ_ONLY,
            fileDescriptor.startOffset,
            fileDescriptor.declaredLength
        )
        interpreter = Interpreter(startOffset)
    }

    fun detectObjects(
        bitmap: Bitmap,
        inputSize: Int = 300,
        confidenceThreshold: Float = 0.5f,
        iouThreshold: Float = 0.2f
    ): List<DetectionResult> {
        val resizedBitmap = bitmap.scale(inputSize, inputSize)
        val inputBuffer = preprocessBitmap(resizedBitmap, inputSize)
        val NUM_DETECTIONS = 3000

        val outputLocations = Array(1) { Array(NUM_DETECTIONS) { FloatArray(4) } }
        val outputScoresAndClasses = Array(1) { Array(NUM_DETECTIONS) { FloatArray(2) } }

        val outputs = mapOf(
            0 to outputLocations,
            1 to outputScoresAndClasses
        )

        interpreter.runForMultipleInputsOutputs(arrayOf(inputBuffer), outputs)

        val results = mutableListOf<DetectionResult>()

        for (i in 0 until NUM_DETECTIONS) {
            val score = outputScoresAndClasses[0][i][1]
            val classIndex = outputScoresAndClasses[0][i][0].toInt()
            val boundingBox = outputLocations[0][i]
            Log.i(TAG, "detectObjects: score = $score | classIndex = $classIndex | boundingBox = ${boundingBox.toList().joinToString(", ")}")

            // Only process detections where classIndex == 1
            //if (classIndex == 1) {
                // Apply your confidence threshold check
                if (score >= confidenceThreshold) {
                    // Apply NMS (Non-Maximum Suppression)
                    results.any { existing ->
                        computeIoU(existing.boundingBox, boundingBox.toList()) >= iouThreshold
                    }
                    if (true) {
                        results.add(DetectionResult(boundingBox.toList(), classIndex, score))
                    }
                }
            //}
        }
        Log.i(TAG, "detectObjects: $results")
        return results
    }

    fun getRawModelOutputAsString(
        bitmap: Bitmap,
        inputSize: Int = 300
    ): String {
        val resizedBitmap = bitmap.scale(inputSize, inputSize)
        val inputBuffer = preprocessBitmap(resizedBitmap, inputSize)

        // Prepare output tensors (let TFLite allocate them)
        val outputMap = mutableMapOf<Int, Any>()

        // Get output tensors from interpreter
        val outputLocationsTensor = interpreter.getOutputTensor(0)
        val outputScoresTensor = interpreter.getOutputTensor(1)

        // Allocate buffers for outputs (raw data)
        val outputLocationsBuffer = ByteBuffer.allocateDirect(outputLocationsTensor.numBytes()).apply {
            order(ByteOrder.nativeOrder())
        }
        val outputScoresBuffer = ByteBuffer.allocateDirect(outputScoresTensor.numBytes()).apply {
            order(ByteOrder.nativeOrder())
        }

        // Map of output indices to buffers
        outputMap[0] = outputLocationsBuffer
        outputMap[1] = outputScoresBuffer

        // Run inference
        interpreter.runForMultipleInputsOutputs(arrayOf(inputBuffer), outputMap)

        // Convert outputs to string representation
        val jsonOutput = JSONObject().apply {
            // Convert locations (output 0)
            val locationsArray = JSONArray()
            (outputMap[0] as ByteBuffer).asFloatBuffer().let { buffer ->
                val floatArray = FloatArray(buffer.remaining())
                buffer.get(floatArray)
                for (i in 0 until floatArray.size step 4) {
                    locationsArray.put(JSONArray().apply {
                        put(floatArray[i])
                        put(floatArray[i+1])
                        put(floatArray[i+2])
                        put(floatArray[i+3])
                    })
                }
            }
            put("locations", locationsArray)

            // Convert scores (output 1)
            val scoresArray = JSONArray()
            (outputMap[1] as ByteBuffer).asFloatBuffer().let { buffer ->
                val floatArray = FloatArray(buffer.remaining())
                buffer.get(floatArray)
                for (i in 0 until floatArray.size step 2) {
                    scoresArray.put(JSONArray().apply {
                        put(floatArray[i])   // class index
                        put(floatArray[i+1]) // score
                    })
                }
            }
            put("scores", scoresArray)
        }

        return jsonOutput.toString(2) // Pretty-print with 2-space indent
    }

    private fun computeIoU(box1: List<Float>, box2: List<Float>): Float {
        val top = maxOf(box1[0], box2[0])
        val left = maxOf(box1[1], box2[1])
        val bottom = minOf(box1[2], box2[2])
        val right = minOf(box1[3], box2[3])

        val intersectionArea = maxOf(0f, bottom - top) * maxOf(0f, right - left)
        val box1Area = (box1[2] - box1[0]) * (box1[3] - box1[1])
        val box2Area = (box2[2] - box2[0]) * (box2[3] - box2[1])
        val unionArea = box1Area + box2Area - intersectionArea

        return if (unionArea <= 0f) 0f else intersectionArea / unionArea
    }

    private fun preprocessBitmap(
        bitmap: Bitmap,
        inputSize: Int = 300
    ): ByteBuffer {
        val inputChannels = 3
        val imgData = ByteBuffer.allocateDirect(1 * inputSize * inputSize * inputChannels * 4)
        imgData.order(ByteOrder.nativeOrder())

        val pixels = IntArray(inputSize * inputSize)
        bitmap.getPixels(pixels, 0, inputSize, 0, 0, inputSize, inputSize)

        for (pixel in pixels) {
            val r = (pixel shr 16 and 0xFF) / 255.0f
            val g = (pixel shr 8 and 0xFF) / 255.0f
            val b = (pixel and 0xFF) / 255.0f

            imgData.putFloat(r)
            imgData.putFloat(g)
            imgData.putFloat(b)
        }

        return imgData
    }
}

data class DetectionResult(
    val boundingBox: List<Float>, // [top, left, bottom, right]
    val classIndex: Int,
    val score: Float
)

