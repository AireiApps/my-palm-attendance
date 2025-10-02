package com.airei.app.phc.attendance.ui.attendance

import android.animation.Animator
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.airei.app.phc.attendance.R
import com.airei.app.phc.attendance.common.AppPreferences
import com.airei.app.phc.attendance.common.FaceRecognitionHelper
import com.airei.app.phc.attendance.databinding.FragmentFaceRecognitionBinding
import com.airei.app.phc.attendance.entity.AttendanceStatus
import com.airei.app.phc.attendance.entity.EmpAttendanceTable
import com.airei.app.phc.attendance.entity.EmployeeBioTable
import com.airei.app.phc.attendance.entity.EmployeeTable
import com.airei.app.phc.attendance.utils.getTodayEndTimeMillis
import com.airei.app.phc.attendance.utils.getTodayStartTimeMillis
import com.airei.app.phc.attendance.viewmodel.RoomViewModel
import kotlin.random.Random

class FaceRecognitionFragment : Fragment() {

    private var _binding: FragmentFaceRecognitionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RoomViewModel by activityViewModels()

    private var isDetectionEnabled: Boolean = false

    private var faceHelper: FaceRecognitionHelper? = null

    private var currentFaceImage: Bitmap? = null
    private var currentFaceEmbedding: FloatArray? = null

    private var detectionEmp: EmployeeTable? = null
    private var detectionEmpAttendance: EmpAttendanceTable? = null
    private var empDataList: List<EmployeeTable>? = null
    private var empBioList: List<EmployeeBioTable>? = null
    private var todayAttendanceList: List<EmpAttendanceTable>? = null

