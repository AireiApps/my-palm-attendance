package com.airei.app.phc.attendance.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airei.app.phc.attendance.api.ApiRepository
import com.airei.app.phc.attendance.api.ApiResponse
import com.airei.app.phc.attendance.entity.AttendanceReq
import com.airei.app.phc.attendance.entity.EmpFaceAccessReq
import com.airei.app.phc.attendance.entity.MillLoginResponse
import com.airei.app.phc.attendance.entity.PlantationLoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApiViewModel @Inject constructor(
    private val repository: ApiRepository
) : ViewModel() {

    private val _millLoginState = MutableLiveData<Result<ApiResponse<MillLoginResponse>?>?>()
    val millLoginState: MutableLiveData<Result<ApiResponse<MillLoginResponse>?>?> = _millLoginState

    private val _plantationLoginState = MutableLiveData<Result<ApiResponse<PlantationLoginResponse>?>?>()
    val plantationLoginState: MutableLiveData<Result<ApiResponse<PlantationLoginResponse>?>?> = _plantationLoginState

    fun clearLogin(){
        _millLoginState.value = null
        _plantationLoginState.value = null
    }


    fun loginUserMill(millCode: String, username: String, password: String) {
        viewModelScope.launch {
            try {
                val result = repository.loginUserMill(millCode, username, password)
                _millLoginState.postValue(Result.success(result))
            } catch (e: Exception) {
                _millLoginState.postValue(Result.failure(e))
            }
        }
    }

    fun loginUserPlantation(username: String, password: String) {
        viewModelScope.launch {
            try {
                val result = repository.loginUserPlantation(username, password)
                _plantationLoginState.postValue(Result.success(result))
            } catch (e: Exception) {
                _plantationLoginState.postValue(Result.failure(e))
            }
        }
    }

    fun getMillEmployeeList() = repository.getMillEmployeeList()
    fun getPlantationEmployeeList() = repository.getPlantationEmployeeList()

    fun empFaceList() = repository.empFaceList()

    fun saveEmpAttendance(data: AttendanceReq) = repository.saveEmpAttendance(data)
    fun saveEmpFace(data: EmpFaceAccessReq) = repository.saveEmpFace(data)
    fun estateList() = repository.estateList()
    fun divisionList() = repository.divisionList()
    fun blockList() = repository.blockList()
    fun parcelList() = repository.parcelList()


}
