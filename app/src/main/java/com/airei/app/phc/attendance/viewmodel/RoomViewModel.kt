package com.airei.app.phc.attendance.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airei.app.phc.attendance.common.AppPreferences
import com.airei.app.phc.attendance.entity.EmpAttendanceTable
import com.airei.app.phc.attendance.entity.EmployeeBioTable
import com.airei.app.phc.attendance.entity.EmployeeTable
import com.airei.app.phc.attendance.entity.UserTable
import com.airei.app.phc.attendance.room.repo.RoomDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RoomViewModel @Inject constructor(
    private val repository: RoomDbRepository,
) : ViewModel() {

    // =========================
    // UserDao
    // =========================
    fun insertUser(sites: UserTable) = viewModelScope.launch {
        repository.insertUser(sites)
    }

    fun updateUser(user: UserTable) = viewModelScope.launch {
        repository.updateUser(user)
    }

    fun deleteUser(user: UserTable) = viewModelScope.launch {
        repository.deleteUser(user)
    }

    fun deleteUserID(userID: String) = viewModelScope.launch {
        repository.deleteUserById(userID)
    }

    fun getAllUsers() = repository.allUsers

    // =========================
    // EmployeeDao
    // =========================
    fun insertEmployeeList(employee: List<EmployeeTable>) = viewModelScope.launch {
        repository.insertEmployees(employee)
    }

    fun deleteEmployeeList(apiType: String) = viewModelScope.launch {
        repository.deleteEmployeesByApiType(apiType)
    }

    fun getAllEmployees() = repository.allEmployees

    // =========================
    // EmployeeBioDao
    // =========================

    fun insertEmployeeBio(employeeBio: EmployeeBioTable) = viewModelScope.launch {
        repository.insertEmployeeBio(employeeBio)
    }
    fun insertEmployeeBioList(employeeBios: List<EmployeeBioTable>) = viewModelScope.launch {
        repository.insertEmployeeBios(employeeBios)
    }
    fun updateEmployeeBio(employeeBio: EmployeeBioTable) = viewModelScope.launch {
        repository.updateEmployeeBio(employeeBio)
    }
    fun deleteEmployeeBioById(empUserId: String) = viewModelScope.launch {
        repository.deleteEmployeeBioById(empUserId)
    }
    fun deleteEmployeeBiosByApiType(apiType: String) = viewModelScope.launch {
        repository.deleteEmployeeBiosByApiType(apiType)
    }
    fun getAllEmployeeBios(): LiveData<List<EmployeeBioTable>> = repository.allEmployeeBios
    fun getEmployeeBiosByApiType(apiType: String = AppPreferences.apiType): LiveData<List<EmployeeBioTable>> =
        repository.getEmployeeBiosByApiType(apiType)

    // =========================
// EmpAttendanceDao
// =========================

    fun insertAttendance(attendance: EmpAttendanceTable) = viewModelScope.launch {
        repository.insertAttendance(attendance)
    }

    fun updateAttendance(attendance: EmpAttendanceTable) = viewModelScope.launch {
        repository.updateAttendance(attendance)
    }

    fun deleteAttendance(attendance: EmpAttendanceTable) = viewModelScope.launch {
        repository.deleteAttendance(attendance)
    }

    fun getAllAttendance(apiType: String = AppPreferences.apiType): LiveData<List<EmpAttendanceTable>> =
        repository.getAllAttendance(apiType)

    fun getAllTodayAttendance(stateTime: Long, endTime: Long): LiveData<List<EmpAttendanceTable>> =
        repository.getAllTodayAttendance(stateTime,endTime)
}