    private var localDetection = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFaceRecognitionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    goBackPage()
                }
            })
        setToolBar()
        clickAction()
        observeData()
        setCamerax()
    }

    private fun observeData() {
        with(viewModel) {
            getAllEmployees().observe(viewLifecycleOwner) {
                empDataList = (it ?: listOf()).filter { it.apiType == AppPreferences.apiType }
            }
            getAllEmployeeBios().observe(viewLifecycleOwner) {
                empBioList = (it ?: listOf()).filter { it.apiType == AppPreferences.apiType }
            }
            getAllTodayAttendance(
                getTodayStartTimeMillis(), getTodayEndTimeMillis()
            ).observe(viewLifecycleOwner) {
                todayAttendanceList =
                    (it ?: listOf()).filter { it.apiType == AppPreferences.apiType }
            }
        }
    }

    private fun clickAction() {
        with(binding) {
            // toggle detection
            binding.btnAttendance.setOnClickListener {
                if (faceHelper != null) {
                    toggleDetection(!isDetectionEnabled, faceHelper = faceHelper!!, img = imgFace)
                }
            }
            // switch camera
            binding.imgSwitchCam.setOnClickListener {
                if (faceHelper != null) {
                    faceHelper?.switchCamera()
                }
            }
        }
    }

    private fun setToolBar() {
        binding.topToolBar.apply {
            tvTitle.text = getString(R.string.attendance)
            imgBack.setOnClickListener { goBackPage() }
            imgOption.visibility = View.VISIBLE
        }
    }

    private fun setCamerax() {
        viewScreenCtrl(SCREEN_CAM)
        faceHelper = FaceRecognitionHelper(
            context = requireContext(),
            lifecycleOwner = viewLifecycleOwner,
            previewView = binding.previewView
        )

        isDetectionEnabled = faceHelper!!.getDetectionEnabled()
        toggleDetection(isDetectionEnabled, faceHelper!!)
        faceHelper!!.onFaceDetected = { bitmap, emb ->
            currentFaceImage = bitmap
            currentFaceEmbedding = emb
            Log.d(TAG, "FaceRecognitionHelper: Single face detected: embedding size=${emb.size}")
            if (localDetection) {
                if (_binding != null) {
                    binding.imgFace.setImageBitmap(bitmap)
                    findEmpBioData(bitmap, emb)
                }
            }else{
                Log.d(TAG, "setCamerax: localDetection $localDetection")
            }
        }

        faceHelper!!.onNoFace = {
            Log.d(TAG, "FaceRecognitionHelper: No face detected")
            if (_binding != null) {
                binding.imgFace.setImageBitmap(null)
                if (binding.tvStatus.text.toString() != getString(R.string.no_face_detected)) binding.tvStatus.text =
                    getString(R.string.no_face_detected)
            }
        }

        faceHelper!!.onMultipleFaces = {
            Log.d(TAG, "FaceRecognitionHelper:  Multiple faces detected")
            if (_binding != null) {
                binding.imgFace.setImageBitmap(null)
                if (binding.tvStatus.text.toString() != getString(R.string.multiple_faces_detected)) binding.tvStatus.text =
                    getString(R.string.multiple_faces_detected)
            }
        }

        // start camera
        faceHelper!!.startCamera()
    }

    @SuppressLint("SetTextI18n")
    private fun findEmpBioData(bitmap: Bitmap, emb: FloatArray) {
        localDetection = false
        val localEmb = emb
        if (faceHelper != null) {
            val findEmpBioData = faceHelper?.findNearest(localEmb, empBioList ?: listOf())
            if (findEmpBioData != null && findEmpBioData.isNotEmpty()) {
                val getBestMatch = findEmpBioData.maxByOrNull { it.distance < 0.6 }
                if (getBestMatch != null) {
                    detectionEmp = empDataList?.find { it.userId == getBestMatch.empUserId }
                    if (detectionEmp != null) {
                        binding.tvStatus.text = "${detectionEmp!!.name} [${detectionEmp!!.userId}]"
                        lastLocation(detectionEmp!!)
                    }
                } else {
                    if (binding.tvStatus.text.toString() != getString(R.string.no_match_found)) binding.tvStatus.text =
                        getString(R.string.no_match_found)
                    localDetection = true
                }
            } else {
                if (binding.tvStatus.text.toString() != getString(R.string.no_match_found)) binding.tvStatus.text =
                    getString(R.string.no_match_found)
                localDetection = true
            }
        }else{
            localDetection = true
        }
    }

    private fun lastLocation(findEmp: EmployeeTable) {
        Log.d(TAG, "lastLocation: $findEmp")
        val isInRange =
            Random.nextBoolean()//locationRangeCheck(getPlantationLocationList(jsonStringIND), location)
        val loginStatus = if (isInRange) {
            AttendanceStatus.IN_BOUNDARY
        } else {
            AttendanceStatus.OUT_BOUNDARY
        }
        makeAttendance(loginStatus, findEmp)
        Log.d(TAG, "lastLocation: loginStatus $loginStatus")
    }

    private fun makeAttendance(loginStatus: AttendanceStatus, findEmp: EmployeeTable) {
        detectionEmpAttendance =
            todayAttendanceList?.find { it.empUserId == findEmp.userId && it.outTime == "0" }
        Log.d(TAG, "makeAttendance: today att: $todayAttendanceList")
        if (detectionEmpAttendance != null) {
            detectionEmpAttendance = detectionEmpAttendance!!.copy(
                outTime = System.currentTimeMillis().toString(),
                outStatus = loginStatus.toString(),
                userId = AppPreferences.loginId
            )
        } else {
            detectionEmpAttendance = EmpAttendanceTable(
                empUserId = findEmp.userId,
                inTime = System.currentTimeMillis().toString(),
                inStatus = loginStatus.toString(),
                outTime = "0",
                outStatus = "",
                userId = AppPreferences.loginId
            )
        }
        Log.d(TAG, "makeAttendance: detectionEmpAttendance $detectionEmpAttendance")
        //viewScreenCtrl(SCREEN_AREA)
        //getAreaInfo(detectionEmpAttendance!!, findEmp)
        saveAttendance(detectionEmpAttendance!!)
    }

    private fun saveAttendance(detectionEmpAttendance: EmpAttendanceTable) {
        viewModel.insertAttendance(detectionEmpAttendance)
        viewScreenCtrl(
            SCREEN_STATUS,
            "Attendance Saved Successfully For ${detectionEmpAttendance.empUserId}"
        )
    }

    private fun viewScreenCtrl(screenArea: String, status: String = "") {
        with(binding) {
            when (screenArea) {
                SCREEN_STATUS -> {
                    layoutStatus.visibility = View.VISIBLE
                    binding.layoutAreaInfo.visibility = View.GONE
                    tvAttebStatus.text = status
                    lottieAnimationView.setAnimation(R.raw.saved)
                    lottieAnimationView.playAnimation()
                    lottieAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
                        @SuppressLint("SetTextI18n")
                        override fun onAnimationEnd(animation: Animator) {
                            lottieAnimationView.postDelayed({
                                viewScreenCtrl(SCREEN_CAM)
                                if (faceHelper != null) {
                                    toggleDetection(false, faceHelper!!)
                                }
                                localDetection = true
                            }, 2000)
                        }

                        override fun onAnimationStart(animation: Animator) {}
                        override fun onAnimationCancel(animation: Animator) {}
                        override fun onAnimationRepeat(animation: Animator) {}
                    })
                }

                SCREEN_CAM -> {
                    layoutStatus.visibility = View.GONE
                    layoutAreaInfo.visibility = View.GONE
                    tvAttebStatus.text = status
                }

                SCREEN_AREA -> {
                    layoutStatus.visibility = View.GONE
                    layoutAreaInfo.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun toggleDetection(
        isDetectEnabled: Boolean,
        faceHelper: FaceRecognitionHelper,
        btn: AppCompatButton = binding.btnAttendance,
        img: ImageView = binding.imgFace,
        llStatus: LinearLayout = binding.llStatus,
    ) {
        isDetectionEnabled = isDetectEnabled
        try {
            faceHelper.setDetectionEnabled(isDetectEnabled)
            if (isDetectEnabled) {
                btn.text = getString(R.string.stop_attendance)
                btn.backgroundTintList = ContextCompat.getColorStateList(
                    requireContext(), R.color.flame_pea
                )
                llStatus.visibility = View.VISIBLE
            } else {
                btn.text = getString(R.string.stop_attendance)
                btn.backgroundTintList = ContextCompat.getColorStateList(
                    requireContext(), R.color.green
                )
                img.setImageBitmap(null)
                llStatus.visibility = View.INVISIBLE
            }
            Toast.makeText(
                requireContext(),
                if (isDetectEnabled) "Detection Enabled" else "Detection Disabled",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun goBackPage() {
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG: String = "FaceRecognitionFragment"
        const val SCREEN_STATUS = "status_screen"
        const val SCREEN_CAM = "cam_screen"
        const val SCREEN_AREA = "area_screen"
    }
}