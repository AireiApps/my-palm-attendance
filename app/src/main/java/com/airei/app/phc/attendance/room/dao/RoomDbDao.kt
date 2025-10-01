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
import com.airei.app.phc.attendance.entity.EmpAttendanceTable
import com.airei.app.phc.attendance.entity.EmployeeBioTable
import com.airei.app.phc.attendance.entity.EmployeeTable
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

    // Delete by empUserId
    @Query("DELETE FROM emp_bio_table WHERE emp_user_id = :empUserId")
    suspend fun deleteByUserId(empUserId: String)

    // Delete all records for a specific apiType
    @Query("DELETE FROM emp_bio_table WHERE api_type = :apiType")
    suspend fun deleteByApiType(apiType: String)

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
