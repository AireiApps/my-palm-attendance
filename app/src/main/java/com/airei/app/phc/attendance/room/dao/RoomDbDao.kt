package com.airei.app.phc.attendance.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.airei.app.phc.attendance.api.ApiDetails
import com.airei.app.phc.attendance.common.AppPreferences
import com.airei.app.phc.attendance.entity.BlockEntity
import com.airei.app.phc.attendance.entity.DivisionEntity
import com.airei.app.phc.attendance.entity.EmpAttendanceTable
import com.airei.app.phc.attendance.entity.EmployeeBioTable
import com.airei.app.phc.attendance.entity.EmployeeTable
import com.airei.app.phc.attendance.entity.EstateEntity
import com.airei.app.phc.attendance.entity.ParcelEntity
import com.airei.app.phc.attendance.entity.UserTable

@Dao
interface UserDao {
    // Create (Insert)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserTable)
    // Read (Get all users)
    @Query("SELECT * FROM user_table")
    fun getAllUsers(): LiveData<List<UserTable>>
    // Update
    @Update
    suspend fun updateUser(user: UserTable)
    // Delete
    @Delete
    suspend fun deleteUser(user: UserTable)
    @Query("DELETE FROM user_table WHERE user_id = :userId")
    suspend fun deleteUserID(userId: String)
}

@Dao
interface EmployeeDao {
    // Insert single or list of employees
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployee(employee: EmployeeTable)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployees(employees: List<EmployeeTable>)
    // Delete employees by apiType
    @Query("DELETE FROM employee_table WHERE api_type = :apiType")
    suspend fun deleteByApiType(apiType: String)
    // Delete all records from employee_table
    @Query("DELETE FROM employee_table")
    suspend fun deleteAllEmployees()
    // Get all employees filtered by apiType as LiveData
    @Query("SELECT * FROM employee_table WHERE api_type = :apiType")
    fun getEmployeesByApiType(apiType: String = AppPreferences.apiType): LiveData<List<EmployeeTable>>
}

@Dao
interface EmployeeBioDao {

    // Insert single employee bio
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployeeBio(employeeBio: EmployeeBioTable)

    // Insert multiple employee bios
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployeeBios(employeeBios: List<EmployeeBioTable>)

    // Update employee bio
    @Update
    suspend fun updateEmployeeBio(employeeBio: EmployeeBioTable)

    // Delete all records
    @Query("DELETE FROM emp_bio_table")
    suspend fun deleteAllEmployeeBio()

    // Get all employee bios as LiveData
    @Query("SELECT * FROM emp_bio_table")
    fun getAllEmployeeBios(): LiveData<List<EmployeeBioTable>>

    // Get employee bios filtered by apiType
    @Query("SELECT * FROM emp_bio_table WHERE api_type = :apiType")
    fun getEmployeeBiosByApiType(apiType: String = AppPreferences.apiType): LiveData<List<EmployeeBioTable>>
}

@Dao
interface EmpAttendanceDao {
    // Insert (single record) -> replace if conflict
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: EmpAttendanceTable)
    // Update
    @Update
    suspend fun updateAttendance(attendance: EmpAttendanceTable)
    // Delete
    @Delete
    suspend fun deleteAttendance(attendance: EmpAttendanceTable)
    // Get all by api_type
    @Query("SELECT * FROM emp_attendance_table WHERE api_type = :apiType ORDER BY in_date DESC")
    fun getAllAttendance(apiType: String): LiveData<List<EmpAttendanceTable>>

    // Delete all records
    @Query("DELETE FROM emp_attendance_table")
    suspend fun deleteAllAttendance()

    @Query("""
    SELECT * FROM emp_attendance_table 
    WHERE in_date >= :startTime 
      AND in_date < :endTime 
      AND api_type = :apiType
    ORDER BY emp_user_id DESC
""")
    fun getAllTodayAttendance(
        startTime: Long,
        endTime: Long,
        apiType: String = AppPreferences.apiType
    ): LiveData<List<EmpAttendanceTable>>

}

@Dao
interface EstateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: List<EstateEntity>)

    @Query("DELETE FROM estate_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM estate_table")
    fun getAll(): LiveData<List<EstateEntity>>
}

@Dao
interface DivisionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: List<DivisionEntity>)

    @Query("DELETE FROM division_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM division_table")
    fun getAll(): LiveData<List<DivisionEntity>>
}

@Dao
interface BlockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: List<BlockEntity>)

    @Query("DELETE FROM block_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM block_table")
    fun getAll(): LiveData<List<BlockEntity>>
}

@Dao
interface ParcelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: List<ParcelEntity>)

    @Query("DELETE FROM parcel_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM parcel_table")
    fun getAll(): LiveData<List<ParcelEntity>>
}

