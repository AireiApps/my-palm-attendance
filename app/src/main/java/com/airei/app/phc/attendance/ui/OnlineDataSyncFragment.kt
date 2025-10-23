package com.airei.app.phc.attendance.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.airei.app.phc.attendance.R
import com.airei.app.phc.attendance.api.ApiDetails
import com.airei.app.phc.attendance.api.ApiDetails.MILL_API
import com.airei.app.phc.attendance.api.ApiDetails.PLANTATION_API
import com.airei.app.phc.attendance.api.ApiResponse
import com.airei.app.phc.attendance.common.AppPreferences
import com.airei.app.phc.attendance.databinding.FragmentOnlineDataSyncBinding
import com.airei.app.phc.attendance.entity.BlockRes
import com.airei.app.phc.attendance.entity.DivisionRes
import com.airei.app.phc.attendance.entity.EmployeeFaceRes
import com.airei.app.phc.attendance.entity.EstateRes
import com.airei.app.phc.attendance.entity.MillEmployeeResponse
import com.airei.app.phc.attendance.entity.ParcelRes
import com.airei.app.phc.attendance.entity.PlantationEmployeeResponse
import com.airei.app.phc.attendance.entity.toEmployeeFaceTable
import com.airei.app.phc.attendance.entity.toEmployeeTable
import com.airei.app.phc.attendance.entity.toEntity
import com.airei.app.phc.attendance.viewmodel.ApiViewModel
import com.airei.app.phc.attendance.viewmodel.MasterDataViewModel
import com.airei.app.phc.attendance.viewmodel.RoomViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.getValue

class OnlineDataSyncFragment : Fragment() {
    private var _binding: FragmentOnlineDataSyncBinding? = null
    private val binding get() = _binding!!
    private val apiViewModel: ApiViewModel by activityViewModels()
    private val roomViewModel: RoomViewModel by activityViewModels()
    private val masterViewModel: MasterDataViewModel by activityViewModels()

    private val _apiLoadingStatus = if (AppPreferences.apiType == ApiDetails.PLANTATION_API) {
        MutableLiveData<Map<String, Boolean?>>(
            mapOf(
                EMP_BIO to null,
                EMP_DATA to null,
                ESTATE to null,
                DIVISION to null,
                BLOCK to null,
                PARCEL to null
            )
        )
    } else {
        MutableLiveData<Map<String, Boolean?>>(
            mapOf(
                EMP_BIO to null,
                EMP_DATA to null,
            )
        )
    }

