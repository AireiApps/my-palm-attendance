package com.airei.app.phc.attendance.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.airei.app.phc.attendance.common.AppPreferences
import com.airei.app.phc.attendance.room.FloatArrayConverter
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user_table")
data class UserTable(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "local_id") val localId: Int = 0,
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "designation_id") val designationId: String,
    @ColumnInfo(name = "designation_name") val designationName: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "welcome_msg") val welcomeMsg: String,
    @ColumnInfo(name = "api_type") val apiType: String,
    @ColumnInfo(name = "latitude") val lat: String = "",
    @ColumnInfo(name = "longitude") val long: String = "",
)

@Entity(tableName = "emp_bio_table")
@TypeConverters(FloatArrayConverter::class)
data class EmployeeBioTable(
    @PrimaryKey @ColumnInfo(name = "emp_user_id") @SerializedName("emp_user_id") val empUserId: String,
    @ColumnInfo("emp_face_data") @SerializedName("emp_face_data") var empFaceData: List<FloatArray> = listOf(),
    @ColumnInfo("upload_status") @SerializedName("upload_status") var uploadStatus: Boolean = false,
    @ColumnInfo(name = "online_id") @SerializedName("online_id") var onlineId: String = "",
    @ColumnInfo(name = "in_date") @SerializedName("in_date") var inDate: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "mod_date") @SerializedName("mod_date") var modDate: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "api_type") @SerializedName("api_type") var apiType: String = "",
)

@Entity(tableName = "employee_table")
data class EmployeeTable(
    @PrimaryKey @ColumnInfo(name = "user_id") val userId: String, // Primary key
    @ColumnInfo(name = "emp_code") val empCode: String,
    @ColumnInfo(name = "emp_type") val empType: String,
    @ColumnInfo(name = "emp_type_id") val empTypeName: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image") val image: String?, // Nullable field
    @ColumnInfo(name = "desig_id") val desigId: String,
    @ColumnInfo(name = "designation") val designation: String,
    @ColumnInfo(name = "dept_id") val deptId: String,
    @ColumnInfo(name = "department") val department: String,
    @ColumnInfo(name = "face_code") val faceCode: String,
    @ColumnInfo(name = "estate_id") val estateId: String? = null,
    @ColumnInfo(name = "division_id") val divisionId: String? = null,
    @ColumnInfo(name = "block_id") val blockId: String? = null,
    @ColumnInfo(name = "api_type") val apiType: String = "",
)

@Entity(tableName = "emp_group_table")
data class EmpGroupTable(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("l_id") val locId: Int = 0,
    @ColumnInfo("login_id") val loginId: String = "",
    @ColumnInfo("group_id") val id: String,
    @ColumnInfo("group_name") val groupName: String,
    @ColumnInfo("category") val category: String,
    @ColumnInfo("user_id") val userId: String,
    @ColumnInfo(name = "estate_id") val estateId: String? = null,
    @ColumnInfo(name = "division_id") val divisionId: String? = null,
    @ColumnInfo(name = "block_id") val blockId: String? = null,
)

// save Report
@Entity(tableName = "emp_attendance_table")
data class EmpAttendanceTable(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "local_id") @SerializedName("local_id") val localId: Int = 0,
    @ColumnInfo(name = "emp_user_id") @SerializedName("emp_user_id") val empUserId: String = "",
    @ColumnInfo(name = "in_time") @SerializedName("in_time") val inTime: String = "0",
    @ColumnInfo(name = "out_time") @SerializedName("out_time") val outTime: String = "0",
    @ColumnInfo(name = "in_status") @SerializedName("in_status") val inStatus: String = "",
    @ColumnInfo(name = "out_status") @SerializedName("out_status") val outStatus: String = "",
    @ColumnInfo(name = "user_id") @SerializedName("user_id") var userId: String = AppPreferences.loginId,
    @ColumnInfo(name = "site_id") @SerializedName("site_id") var siteId: String = "",
    @ColumnInfo(name = "estate_id") @SerializedName("estate_id") var estateId: String = "",
    @ColumnInfo(name = "division_id") @SerializedName("division_id") var divisionId: String = "",
    @ColumnInfo(name = "block_id") @SerializedName("block_id") var blockId: String = "",
    @ColumnInfo(name = "in_date") @SerializedName("in_date") var inDate: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "mod_date") @SerializedName("mod_date") var modDate: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "upload_status") @SerializedName("upload_status") var uploadStatus: Boolean = false,
    @ColumnInfo(name = "online_id") @SerializedName("online_id") var onlineId: String = "",
    @ColumnInfo(name = "api_type") @SerializedName("api_type") var apiType: String = "",
)