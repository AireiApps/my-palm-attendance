package com.airei.app.phc.attendance.room.repo

import androidx.lifecycle.LiveData
import com.airei.app.phc.attendance.common.AppPreferences
import com.airei.app.phc.attendance.entity.BlockEntity
import com.airei.app.phc.attendance.entity.DivisionEntity
import com.airei.app.phc.attendance.entity.EmpAttendanceTable
import com.airei.app.phc.attendance.entity.EmployeeBioTable
import com.airei.app.phc.attendance.entity.EmployeeTable
import com.airei.app.phc.attendance.entity.EstateEntity
import com.airei.app.phc.attendance.entity.ParcelEntity
import com.airei.app.phc.attendance.entity.UserTable
import com.airei.app.phc.attendance.room.dao.BlockDao
import com.airei.app.phc.attendance.room.dao.DivisionDao
import com.airei.app.phc.attendance.room.dao.EmpAttendanceDao
import com.airei.app.phc.attendance.room.dao.EmployeeBioDao
import com.airei.app.phc.attendance.room.dao.EmployeeDao
import com.airei.app.phc.attendance.room.dao.EstateDao
import com.airei.app.phc.attendance.room.dao.ParcelDao
import com.airei.app.phc.attendance.room.dao.UserDao
import javax.inject.Inject

class RoomDbRepository @Inject constructor(
    private val userDao: UserDao,
    private val employeeDao: EmployeeDao,
    private val employeeBioDao: EmployeeBioDao,
    private val empAttendanceDao: EmpAttendanceDao,
    private val estateDao: EstateDao,
    private val divisionDao: DivisionDao,
    private val blockDao: BlockDao,
    private val parcelDao: ParcelDao
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
    suspend fun deleteAllEmployees() = employeeDao.deleteAllEmployees()

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

    //deleteAllEmployeeBio
    suspend fun deleteAllEmployeeBio() = employeeBioDao.deleteAllEmployeeBio()

    //getEmployeeBiosByApiType
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

    // 🏠 Estate
    suspend fun insertEstateList(list: List<EstateEntity>) = estateDao.insertList(list)
    fun getAllEstates() = estateDao.getAll()
    suspend fun deleteAllEstates() = estateDao.deleteAll()

    // 🏢 Division
    suspend fun insertDivisionList(list: List<DivisionEntity>) = divisionDao.insertList(list)
    fun getAllDivisions() = divisionDao.getAll()
    suspend fun deleteAllDivisions() = divisionDao.deleteAll()

    // 🌳 Block
    suspend fun insertBlockList(list: List<BlockEntity>) = blockDao.insertList(list)
     fun getAllBlocks() = blockDao.getAll()
    suspend fun deleteAllBlocks() = blockDao.deleteAll()

    // 📦 Parcel
    suspend fun insertParcelList(list: List<ParcelEntity>) = parcelDao.insertList(list)
    fun getAllParcels() = parcelDao.getAll()
    suspend fun deleteAllParcels() = parcelDao.deleteAll()

}