    val apiLoadingStatus: LiveData<Map<String, Boolean?>> = _apiLoadingStatus

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentOnlineDataSyncBinding.inflate(layoutInflater, container, false)
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
        apiViewModel.clearLogin()
        Log.d(TAG, "onViewCreated: ")
        setToolBar("Loading", false, false)
        observeApiLoadingStatus()
        binding.btnSync.setOnClickListener {
            AppPreferences.isDataDownloaded = false
            resetAllApiLoadingStatus()
            apiCalling()
        }
        binding.btnSync.visibility = View.INVISIBLE
        apiCalling()
    }

    private fun apiCalling() {
        binding.btnSync.visibility = View.INVISIBLE
        if (AppPreferences.apiType == MILL_API){
            fetchEmpMillList()
            fetchEmpFaceList()
        }else if (AppPreferences.apiType == PLANTATION_API){
            fetchEmpPlantationList()
            fetchEmpFaceList()
            fetchEstates()
            fetchDivisions()
            fetchBlocks()
            fetchParcels()
        }
    }

    // 🏠 Estates
    private fun fetchEstates() {
        setApiLoading(ESTATE, false)
        apiViewModel.estateList().enqueue(object : Callback<ApiResponse<List<EstateRes>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<EstateRes>>>,
                response: Response<ApiResponse<List<EstateRes>>>
            ) {
                if (response.isSuccessful && response.body()?.httpcode == 200) {
                    val data = response.body()?.data ?: emptyList()
                    Log.d(TAG, "✅ Estates received: ${data.size}")
                    val convertData = data.map { d-> d.toEntity() }
                    masterViewModel.deleteAllEstates()
                    masterViewModel.insertEstates(convertData)
                    setApiLoading(ESTATE, true)
                } else {
                    Log.e(TAG, "❌ Estates error: ${response.code()} - ${response.message()}")
                    setApiLoading(ESTATE, false)
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<EstateRes>>>, t: Throwable) {
                Log.e(TAG, "⚠️ Estates network failure: ${t.localizedMessage}")
                setApiLoading(ESTATE, false)
            }
        })
    }

    // 📍 Divisions
    private fun fetchDivisions() {
        setApiLoading(EMP_BIO, false)
        apiViewModel.divisionList().enqueue(object : Callback<ApiResponse<List<DivisionRes>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<DivisionRes>>>,
                response: Response<ApiResponse<List<DivisionRes>>>
            ) {
                if (response.isSuccessful && response.body()?.httpcode == 200) {
                    val data = response.body()?.data ?: emptyList()
                    Log.d(TAG, "✅ Divisions received: ${data.size}")
                    val convertData = data.map { d-> d.toEntity() }
                    masterViewModel.deleteAllDivisions()
                    masterViewModel.insertDivisions(convertData)
                    setApiLoading(DIVISION, true)
                } else {
                    Log.e(TAG, "❌ Divisions error: ${response.code()} - ${response.message()}")
                    setApiLoading(DIVISION, false)
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<DivisionRes>>>, t: Throwable) {
                setApiLoading(DIVISION, false)
                Log.e(TAG, "⚠️ Divisions network failure: ${t.localizedMessage}")
            }
        })
    }

    // 🧱 Blocks
    private fun fetchBlocks() {
        setApiLoading(BLOCK, false)
        apiViewModel.blockList().enqueue(object : Callback<ApiResponse<List<BlockRes>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<BlockRes>>>,
                response: Response<ApiResponse<List<BlockRes>>>
            ) {
                if (response.isSuccessful && response.body()?.httpcode == 200) {
                    val data = response.body()?.data ?: emptyList()
                    Log.d(TAG, "✅ Blocks received: ${data.size}")
                    val convertData = data.map { d-> d.toEntity() }
                    masterViewModel.deleteAllBlocks()
                    masterViewModel.insertBlocks(convertData)
                    setApiLoading(BLOCK, true)
                } else {
                    Log.e(TAG, "❌ Blocks error: ${response.code()} - ${response.message()}")
                    setApiLoading(BLOCK, false)
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<BlockRes>>>, t: Throwable) {
                Log.e(TAG, "⚠️ Blocks network failure: ${t.localizedMessage}")
                setApiLoading(BLOCK, false)
            }
        })
    }

    // 📦 Parcels
    private fun fetchParcels() {
        setApiLoading(PARCEL, false)
        apiViewModel.parcelList().enqueue(object : Callback<ApiResponse<List<ParcelRes>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<ParcelRes>>>,
                response: Response<ApiResponse<List<ParcelRes>>>
            ) {
                if (response.isSuccessful && response.body()?.httpcode == 200) {
                    val data = response.body()?.data ?: emptyList()
                    Log.d(TAG, "✅ Blocks received: ${data.size}")
                    val convertData = data.map { d-> d.toEntity() }
                    masterViewModel.deleteAllParcels()
                    masterViewModel.insertParcels(convertData)
                    setApiLoading(PARCEL, true)
                } else {
                    Log.e(TAG, "❌ Parcels error: ${response.code()} - ${response.message()}")
                    setApiLoading(PARCEL, false)
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<ParcelRes>>>, t: Throwable) {
                Log.e(TAG, "⚠️ Parcels network failure: ${t.localizedMessage}")
                setApiLoading(PARCEL, false)
            }
        })
    }


    private fun fetchEmpFaceList() {
        apiViewModel.empFaceList().enqueue(object :
            Callback<ApiResponse<List<EmployeeFaceRes>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<EmployeeFaceRes>>>,
                response: Response<ApiResponse<List<EmployeeFaceRes>>>
            ) {
                if (response.isSuccessful && response.body()?.httpcode == 200) {
                    val faceList = (response.body()?.data ?: emptyList()).filter { f -> !f.faceCode.isNullOrEmpty() }
                    Log.d(TAG, "API : ✅ Employee face list received: ${faceList.size}")
                    // ✅ Mark success
                    roomViewModel.deleteAllEmployeeBio()
                    if (!faceList.isEmpty()){
                        val convertData = faceList.map { d-> d.toEmployeeFaceTable() }
                        convertData.forEach { fd->
                            roomViewModel.insertEmployeeBio(fd)
                        }

                    }
                    setApiLoading(EMP_BIO, true)
                    // TODO: handle data — save to DB, update UI, etc.
                } else {
                    Log.e(TAG, "API : ❌ Error ${response.code()} - ${response.message()}")
                    setApiLoading(EMP_BIO, false)
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<EmployeeFaceRes>>>, t: Throwable) {
                Log.e(TAG, "API : ⚠️ Network failure - ${t.localizedMessage}")
                setApiLoading(EMP_BIO, false)
            }
        })
    }
    private fun fetchEmpMillList() {
        apiViewModel.getMillEmployeeList().enqueue(object :
            Callback<ApiResponse<List<MillEmployeeResponse>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<MillEmployeeResponse>>>,
                response: Response<ApiResponse<List<MillEmployeeResponse>>>
            ) {
                if (response.isSuccessful && response.body()?.httpcode == 200) {
                    val employees = response.body()?.data ?: emptyList()
                    Log.d(TAG, "API : ✅ Employee list received: ${employees.size}")
                    val convertData = employees.map { d-> d.toEmployeeTable() }
                    roomViewModel.deleteAllEmployees()
                    if (!convertData.isEmpty()){
                        roomViewModel.insertEmployeeList(convertData)
                    }
                    setApiLoading(EMP_DATA, true)
                } else {
                    Log.e(TAG, "API : ⚠️ Error fetching employee list")
                    setApiLoading(EMP_DATA, false)
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<MillEmployeeResponse>>>, t: Throwable) {
                Log.e(TAG, "API : ⚠️ Error fetching employee list: ${t.message}")
                setApiLoading(EMP_DATA, false)
            }
        })

    }

    private fun fetchEmpPlantationList() {

        apiViewModel.getPlantationEmployeeList().enqueue(object :
            Callback<ApiResponse<List<PlantationEmployeeResponse>>> {

            override fun onResponse(
                call: Call<ApiResponse<List<PlantationEmployeeResponse>>>,
                response: Response<ApiResponse<List<PlantationEmployeeResponse>>>
            ) {
                if (response.isSuccessful && response.body()?.httpcode == 200) {
                    val employees = response.body()?.data ?: emptyList()

                    Log.d(TAG, "API : ✅ Employee list received: ${employees.size}")

                    val convertData = employees.map { d-> d.toEmployeeTable() }

                    roomViewModel.deleteAllEmployees()
                    if (!convertData.isEmpty()){
                        roomViewModel.insertEmployeeList(convertData)
                    }

                    // ✅ Mark as data loaded
                    setApiLoading(EMP_DATA, true)

                    // TODO: Save employees or update UI
                } else {
                    Log.e(TAG, "API : ❌ Error ${response.code()} - ${response.message()}")
                    setApiLoading(EMP_DATA, false)
                }
            }

            override fun onFailure(
                call: Call<ApiResponse<List<PlantationEmployeeResponse>>>,
                t: Throwable
            ) {
                Log.e(TAG, "API : ⚠️ Network failure - ${t.localizedMessage}")
                setApiLoading(EMP_DATA, false)
            }
        })
    }



    private fun observeApiLoadingStatus() {
        apiLoadingStatus.observe(viewLifecycleOwner) { statusMap ->
            // Check if all APIs are false or null (treat null as not loading)
            Log.e(TAG, "API Loading Status: $statusMap")
            val allApiDone = statusMap.values.all { it != null }
            Log.e(TAG, "All API calls done: $allApiDone")
            if (allApiDone) {
                val allComplete = statusMap.values.all { it != false }
                Log.e(TAG, "All API calls completed: $allComplete")
                if (allComplete) {
                    AppPreferences.isDataDownloaded = true
                    binding.btnSync.visibility = View.INVISIBLE
                    Log.e(TAG, "All API calls completed")
                    findNavController().navigate(R.id.attendanceHomeFragment)
                }else{
                    binding.btnSync.visibility = View.VISIBLE
                }
            }
        }

    }

    private fun resetAllApiLoadingStatus() {
        val currentMap = _apiLoadingStatus.value?.toMutableMap() ?: mutableMapOf()
        for (key in currentMap.keys) {
            currentMap[key] = false
        }
        _apiLoadingStatus.value = currentMap
    }


    private fun setApiLoading(key: String, isLoading: Boolean) {
        val currentMap = _apiLoadingStatus.value?.toMutableMap() ?: mutableMapOf()
        currentMap[key] = isLoading
        _apiLoadingStatus.value = currentMap
    }

    private fun setToolBar(
        screenTitle: String = getString(R.string.app_name),
        showBack: Boolean = false,
        showAppLogo: Boolean = false,
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
                if(showAppLogo){
                    imgBack.setImageResource(R.drawable.img_logo_my_palm_2x)
                    imgBack.setOnClickListener(null)
                }else{
                    imgBack.visibility = View.INVISIBLE
                }

            }
            imgOption.visibility = View.INVISIBLE
        }
    }

    private fun goBackPage() {
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG: String = "OnlineDataSyncFragment"
        const val EMP_BIO = "EMP_BIO"
        const val EMP_DATA = "EMP_DATA"
        const val ESTATE = "ESTATE"
        const val DIVISION = "DIVISION"
        const val BLOCK = "BLOCK"
        const val PARCEL = "PARCEL"
    }
}