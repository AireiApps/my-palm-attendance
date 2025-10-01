package com.airei.app.phc.attendance.ui.attendance

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import android.hardware.camera2.CameraManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.airei.app.phc.attendance.MyPalmAttendanceApp
import com.airei.app.phc.attendance.R
import com.airei.app.phc.attendance.common.AppPreferences
import com.airei.app.phc.attendance.common.LocationProvider
import com.airei.app.phc.attendance.common.TensorflowKit
import com.airei.app.phc.attendance.databinding.FragmentMakeAttendanceBinding
import com.airei.app.phc.attendance.entity.AttendanceStatus
import com.airei.app.phc.attendance.entity.EmpAttendanceTable
import com.airei.app.phc.attendance.entity.EmployeeBioTable
import com.airei.app.phc.attendance.entity.EmployeeTable
import com.airei.app.phc.attendance.entity.PlantationLocation
import com.airei.app.phc.attendance.entity.UserTable
import com.airei.app.phc.attendance.utils.getTodayEndTimeMillis
import com.airei.app.phc.attendance.utils.getTodayStartTimeMillis
import com.airei.app.phc.attendance.viewmodel.RoomViewModel
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import java.io.IOException
import java.util.Calendar
import java.util.TimeZone
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.getValue
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random


class MakeAttendanceFragment : Fragment() {

    private var _binding: FragmentMakeAttendanceBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RoomViewModel by activityViewModels()

    // db data
    private var todayAttendance: List<EmpAttendanceTable> = emptyList()
    private var employeeList = emptyList<EmployeeTable>()
    private var empBioList = emptyList<EmployeeBioTable>()
    private var empAttendance = emptyList<EmpAttendanceTable>()

    private var localEmpAttendance: EmpAttendanceTable? = null

    private var userData: UserTable? = null

    private var lastLocation = MutableLiveData<PlantationJsonLocation?>()

    private var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null
    private var cameraProvider: ProcessCameraProvider? = null

    private var recognition = true
    private lateinit var cameraSelector: CameraSelector
    private var camFace = MutableLiveData(CameraSelector.LENS_FACING_FRONT)
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraManager: CameraManager
    private var flipX = false

    private var faceImgWithAngle = MutableLiveData<Pair<Bitmap?, Float>>()
    private lateinit var tensorflowKit: TensorflowKit

    // location
    private lateinit var locationProvider: LocationProvider

    private var employee = MutableLiveData<EmpLiveCheck?>()

    // area
   /* private var estateTable: List<EstateTable> = listOf()
    private var divisionTable: List<DivisionTable> = listOf()
    private var blockTable: List<BlockTable> = listOf()*/

    data class PlantationJsonLocation(
        val latitude: Double,
        val longitude: Double
    )

    data class EmpLiveCheck(
        var empId: String = "", var start: Long = 0, var end: Long = 0
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMakeAttendanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recognition = false
        setupOnBackPressed()
        setToolBar()
        initializeComponents()
        dataObserve()
        setObservers()
        setButtonActions()
        binding.layoutStatus.visibility = View.GONE
        binding.layoutAreaInfo.visibility = View.GONE

        try {
            Handler(Looper.getMainLooper()).postDelayed({recognition = true},3000)
        }catch (e:Exception){
            e.printStackTrace()
            recognition = true
        }
    }

