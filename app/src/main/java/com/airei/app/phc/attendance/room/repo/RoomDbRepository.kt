package com.airei.app.phc.attendance.room.repo

import androidx.lifecycle.LiveData
import com.airei.app.phc.attendance.common.AppPreferences
import com.airei.app.phc.attendance.entity.EmpAttendanceTable
import com.airei.app.phc.attendance.entity.EmployeeBioTable
import com.airei.app.phc.attendance.entity.EmployeeTable
import com.airei.app.phc.attendance.entity.UserTable
import com.airei.app.phc.attendance.room.dao.EmpAttendanceDao
import com.airei.app.phc.attendance.room.dao.EmployeeBioDao
import com.airei.app.phc.attendance.room.dao.EmployeeDao
import com.airei.app.phc.attendance.room.dao.UserDao
import javax.inject.Inject

class RoomDbRepository @Inject constructor(
    private val userDao: UserDao,
    private val employeeDao: EmployeeDao,
    private val employeeBioDao: EmployeeBioDao,
    private val empAttendanceDao: EmpAttendanceDao,
) {

    // =========================
    // UserDao
    // =========================
    val allUsers: LiveData<List<UserTable>> = userDao.getAllUsers()

    suspend fun insertUser(user: UserTable) = userDao.insertUser(user)
    suspend fun updateUser(user: UserTable) = userDao.updateUser(user)
    suspend fun deleteUser(user: UserTable) = userDao.deleteUser(user)
    suspend fun deleteUserById(userId: String) = userDao.deleteUserID(userId)

    // =========================
    // EmployeeDao
    // =========================
    val allEmployees: LiveData<List<EmployeeTable>> = employeeDao.getEmployeesByApiType() // all data
    suspend fun insertEmployee(employee: EmployeeTable) = employeeDao.insertEmployee(employee)
    suspend fun insertEmployees(employees: List<EmployeeTable>) = employeeDao.insertEmployees(employees)
    suspend fun deleteEmployeesByApiType(apiType: String) = employeeDao.deleteByApiType(apiType)

    // =========================
    // EmployeeBioDao
    // =========================
    val allEmployeeBios: LiveData<List<EmployeeBioTable>> = employeeBioDao.getAllEmployeeBios()

    suspend fun insertEmployeeBio(employeeBio: EmployeeBioTable) =
        employeeBioDao.insertEmployeeBio(employeeBio)

    suspend fun insertEmployeeBios(employeeBios: List<EmployeeBioTable>) =
        employeeBioDao.insertEmployeeBios(employeeBios)

    suspend fun updateEmployeeBio(employeeBio: EmployeeBioTable) =
        employeeBioDao.updateEmployeeBio(employeeBio)

    suspend fun deleteEmployeeBioById(empUserId: String) = employeeBioDao.deleteByUserId(empUserId)
    suspend fun deleteEmployeeBiosByApiType(apiType: String) =
        employeeBioDao.deleteByApiType(apiType)

    fun getEmployeeBiosByApiType(apiType: String = AppPreferences.apiType): LiveData<List<EmployeeBioTable>> =
        employeeBioDao.getEmployeeBiosByApiType(apiType)
    // =========================
    // EmpAttendanceDao
    // =========================
    fun getAllAttendance(apiType: String = AppPreferences.apiType): LiveData<List<EmpAttendanceTable>> =
        empAttendanceDao.getAllAttendance(apiType)

    fun getAllTodayAttendance(stateTime: Long, endTime: Long ): LiveData<List<EmpAttendanceTable>> =
        empAttendanceDao.getAllTodayAttendance(stateTime, endTime)

    suspend fun insertAttendance(attendance: EmpAttendanceTable) =
        empAttendanceDao.insertAttendance(attendance)

    suspend fun updateAttendance(attendance: EmpAttendanceTable) =
        empAttendanceDao.updateAttendance(attendance)

    suspend fun deleteAttendance(attendance: EmpAttendanceTable) =
        empAttendanceDao.deleteAttendance(attendance)

}

