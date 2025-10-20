package com.airei.app.phc.attendance.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.airei.app.phc.attendance.R
import com.airei.app.phc.attendance.api.ApiResponse
import com.airei.app.phc.attendance.common.AppPreferences
import com.airei.app.phc.attendance.databinding.FragmentUploadDataBinding
import com.airei.app.phc.attendance.entity.AttendanceReq
import com.airei.app.phc.attendance.entity.EmpAttendanceTable
import com.airei.app.phc.attendance.entity.EmpFaceAccessReq
import com.airei.app.phc.attendance.entity.EmployeeBioTable
import com.airei.app.phc.attendance.entity.OnlineData
import com.airei.app.phc.attendance.utils.saveDataToDownloadsScoped
import com.airei.app.phc.attendance.viewmodel.ApiViewModel
import com.airei.app.phc.attendance.viewmodel.RoomViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadDataFragment : Fragment() {
    private var _binding: FragmentUploadDataBinding? = null
    private val binding get() = _binding!!

    private val viewModelRDB: RoomViewModel by activityViewModels()
    private val viewModelAPI: ApiViewModel by activityViewModels()

    private var empFaceDataList: List<EmployeeBioTable>? = null
    private var attendanceDataList: List<EmpAttendanceTable>? = null
    private val loadingState: MutableLiveData<Boolean> = MutableLiveData()

    private val _apiUploadStatus = MutableLiveData<Map<String, Boolean>>(
        mapOf(
            EMP_BIO to false, EMP_ATTENDANCE to false
        )
    )
    private val apiUploadStatus: LiveData<Map<String, Boolean>> = _apiUploadStatus

    private var stateUpload: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUploadDataBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    goBackPage()
                }
            })
        setToolBar(
            screenTitle = getString(R.string.uploading_data), showBack = true
        )
        setProcessBar()
        observeDataRoomDb()
        setBtnClickAction()
    }

    private fun setProcessBar() {
        with(binding) {
            progressLayout.visibility = View.INVISIBLE
            apiUploadStatus.observe(viewLifecycleOwner) { statusMap ->
                if (statusMap.isNullOrEmpty()) return@observe

                val total = statusMap.size
                val done = statusMap.values.count { it }
                val percent = ((done.toFloat() / total) * 100).toInt()
                // ✅ Update progress bar and text
                binding.progressBar.progress = percent
                binding.tvProgress.text = "$percent%"

                // (Optional) Log or show statuses
                statusMap.forEach { (key, value) ->
                    Log.d(TAG, "upload status : $key -> ${if (value) "DONE" else "PENDING"}")
                }
                // ✅ If all uploads are completed
                if (done == total) {
                    Toast.makeText(requireContext(), "Upload Complete ✅", Toast.LENGTH_SHORT).show()
                    // Delay hiding progress layout
                    lifecycleScope.launch {
                        delay(1500) // 1.5-second delay before hiding
                        progressLayout.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    private fun setBtnClickAction() {
        with(binding) {
            btnTryAgain.setOnClickListener {
                startUpload()
            }

            btnUploadData.setOnClickListener {
                startUpload()
            }

            btnCancel.setOnClickListener {
                goBackPage()
            }
        }
    }

    private fun startUpload() {
        with(binding) {
            _apiUploadStatus.value = mapOf(
                "EMP_BIO" to false, "EMP_ATTENDANCE" to false
            )
            progressBar.progress = 0
            tvProgress.text = "0%"
            binding.progressLayout.visibility = View.VISIBLE
            stateUpload = true
            observeDataRoomDb()
        }
    }

    fun updateUploadStatus(key: String, status: Boolean) {
        val currentMap = _apiUploadStatus.value?.toMutableMap() ?: mutableMapOf()
        currentMap[key] = status
        _apiUploadStatus.postValue(currentMap)
    }


    private fun observeDataRoomDb() {
        loadingState.observe(viewLifecycleOwner) {
            if (empFaceDataList != null && attendanceDataList != null) {
                if (empFaceDataList!!.isNotEmpty() || attendanceDataList!!.isNotEmpty()) {
                    binding.emptyLayout.visibility = View.GONE
                    binding.uploadLayout.visibility = View.VISIBLE
                    if (stateUpload) {
                        startUploadData()
                    }
                } else {
                    binding.emptyLayout.visibility = View.VISIBLE
                    binding.uploadLayout.visibility = View.GONE
                }
            }
        }

        with(viewModelRDB) {
            getAllAttendance().observe(viewLifecycleOwner) { atten ->
                getAllAttendance().removeObserver { }
                if (atten != null) {
                    attendanceDataList =
                        atten.filter { a -> a.apiType == AppPreferences.apiType && !a.uploadStatus }
                    Log.d(TAG, "observeData: attendanceDataList: ${attendanceDataList?.size}")
                    loadingState.postValue(true)
                } else {
                    attendanceDataList = emptyList()
                    loadingState.postValue(false)
                }
            }
            getEmployeeBiosByApiType().observe(viewLifecycleOwner) { bio ->
                getEmployeeBiosByApiType().removeObserver { }
                if (bio != null) {
                    empFaceDataList =
                        bio.filter { a -> a.apiType == AppPreferences.apiType && !a.uploadStatus }
                    Log.d(TAG, "observeData: empFaceDataList: ${empFaceDataList?.size}")
                    loadingState.postValue(true)
                } else {
                    empFaceDataList = emptyList()
                    loadingState.postValue(false)
                }

            }
        }
    }

    private fun startUploadData() {
        with(viewModelAPI) {
            stateUpload = false
            if (!empFaceDataList.isNullOrEmpty() || !attendanceDataList.isNullOrEmpty()) {
                // launch coroutine for delay
                lifecycleScope.launch {
                    delay(100)
                    uploadAttendance(attendanceDataList!!)
                    delay(500)
                    uploadEmpFace(empFaceDataList)
                }
            } else {
                Toast.makeText(
                    requireContext(), getString(R.string.no_data_to_upload), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun uploadAttendance(attendanceDataList: List<EmpAttendanceTable>? = null) {
        with(viewModelAPI) {
            if (attendanceDataList.isNullOrEmpty()) {
                updateUploadStatus("EMP_ATTENDANCE", true)
                return
            }

            val reqData = AttendanceReq(attendanceDataList)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveDataToDownloadsScoped(
                    requireContext(),
                    "MyPalmAttendance-EmpAttendance",
                    reqData
                )
            }
            // Launch coroutine to call suspend function
            viewModelScope.launch {
                try {
                    val call: Call<ApiResponse<OnlineData>> =
                        saveEmpAttendance(reqData) // suspend function
                    // Enqueue inside coroutine
                    call.enqueue(object : Callback<ApiResponse<OnlineData>> {
                        override fun onResponse(
                            call: Call<ApiResponse<OnlineData>>,
                            response: Response<ApiResponse<OnlineData>>
                        ) {
                            if (response.isSuccessful) {
                                Log.d(TAG, "Attendance upload success [${response.body()}]")
                                val uploadData = response.body()?.data?.dbId ?: listOf()

                                uploadData.forEach { u ->
                                    val updatedAtten =
                                        attendanceDataList.find { a -> a.localId.toString() == u.jobId && a.empUserId == u.empUserId }
                                    if (updatedAtten != null) {
                                        viewModelRDB.updateAttendance(updatedAtten.apply {
                                            uploadStatus = true
                                            onlineId = u.onlineId
                                        })
                                    }
                                }

                                updateUploadStatus("EMP_ATTENDANCE", true)
                            } else {
                                Log.e(TAG, "Attendance upload failed: ${response.code()}")
                            }
                        }

                        override fun onFailure(call: Call<ApiResponse<OnlineData>>, t: Throwable) {
                            Log.e(TAG, "Attendance upload failed: ${t.message}")
                        }
                    })
                } catch (e: Exception) {
                    Log.e(TAG, "Exception: ${e.message}")
                }
            }
        }
    }

    private fun uploadEmpFace(empFace: List<EmployeeBioTable>? = null) {
        with(viewModelAPI) {
            if (empFace.isNullOrEmpty()) {
                updateUploadStatus(EMP_BIO, true)
                return
            }

            val reqData = EmpFaceAccessReq(empFace)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveDataToDownloadsScoped(requireContext(), "MyPalmAttendance-EmpFace", reqData)
            }
            // Launch coroutine to call suspend function
            viewModelScope.launch {
                try {
                    val call: Call<ApiResponse<OnlineData>> =
                        saveEmpFace(reqData) // suspend function
                    // Enqueue inside coroutine
                    call.enqueue(object : Callback<ApiResponse<OnlineData>> {
                        override fun onResponse(
                            call: Call<ApiResponse<OnlineData>>,
                            response: Response<ApiResponse<OnlineData>>
                        ) {
                            if (response.isSuccessful) {
                                Log.d(TAG, "Face-Access upload success [${response.body()}]")
                                val uploadData = response.body()?.data?.dbId ?: listOf()

                                uploadData.forEach { u ->
                                    val updatedData =
                                        empFace.find { a -> a.empUserId.toString() == u.onlineId }
                                    if (updatedData != null) {
                                        viewModelRDB.updateEmployeeBio(updatedData.apply {
                                            uploadStatus = true
                                            onlineId = u.onlineId
                                        })
                                    }
                                }
                                updateUploadStatus(EMP_BIO, true)
                            } else {
                                Log.e(TAG, "Face-Access upload failed: ${response.code()}")
                            }
                        }

                        override fun onFailure(call: Call<ApiResponse<OnlineData>>, t: Throwable) {
                            Log.e(TAG, "Face-Access upload failed: ${t.message}")
                        }
                    })
                } catch (e: Exception) {
                    Log.e(TAG, "Exception: ${e.message}")
                }
            }
        }
    }

    private fun setToolBar(
        screenTitle: String = getString(R.string.app_name),
        showBack: Boolean = false,
    ) {
        with(binding.topToolBar) {
            tvTitle.text = screenTitle

            // set start icon → logo or back arrow
            if (showBack) {
                imgBack.setImageResource(R.drawable.ic_arrow_back)
                imgBack.setOnClickListener {
                    goBackPage()
                }
            } else {
                imgBack.setImageResource(R.drawable.img_logo_my_palm_2x)
                imgBack.setOnClickListener(null)
            }
            imgOption.visibility = View.INVISIBLE
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
        const val TAG: String = "UploadDataFragment"
        const val EMP_BIO = "EMP_BIO"
        const val EMP_ATTENDANCE = "EMP_ATTENDANCE"
    }
}