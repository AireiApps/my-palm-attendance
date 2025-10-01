package com.airei.app.phc.attendance.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import android.os.Build
import android.util.Log
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraHelper(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val previewView: PreviewView,
    private val flipX: Boolean = false,
    private val faceDetectionCallback: FaceDetectionCallback,
    private val tensorflowKit: TensorflowKit
) {
    private var cameraProviderFuture: ListenableFuture<ProcessCameraProvider> = ProcessCameraProvider.getInstance(context)
    private var cameraProvider: ProcessCameraProvider? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private var camFacing = CameraSelector.LENS_FACING_FRONT
    private val faceDetector = FaceDetection.getClient()

    init {
        bindCameraUseCases()
    }

    interface FaceDetectionCallback {
        fun onFaceDetected(faceBitmap: Bitmap?, angle: Float)
    }

    fun toggleCamera() {
        camFacing = if (camFacing == CameraSelector.LENS_FACING_BACK) {
            CameraSelector.LENS_FACING_FRONT
        } else {
            CameraSelector.LENS_FACING_BACK
        }
        cameraSelector = CameraSelector.Builder().requireLensFacing(camFacing).build()
        bindCameraUseCases()
    }

    private fun bindCameraUseCases() {
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            setupPreviewAndAnalyzer()
        }, ContextCompat.getMainExecutor(context))
    }

    private fun setupPreviewAndAnalyzer() {
        val preview = Preview.Builder().build().apply {
            surfaceProvider = previewView.surfaceProvider
        }

        val imageAnalyzer = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .apply { setAnalyzer(cameraExecutor, FaceAnalyzer()) }

        cameraProvider?.unbindAll()
        cameraProvider?.bindToLifecycle(lifecycleOwner, cameraSelector, imageAnalyzer, preview)
    }

    private inner class FaceAnalyzer : ImageAnalysis.Analyzer {
        @OptIn(ExperimentalGetImage::class)
        @RequiresApi(Build.VERSION_CODES.S)
        override fun analyze(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image ?: return
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            Log.i(TAG, "analyze: ${image.width}")
            faceDetector.process(image)
                .addOnSuccessListener { faces ->
                    val face = faces.maxByOrNull { it.boundingBox.width() * it.boundingBox.height() }
                    face?.let {
                        val faceBitmap = tensorflowKit.processImage(
                            mediaImage,
                            imageProxy.imageInfo.rotationDegrees,
                            RectF(face.boundingBox),
                            flipX
                        )
                        Log.i(TAG, "analyze: faceBitmap-> ${faceBitmap.width}")
                        faceDetectionCallback.onFaceDetected(faceBitmap, face.headEulerAngleY)
                    } ?: faceDetectionCallback.onFaceDetected(null, 0f)
                }
                .addOnCompleteListener { imageProxy.close() }
        }


    }

    fun releaseResources() {
        cameraProvider?.unbindAll()
        cameraExecutor.shutdown()
        faceDetector.close()
    }


    companion object {
        private const val TAG = "CameraHelper"
    }
}