    private fun dataObserve() {
        with(viewModel) {
            getAllUsers().observe(viewLifecycleOwner) {
                userData = it.find { u -> u.userId == AppPreferences.loginId }
            }

            /*getAllEstates.observe(viewLifecycleOwner) {
                estateTable = it.filter { d -> d.loginId == AppPreferences.loginId }
            }
            getAllDivision.observe(viewLifecycleOwner) {
                divisionTable = it.filter { d -> d.loginId == AppPreferences.loginId }
            }
            getAllBlock.observe(viewLifecycleOwner) {
                blockTable = it.filter { d -> d.loginId == AppPreferences.loginId }
            }*/


            getAllEmployees().observe(viewLifecycleOwner) {
                Log.i(TAG, "dataObserve: emp list = $it")
                employeeList = it.filter { e -> e.apiType == AppPreferences.apiType }
                /*if (viewModel.loginEstate != "") {
                    employeeList = employeeList.filter { e -> e.estateId == viewModel.loginEstate }.toMutableList()
                }
                if (viewModel.loginDivision != "") {
                    employeeList = employeeList.filter { e -> e.divisionId == viewModel.loginDivision }.toMutableList()
                }
                if (viewModel.loginBlock != "") {
                    employeeList = employeeList.filter { e -> e.blockId == viewModel.loginBlock }.toMutableList()
                }*/
            }
            getEmployeeBiosByApiType().observe(viewLifecycleOwner) {
                Log.i(TAG, "dataObserve: emp bio list = $it")
                empBioList = it
            }

            getAllAttendance(
            ).observe(viewLifecycleOwner) {
                Log.i(TAG, "dataObserve: emp attendance list = $it")
                empAttendance = it
            }

            getAllUsers().observe(viewLifecycleOwner) {
                userData = it.find { u -> u.userId == AppPreferences.loginId }!!
                Log.i(TAG, "dataObserve: userData = $userData")
            }
        }
    }



    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    goBackPage()
                }
            }
        )
    }

    private fun setObservers() {
        camFace.observe(viewLifecycleOwner) {
            flipX = it == CameraSelector.LENS_FACING_FRONT
            updateCamera()
        }

        employee.observe(viewLifecycleOwner) {
            if (it != null) {
                Log.i(
                    TAG, "observeLiveData employee: ${it.empId} -> waiting for ${
                        (getWaitingTime(
                            it.start, it.end
                        )).toInt()
                    }seconds"
                )
                if ((getWaitingTime(
                        it.start, it.end
                    )).toInt() >= 5
                ) {
                    recognition = false
                    getLocation()
                }
            }
        }

        faceImgWithAngle.observe(viewLifecycleOwner) {
            if (it.first != null) {
                binding.face.setImageBitmap(it.first)
                checkEmpBio(it.first!!)
            } else {
                binding.face.setImageBitmap(null)
            }
        }

        lastLocation.observe(viewLifecycleOwner) { location ->
            if (location == null) {
                recognition = true
                return@observe
            }
            recognition = false
            val isInRange =  Random.nextBoolean()//locationRangeCheck(getPlantationLocationList(jsonStringIND), location)

            val lastAttendance =
                empAttendance.find { e -> e.empUserId == employee.value?.empId && e.outTime.toLong() == 0L }
            Log.i(TAG, "lastAttendance: $lastAttendance")

            var atten = EmpAttendanceTable(
                empUserId = employee.value?.empId!!,
            )

            val loginStatus = if (isInRange) {
                AttendanceStatus.IN_BOUNDARY
            } else {
                AttendanceStatus.OUT_BOUNDARY
            }

            if (lastAttendance == null) {
                atten = atten.copy(
                    inTime = System.currentTimeMillis().toString(),
                    inStatus = loginStatus.toString(),
                    outTime = "0",
                    outStatus = "",
                    userId = AppPreferences.loginId,
                    apiType = AppPreferences.apiType,
                )
            } else {

                atten = atten.copy(
                    localId = lastAttendance.localId,
                    inTime = lastAttendance.inTime,
                    inStatus = lastAttendance.inStatus,
                    outTime = System.currentTimeMillis().toString(),
                    outStatus = loginStatus.toString(),
                    inDate = lastAttendance.inDate,
                    modDate = System.currentTimeMillis(),
                    userId = lastAttendance.userId,
                    siteId = lastAttendance.siteId,
                    estateId = lastAttendance.estateId,
                    divisionId = lastAttendance.divisionId,
                    blockId = lastAttendance.blockId,
                    apiType = lastAttendance.apiType,
                )
            }
            localEmpAttendance = atten
            Log.d(TAG, "setObservers: localEmpAttendance = $localEmpAttendance")
            insertEmpAttendance(localEmpAttendance?:atten)
            localEmpAttendance = null
            //getAreaInfo(atten)
        }
    }

    private fun getAreaInfo(atten: EmpAttendanceTable) {
        binding.layoutAreaInfo.visibility = View.VISIBLE
        binding.layoutStatus.visibility = View.GONE
        Log.d(TAG, "getAreaInfo: atten = $atten")
        /*if (viewModel.loginEstate != "") {
            Log.i(TAG, "setButtonActions: loginEstate : ${viewModel.loginEstate}")
            binding.etEstate.setText(estateTable.find { e -> e.id == viewModel.loginEstate }?.estateName)
        }
        if (viewModel.loginDivision != "") {
            Log.i(TAG, "setButtonActions: loginDivision : ${divisionTable.find { d -> d.id == viewModel.loginDivision }?.divisionName}")
            binding.etDivision.setText(divisionTable.find { d -> d.id == viewModel.loginDivision }?.divisionName)
        }
        if (viewModel.loginBlock != "") {
            Log.i(TAG, "setButtonActions: loginBlock : ${blockTable.find { b -> b.id == viewModel.loginBlock }?.blockName}")
            binding.etBlock.setText(blockTable.find { b -> b.id == viewModel.loginBlock }?.blockName)
        }*/
        if (atten.outTime == "0") {
            /*if (userData != null && estateTable != null && divisionTable != null && blockTable != null) {
                val estate = estateTable.find { e -> e.id == userData.estateId }?.estateName
                binding.etEstate.setText(estate)
                localEmpAttendance = atten
            } else {*/
                insertEmpAttendance(atten)
            //}
        } else {
            insertEmpAttendance(atten)
        }
    }

    private fun insertEmpAttendance(atten: EmpAttendanceTable) {
        Log.d(TAG, "insertEmpAttendance: atten = $atten")
        viewModel.insertAttendance(atten)
        //binding.tvAttebStatus.text = result.message
        binding.layoutAreaInfo.visibility = View.GONE
        binding.layoutStatus.visibility = View.VISIBLE
        binding.tvAttebName.text = atten.empUserId
        binding.lottieAnimationView.setAnimation(R.raw.saved)
        binding.tvAttebName.text = atten.empUserId
        binding.lottieAnimationView.setAnimation(R.raw.saved)
    }


    private fun locationRangeCheck(
        plantationList: List<PlantationLocation>,
        plantation: PlantationJsonLocation,
        rangeInKm: Double = 10.0
    ): Boolean {
        for (p in plantationList) {
            val d = distance(
                plantation.latitude, plantation.longitude, p.lat.toDouble(), p.lat.toDouble()
            )
            if (d <= rangeInKm) {
                return true
            }
        }
        return false
    }

    private fun distance(
        lat1: Double, lon1: Double, lat2: Double, lon2: Double
    ): Double {
        val R = 6371 // Radius of the Earth in kilometers
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a =
            sin(dLat / 2) * sin(dLat / 2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(
                dLon / 2
            ) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c // Distance in kilometers
    }

    private fun getWaitingTime(start: Long, end: Long): Float {
        if (start == 0L || end == 0L) return 0F
        return (end - start) / 1000F
    }

    private fun checkEmpBio(first: Bitmap) {
        val faceEmbeddings = tensorflowKit.getFaceEmbeddings(first)
        Log.i(TAG, "checkEmpBio: $empBioList")
        val findMatch = tensorflowKit.recognizeFace(faceEmbeddings, empBioList)
        Log.i(TAG, "checkEmpBio: $findMatch")
        if (findMatch.isNotEmpty()) {
            if (findMatch.first().first >= 3) {
                Log.i(
                    TAG,
                    "observeLiveData face: ${findMatch.first().first} -> ${findMatch.first().second.empUserId}"
                )
                var lastEmp = employee.value
                if (lastEmp != null) {
                    if (lastEmp.empId == findMatch.first().second.empUserId) {
                        lastEmp.end = getCurrentUTCTimeInMillis()
                        employee.postValue(lastEmp)
                    } else {
                        employee.postValue(
                            EmpLiveCheck(
                                findMatch.first().second.empUserId,
                                start = getCurrentUTCTimeInMillis()
                            )
                        )
                    }
                } else {
                    employee.postValue(
                        EmpLiveCheck(
                            findMatch.first().second.empUserId,
                            start = getCurrentUTCTimeInMillis()
                        )
                    )
                }
            }
        }
    }

    private fun getCurrentUTCTimeInMillis(): Long {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        return calendar.timeInMillis
    }

    private fun setToolBar() {
        binding.topToolBar.apply {
            tvTitle.text = getString(R.string.attendance)
            imgBack.setOnClickListener { goBackPage() }
            imgOption.visibility = View.GONE
        }
    }

    private fun goBackPage() {
        try {
            findNavController().popBackStack(R.id.attendanceHomeFragment, false)
        } catch (e: Exception) {
            e.printStackTrace()
            findNavController().navigate(R.id.attendanceHomeFragment)
        }

    }

    private fun setButtonActions() {
        with(binding) {
            switchCam.setOnClickListener { toggleCamera() }
            btnOk.setOnClickListener {
                //localEmpAttendance = localEmpAttendance!!.copy()
                //insertEmpAttendance(localEmpAttendance!!)
                binding.layoutStatus.visibility = View.GONE
                binding.layoutAreaInfo.visibility = View.GONE
                binding.tvAttebStatus.text = ""
                binding.tvAttebName.text = ""
                binding.face.setImageBitmap(null)
                binding.lottieAnimationView.setAnimation(R.raw.saved)
                Handler(Looper.getMainLooper()).postDelayed({ recognition = true },1000)
                /*binding.layoutStatus.visibility = View.GONE
                binding.layoutAreaInfo.visibility = View.GONE
                //binding.tvAttebStatus.text = ""
                binding.tvAttebName.text = ""
                binding.face.setImageBitmap(null)
                binding.lottieAnimationView.setAnimation(R.raw.saved)
                */
            }
            /*if (viewModel.loginEstate == "" || viewModel.loginDivision == ""|| viewModel.loginBlock == "") {
                etEstate.doOnTextChanged { _, _, _, _ ->
                    etEstate.error = null
                    etDivision.text?.clear()
                }
                etDivision.doOnTextChanged { _, _, _, _ ->
                    etDivision.error = null
                    etBlock.text?.clear()
                }
                etBlock.doOnTextChanged { _, _, _, _ ->
                    etBlock.error = null
                }
            }*/
            /*etEstate.setOnClickListener {
                if (viewModel.loginEstate.toString() == "") {
                    if (userData?.estateId == null || userData?.estateId == "") {
                        val dataList = estateTable.map { it.estateName }
                        showFieldSelectionDialog(
                            requireContext(), getString(R.string.select_estate), dataList, etEstate
                        )
                    }
                }
            }
            etDivision.setOnClickListener {
                if (viewModel.loginDivision.toString() == "") {
                    if (etEstate.text.toString() == "") {
                        etEstate.error = getString(R.string.required_field)
                    }
                    else {
                        val estateId =
                            estateTable.find { d -> d.estateName == etEstate.text.toString() }?.id
                        val dataList =
                            divisionTable.filter { d -> d.estateId == estateId }
                                .map { it.divisionName }
                        showFieldSelectionDialog(
                            requireContext(),
                            getString(R.string.select_division),
                            dataList,
                            etDivision
                        )
                    }
                }
            }
            etBlock.setOnClickListener {
                if (viewModel.loginBlock == "") {
                    if (etDivision.text.toString() == "") {
                        etDivision.error = getString(R.string.required_field)
                    } else {
                        val divId =
                            divisionTable.find { d -> d.divisionName == etDivision.text.toString() }?.id
                        val dataList =
                            blockTable.filter { d -> d.divisionId == divId }.map { it.blockName }
                        showFieldSelectionDialog(
                            requireContext(), getString(R.string.select_block), dataList, etBlock
                        )
                    }
                }
            }*/
            /*btnConfirm.setOnClickListener {
                if (localEmpAttendance == null) {
                    viewModel.toastMsg.postValue(
                        Pair(
                            ToastMsgType().error,
                            getString(R.string.no_attendance)
                        )
                    )
                } else {
                    if (validateData()) {
                        val estate =
                            estateTable.find { e -> e.estateName == etEstate.text.toString() }
                        val division =
                            divisionTable.find { d -> d.divisionName == etDivision.text.toString() }
                        val block = blockTable.find { b -> b.blockName == etBlock.text.toString() }
                        localEmpAttendance = localEmpAttendance!!.copy(
                            estateId = estate?.id ?: "",
                            divisionId = division?.id ?: "",
                            blockId = block?.id ?: ""
                        )
                        insertEmpAttendance(localEmpAttendance!!)
                    } else {

                    }

                }
            }*/
        }
    }

    /*private fun validateData(): Boolean {
        with(binding) {
            var allFieldsFilled = true
            if (etEstate.text.toString() == "") {
                etEstate.error = getString(R.string.required_field)
                allFieldsFilled = false
            }
            if (etDivision.text.toString() == "") {
                etDivision.error = getString(R.string.required_field)
                allFieldsFilled = false
            }
            if (etBlock.text.toString() == "") {
                etBlock.error = getString(R.string.required_field)
                allFieldsFilled = false
            }
            return allFieldsFilled
        }
    }*/

    private fun toggleCamera() {
        camFace.postValue(
            if (camFace.value == CameraSelector.LENS_FACING_BACK) {
                CameraSelector.LENS_FACING_FRONT
            } else {
                CameraSelector.LENS_FACING_BACK
            }
        )
    }

    private fun getLocation() {
        Log.i(TAG, "getLocation: ")

        try {
            locationProvider.getLastLocation(object : LocationProvider.LocationListener {
                override fun onLocationResult(location: Location) {
                    // Handle last known location here
                    Log.d(
                        TAG,
                        "LastLocation : Lat: ${location.latitude}, Long: ${location.longitude}"
                    )
                    lastLocation.postValue(
                        PlantationJsonLocation(
                            latitude = location.latitude, longitude = location.longitude
                        )
                    )
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "getLocation: $e" )
        }
    }

    private fun initializeComponents() {
        try {
            cameraManager = requireContext().getSystemService(Context.CAMERA_SERVICE) as CameraManager
            cameraExecutor = Executors.newSingleThreadExecutor()
            locationProvider = LocationProvider(MyPalmAttendanceApp.instance)
            cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
            tensorflowKit = TensorflowKit(requireActivity(), modelFile)
        } catch (e: IOException) {
            e.printStackTrace()
        }
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
                detector.process(image).addOnSuccessListener { faces ->
                    if (recognition) {
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
                }.addOnFailureListener { }.addOnCompleteListener { imageProxy.close() }
            } catch (e: Exception) {
                Log.e(TAG, "analyze: ${e.message}")
            }
        }
    }

    private fun trackNearestFace(faces: List<Face>): Face {
        if (faces.isEmpty()) throw IllegalArgumentException("Faces list cannot be empty")
        return faces.maxByOrNull { it.boundingBox.width() * it.boundingBox.height() } ?: faces[0]
    }

    private fun updateCamera() {
        cameraProvider?.unbindAll()
        bindCameraUseCases()
    }

    private fun bindCameraUseCases() {
        cameraProviderFuture?.addListener({
            try {
                cameraProvider = cameraProviderFuture?.get()
                setupPreviewAndAnalyzer(cameraProvider!!)
            } catch (e: Exception) {
                Log.e(TAG, "bindCameraUseCases: ${e.message}")
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun setupPreviewAndAnalyzer(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder().build().apply {
            surfaceProvider = binding.previewView.surfaceProvider
        }
        cameraSelector = CameraSelector.Builder().requireLensFacing(camFace.value!!).build()

        val imageAnalyzer = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .apply { setAnalyzer(cameraExecutor, FaceAnalyzer()) }

        cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalyzer, preview)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        cameraProvider?.unbindAll()
        cameraExecutor.shutdown()
        tensorflowKit.closeTensorflow()
        _binding = null
    }

    companion object {
        private const val TAG = "MakeAttendanceFragment"
        private const val modelFile = "mobile_face_net.tflite"
    }
}