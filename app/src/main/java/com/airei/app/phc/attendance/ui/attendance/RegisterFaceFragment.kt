package com.airei.app.phc.attendance.ui.attendance

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.airei.app.phc.attendance.R
import com.airei.app.phc.attendance.common.AppPreferences
import com.airei.app.phc.attendance.common.FaceAngleRange
import com.airei.app.phc.attendance.common.TensorflowKit
import com.airei.app.phc.attendance.common.categorizeFaceAngle
import com.airei.app.phc.attendance.databinding.FragmentRegisterFaceBinding
import com.airei.app.phc.attendance.databinding.LayoutEmpSaveBinding
import com.airei.app.phc.attendance.entity.EmployeeBioTable
import com.airei.app.phc.attendance.entity.EmployeeTable
import com.airei.app.phc.attendance.viewmodel.RoomViewModel
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class RegisterFaceFragment : Fragment() {

    private var _binding: FragmentRegisterFaceBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RoomViewModel by activityViewModels()

    private var empBioData: EmployeeBioTable? = null
    private var empId: String? = null
    private var empData: EmployeeTable? = null

    private var empBioList: List<EmployeeBioTable>? = listOf()

    private var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var cameraSelector: CameraSelector
    private var camFace = MutableLiveData(CameraSelector.LENS_FACING_FRONT)
    private lateinit var cameraExecutor: ExecutorService

    private var flashOn = false
    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId: String

    private var flipX = true

    private var tracking = true

    private var startSaving = false

    private lateinit var alertDialog: AlertDialog

    private lateinit var tensorflowKit: TensorflowKit

    private var faceImgWithAngle = MutableLiveData<Pair<Bitmap?, Float>>()

    private var faceDataListWithAngle: ArrayList<Pair<FloatArray, FaceAngleRange>> = arrayListOf()

    private var angelFaceImg: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegisterFaceBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        empId = arguments?.getString("select_emp_id")
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    goBackPage()
                }
            })
        setToolBar()
        initializeComponents()
        initializeCamera()
        setupListeners()
        observeData()
    }

    private fun setupListeners() {
        binding.register.setOnClickListener { toggleRegistration() }
        binding.switchCam.setOnClickListener { toggleCamera() }
    }

    private fun toggleRegistration() {
        startSaving = !startSaving
        binding.register.text =
            if (startSaving) getString(R.string.str_stop) else getString(R.string.register)
        binding.segmentedProgressBar.visibility = if (startSaving) View.VISIBLE else View.GONE
        angelFaceImg = null
        faceDataListWithAngle.clear()
    }

    private fun toggleCamera() {
        if (camFace.value == CameraSelector.LENS_FACING_BACK) {
            camFace.postValue(CameraSelector.LENS_FACING_FRONT)
            flipX = true
        } else {
            camFace.postValue(CameraSelector.LENS_FACING_BACK)
            flipX = false
        }
    }

    private fun initializeCamera() {
        camFace.postValue(CameraSelector.LENS_FACING_FRONT)
    }

    private fun initializeComponents() {
        cameraManager = requireContext().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            tensorflowKit = TensorflowKit(requireActivity(), modelFile)
        } catch (e: IOException) {
            Log.e(TAG, "initializeComponents: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun observeData() {
        with(viewModel) {
            camFace.observe(viewLifecycleOwner) { updateCamera() }
            faceImgWithAngle.observe(viewLifecycleOwner) { handleFaceData(it) }



            getAllEmployees().observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    empData = it.find { emp -> emp.userId == empId }
                    binding.tvEmpName.text = empData?.name
                    binding.tvEmpCode.text = empData?.empCode
                }
            }

            getEmployeeBiosByApiType().observe(viewLifecycleOwner) {
                if (it.isNotEmpty() && empId != null) {
                    empBioData = it.find { emp -> emp.empUserId == (empId ?: "") }
                    if(empBioData == null){
                        empBioData = EmployeeBioTable(empId ?: "")
                    }
                }
            }
        }
    }

    private fun updateCamera() {

        if (this::cameraProvider.isInitialized) {
            cameraProvider.unbindAll()
        }
        cameraBind()
    }

    private fun handleFaceData(data: Pair<Bitmap?, Float>) {
        binding.face.setImageBitmap(data.first)

        if (startSaving && data.first != null) {
            val angle = data.second
            val angleRange = categorizeFaceAngle(angle)
            Log.d(TAG, "handleFaceData: live $angleRange | $angle")
            if (angleRange != null && !faceDataListWithAngle.any { it.second == angleRange }) {
                Log.d(TAG, "handleFaceData: tensorflowKit ${!this::tensorflowKit.isInitialized}")
                if (!this::tensorflowKit.isInitialized) {
                    tensorflowKit = TensorflowKit(requireActivity(), modelFile)
                }
                val faceEmbeddings = tensorflowKit.getFaceEmbeddings(data.first!!)
                faceDataListWithAngle.add(Pair(faceEmbeddings, angleRange))
                if (angleRange == FaceAngleRange.MINUS_10_TO_0 || angleRange == FaceAngleRange.ZERO_TO_10) {
                    angelFaceImg = data.first
                }
                if (angelFaceImg == null) {
                    angelFaceImg = data.first
                }
                Log.d(TAG, "handleFaceData: save list ${faceDataListWithAngle.size}")
                binding.segmentedProgressBar.setCurrentStep(faceDataListWithAngle.size)
                Log.d(TAG, "handleFaceData: save list ${faceDataListWithAngle.size} | angelFaceImg ${angelFaceImg != null}")
                if (faceDataListWithAngle.size == 10 && angelFaceImg != null) {
                    /* workerData.apply {
                         workerFaceData = faceDataListWithAngle.map { it.first }
                         workerImage = angelFaceImg
                     }*/

                    Log.d(TAG, "handleFaceData: empBioData $empBioData")
                    if(empBioData == null){
                        empBioData = EmployeeBioTable(empId ?: "")
                    }
                    val newEmpBioData = empBioData?.copy()?.apply {
                        empFaceData = faceDataListWithAngle.map { it.first }
                    }
                    Log.d(TAG, "handleFaceData: newEmpBioData $newEmpBioData")
                    startSaving = false
                    binding.register.text = getString(R.string.register)
                    angelFaceImg = null
                    faceDataListWithAngle.clear()
                    binding.segmentedProgressBar.visibility = View.GONE
                    if (newEmpBioData != null) {
                        showDialog(newEmpBioData)
                    } else {
                        angelFaceImg = null
                        tracking = true
                    }
                }
            }
        } else {
            angelFaceImg = null
            faceDataListWithAngle.clear()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDialog(newEmpBioData: EmployeeBioTable) {
        // check face data to other emp bio data table
        Log.d(TAG, "showDialog: newEmpBioData $newEmpBioData")

        var isMatch = false

        if (empBioList != null && empBioList!!.isNotEmpty()) {
            newEmpBioData.empFaceData.forEach { fd ->
                fd.forEach {
                    empBioList!!.forEach { empBio ->
                        if (empBio.empFaceData.isNotEmpty()) {
                            val d = tensorflowKit.recognizeFace(fd, empBioList!!)
                            Log.i(TAG, "check face data: $d ")
                            if (d.isNotEmpty() && !d.any { it.second.empUserId == newEmpBioData.empUserId }) {
                                isMatch = true
                            }
                        }
                    }
                }
            }

        }
        if (isMatch) {
            Toast.makeText(
                requireContext(),
                getString(R.string.face_already_exist),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            try {
                val builder = AlertDialog.Builder(requireContext())
                val bindingAlert = LayoutEmpSaveBinding.inflate(layoutInflater)
                builder.setView(bindingAlert.root)
                builder.setCancelable(true)
                alertDialog = builder.create()
                bindingAlert.empName.setText(empData?.name)

                alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                bindingAlert.saveEmp.setOnClickListener {
                    Log.d(TAG, "showDialog: saveEmp ${newEmpBioData}")
                    newEmpBioData.let {
                        viewModel.insertEmployeeBio(it.copy(apiType = AppPreferences.apiType))
                    }
                    alertDialog.dismiss()
                    findNavController().navigate(R.id.attendanceHomeFragment)
                }
                alertDialog.show()
                alertDialog.setOnDismissListener {
                    angelFaceImg = null
                    tracking = true
                }
            } catch (e: Exception) {
                tracking = true
                Log.e(TAG, "showDialog: ${e.message}")
            }
        }
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        try {
            val preview = Preview.Builder().build().apply {
                surfaceProvider = binding.previewView.surfaceProvider
            }
            cameraSelector = CameraSelector.Builder().requireLensFacing(camFace.value!!).build()
            cameraExecutor = Executors.newSingleThreadExecutor()
            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .apply { setAnalyzer(cameraExecutor, FaceAnalyzer()) }

            cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalyzer, preview)
        } catch (e: Exception) {
            Log.e(TAG, "bindPreview: ${e.message}")
        }
    }

    private fun cameraBind() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture!!.addListener({
            try {
                cameraProvider = cameraProviderFuture!!.get()
                bindPreview(cameraProvider)
            } catch (e: Exception) {
                Log.e(TAG, "cameraBind: ${e.message}")
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun setToolBar() {
        with(binding.topToolBar) {
            tvTitle.text = getString(R.string.register_face)
            imgBack.setOnClickListener {
                goBackPage()
            }
        }
    }

    private fun goBackPage() {
        try {
            findNavController().popBackStack(R.id.viewEmployeeFragment, false)
        } catch (e: Exception) {
            findNavController().navigate(R.id.viewEmployeeFragment)
        }
    }

    private fun trackNearestFace(faces: List<Face>): Face {
        if (faces.isEmpty()) throw IllegalArgumentException("Faces list cannot be empty")
        return faces.maxByOrNull { it.boundingBox.width() * it.boundingBox.height() } ?: faces[0]
    }

    private inner class FaceAnalyzer : ImageAnalysis.Analyzer {
        private val detector = FaceDetection.getClient()

        @RequiresApi(Build.VERSION_CODES.S)
        @OptIn(ExperimentalGetImage::class)
        override fun analyze(imageProxy: ImageProxy) {
            try {
                val mediaImage = imageProxy.image ?: return
                val image =
                    InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                detector.process(image)
                    .addOnSuccessListener { faces ->
                        if (faces.isNotEmpty()) {
                            val face = trackNearestFace(faces)
                            val faceBitmap = tensorflowKit.processImage(
                                mediaImage,
                                imageProxy.imageInfo.rotationDegrees,
                                RectF(face.boundingBox),
                                flipX
                            )
                            faceImgWithAngle.postValue(Pair(faceBitmap, face.headEulerAngleY))
                        } else {
                            faceImgWithAngle.postValue(Pair(null, 0f))
                        }
                    }
                    .addOnFailureListener { }
                    .addOnCompleteListener { imageProxy.close() }
            } catch (e: Exception) {
                Log.e(TAG, "analyze: ${e.message}")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try {
            cameraProvider.unbindAll()
            cameraExecutor.shutdown()
            tensorflowKit.closeTensorflow()
            _binding = null
        } catch (e: Exception) {
            Log.e(TAG, "onDestroyView: ${e.message}")
        }
    }

    companion object {
        const val TAG: String = "RegisterFaceFragment"
        private const val modelFile = "mobile_face_net.tflite"
    }
}