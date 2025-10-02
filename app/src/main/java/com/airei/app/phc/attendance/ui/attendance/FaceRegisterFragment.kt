package com.airei.app.phc.attendance.ui.attendance

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.airei.app.phc.attendance.databinding.FragmentFaceRegisterBinding
import com.airei.app.phc.attendance.entity.EmployeeBioTable
import com.airei.app.phc.attendance.entity.EmployeeTable
import com.airei.app.phc.attendance.entity.MatchData
import com.airei.app.phc.attendance.utils.showEmpDialog
import com.airei.app.phc.attendance.viewmodel.RoomViewModel


class FaceRegisterFragment : Fragment() {

    private var _binding: FragmentFaceRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RoomViewModel by activityViewModels()

    private var isDetectionEnabled: Boolean = false

    private var faceHelper: FaceRecognitionHelper? = null

    private var empId: String? = null
    private var empBioData: EmployeeBioTable? = null
    private var empBioList: List<EmployeeBioTable>? = null
    private var empData: EmployeeTable? = null

    private var currentFaceImage: Bitmap? = null
    private var currentFaceEmbedding: FloatArray? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFaceRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        empId = arguments?.getString("select_emp_id")
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    goBackPage()
                }
            })
        setToolBar()
        observeData()
        clickAction()
        setCamerax()
    }

    private fun clickAction() {
        with(binding) {
            btnAdd.setOnClickListener {
                try {
                    if (faceHelper != null) {
                        if (currentFaceImage != null && currentFaceEmbedding != null) {
                            val locImage = currentFaceImage
                            val locEmbedding = currentFaceEmbedding
                            toggleDetection(false, faceHelper = faceHelper!!)
                            val findMatch: List<MatchData>? = faceHelper?.findNearest(
                                locEmbedding!!,
                                (empBioList ?: listOf())
                            )?.filter { it.distance < 0.7 }
                            var isSameUser: Boolean = findMatch.isNullOrEmpty()
                            if (empBioData != null && !findMatch.isNullOrEmpty()) {
                                isSameUser =
                                    findMatch.firstOrNull()?.empUserId == empBioData?.empUserId
                            }
                            if (isSameUser) {
                                var newEmpBioData = EmployeeBioTable(
                                    empUserId = empId ?: empData?.userId ?: "",
                                    empFaceData = listOf(locEmbedding!!),
                                    apiType = AppPreferences.apiType
                                )
                                requireActivity().showEmpDialog(
                                    bitmap = locImage,
                                    empName = empData?.name ?: "unknown user [${empId}]",
                                ) {
                                    if (empBioData != null) {
                                        newEmpBioData = newEmpBioData.copy(
                                            localId = empBioData?.localId ?: 0,
                                        )
                                    }
                                    viewModel.insertEmployeeBio(newEmpBioData)
                                    Toast.makeText(
                                        requireContext(),
                                        "Face Registered Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    goBackPage()
                                }
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Face Already Registered",
                                    Toast.LENGTH_SHORT
                                ).show()
                                toggleDetection(true, faceHelper = faceHelper!!)
                            }
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Please capture a face",
                                Toast.LENGTH_SHORT
                            ).show()
                            //toggleDetection(true, faceHelper = faceHelper!!)
                        }
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
            // toggle detection
            binding.btnRegister.setOnClickListener {
                if (faceHelper != null) {
                    toggleDetection(!isDetectionEnabled, faceHelper = faceHelper!!)
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

    @SuppressLint("SetTextI18n")
    private fun observeData() {
        with(viewModel) {
            getAllEmployees().observe(viewLifecycleOwner) {
                if (it.isNotEmpty() && empId != null) {
                    empData = it.find { emp -> emp.userId == (empId ?: "") }
                    binding.tvEmpName.text = "${empData?.name} [${empData?.userId}]"
                    Log.d(TAG, "observeData empData: ${empData?.name} [${empData?.userId}]")
                }
                getAllEmployees().removeObserver { }
            }
            getAllEmployeeBios().observe(viewLifecycleOwner) {
                if (it.isNotEmpty() && empId != null) {
                    empBioList = it ?: listOf()
                    empBioData = it.find { emp -> emp.empUserId == (empId ?: "") }
                    Log.d(TAG, "observeData empBioData: ${empBioData?.empUserId}")
                }
                getAllEmployeeBios().removeObserver { }
            }
        }
    }

    private fun setToolBar() {
        binding.topToolBar.apply {
            tvTitle.text = getString(R.string.register_face)
            imgBack.setOnClickListener { goBackPage() }
            imgOption.visibility = View.VISIBLE
        }
    }

    private fun setCamerax() {
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
            if (_binding != null) {
                binding.imgFace.setImageBitmap(bitmap)
            }
        }

        faceHelper!!.onNoFace = {
            Log.d(TAG, "FaceRecognitionHelper: No face detected")
            if (_binding != null) {
                binding.imgFace.setImageBitmap(null)
            }
        }

        faceHelper!!.onMultipleFaces = {
            Log.d(TAG, "FaceRecognitionHelper:  Multiple faces detected")
            if (_binding != null) {
                binding.imgFace.setImageBitmap(null)
            }
        }

        // start camera
        faceHelper!!.startCamera()

    }

    private fun toggleDetection(
        isDetectEnabled: Boolean,
        faceHelper: FaceRecognitionHelper,
        btn: AppCompatButton = binding.btnRegister,
    ) {
        isDetectionEnabled = isDetectEnabled
        try {
            faceHelper.setDetectionEnabled(isDetectEnabled)
            if (isDetectEnabled) {
                btn.text = getString(R.string.stop_detection)
                btn.backgroundTintList =
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.flame_pea
                    )
            } else {
                btn.text = getString(R.string.start_detection)
                btn.backgroundTintList =
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.green
                    )
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
        const val TAG: String = "FaceRegisterFragment"
    }
}