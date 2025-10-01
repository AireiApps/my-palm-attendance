package com.airei.app.plantation.phc.entity

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class LoginRes(
    @SerializedName("user_id") val userId: String,
    @SerializedName("username") val username: String,
    @SerializedName("name") val name: String,
    @SerializedName("user_role_id") val userRoleId: String,
    @SerializedName("department") val department: String,
    @SerializedName("desigId") val desigId: String,
    @SerializedName("json_location") val jsonLocation: String,
    @SerializedName("designation") val designation: String,
    @SerializedName("dept_id") val deptId: String,
    @SerializedName("millname") val millName: String,
    @SerializedName("mill_lat") val millLat: String,
    @SerializedName("mill_long") val millLong: String,
    @SerializedName("dob") val dob: String,
    @SerializedName("doj") val doj: String,
    @SerializedName("estate_name") val estateName: String,
    @SerializedName("estate_id") val estateId: String,
    @SerializedName("division_name") val divisionName: String,
    @SerializedName("division_id") val divisionId: String,
    @SerializedName("block_name") val blockName: String,
    @SerializedName("block_id") val blockId: String,
    @SerializedName("password") val password: String,
    @SerializedName("welcome_msg") val welcomeMsg: String,
    @SerializedName("baseurl") val baseUrl: String
)


data class EmployeeRes(
    @SerializedName("user_id") val userId: String, // Primary key
    @SerializedName("emp_code") val empCode: String?,
    @SerializedName("emp_type") val empType: String,
    @SerializedName("emp_type_name") val empTypeName: String,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String?, // Nullable field
    @SerializedName("gender") val gender: String,
    @SerializedName("desig_id") val desigId: String,
    @SerializedName("designation") val designation: String,
    @SerializedName("dept_id") val deptId: String,
    @SerializedName("department") val department: String,
    @SerializedName("face_access_code") val faceCode: String,
    @SerializedName("estate_id") val estateId: String? = null,
    @SerializedName("division_id") val divisionId: String? = null,
    @SerializedName("block_id") val blockId: String? = null,
)

data class EmpGroupRes(
    @SerializedName("id") val id: String,
    @SerializedName("name") val groupName: String,
    @SerializedName("category") val category: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("estate_id") val estateId: String? = null,
    @SerializedName("division_id") val divisionId: String? = null,
    @SerializedName("block_id") val blockId: String? = null,

)

data class EstateRes(
    @SerializedName("id") val id: String,
    @SerializedName("estate_name") val estateName: String,
    @SerializedName("hectare") val hectare: String
)

data class DivisionRes(
    @SerializedName("id") val id: String,
    @SerializedName("estate_id") val estateId: String,
    @SerializedName("division_name") val divisionName: String,
    @SerializedName("hectare") val hectare: String,
    @SerializedName("estate_name") val estateName: String
)

data class BlockRes(
    @SerializedName("id") val id: String,
    @SerializedName("estate_id") val estateId: String,
    @SerializedName("hectare") val hectare: String,
    @SerializedName("division_id") val divisionId: String,
    @SerializedName("block_name") val blockName: String,
    @SerializedName("estate_name") val estateName: String,
    @SerializedName("division_name") val divisionName: String
)

data class ParcelRes(
    @SerializedName("id") val id: String,
    @SerializedName("estate_id") val estateId: String,
    @SerializedName("division_id") val divisionId: String,
    @SerializedName("block_id") val blockId: String,
    @SerializedName("hectare") val hectare: String,
    @SerializedName("block_name") val blockName: String,
    @SerializedName("division_name") val divisionName: String,
    @SerializedName("estate_name") val estateName: String,
    @SerializedName("parcel_name") val parcelName: String
)


data class TaskRes(
    @SerializedName("id") val id: String,
    @SerializedName("estate_id") val estateId: String,
    @SerializedName("division_id") val divisionId: String,
    @SerializedName("block_id") val blockId: String,
    @SerializedName("parcel_id") val parcelId: String,
    @SerializedName("block_name") val blockName: String,
    @SerializedName("division_name") val divisionName: String,
    @SerializedName("estate_name") val estateName: String,
    @SerializedName("parcel_name") val parcelName: String,
    @SerializedName("task") val task: String
)

data class ActivityRes(
    @SerializedName("id") val id: String,
    @SerializedName("activity_name") val activityName: String
)

data class FertiliserRes(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
)

data class PestDiseaseRes(
    @SerializedName("id") val id: String,
    @SerializedName("pest") val pest: String
)

data class VehicleRes(
    @SerializedName("id") val id: String,
    @SerializedName("truck_number") val truckNumber: String,
    @SerializedName("truck_code") val truckCode: String,
    @SerializedName("tare") val tare: String
)

//DriverTable

data class DriverRes(
    @SerializedName("user_id") val userId: String,
    @SerializedName("name") val name: String
)

data class ActivityCalenderRes(
    @SerializedName("id") val id: String,
    @SerializedName("estate_id") val estateId: String,
    @SerializedName("division_id") val divisionId: String,
    @SerializedName("block_id") val blockId: String,
    @SerializedName("parcel_id") val parcelId: String,
    @SerializedName("estate_name") val estateName: String,
    @SerializedName("division_name") val divisionName: String,
    @SerializedName("block_name") val blockName: String,
    @SerializedName("parcel_name") val parcelName: String,
    @SerializedName("start_date") val startDate: String?,
    @SerializedName("last_date") val lastDate: String,
    @SerializedName("frequency_range") val frequencyRange: String,
    @SerializedName("activity_id") val activityId: String,
    @SerializedName("activity_name") val activityName: String?
)

data class AttendanceData(
    @SerializedName("id") val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("site_id") val siteId: String,
    @SerializedName("estate_id") val estateId: String,
    @SerializedName("division_id") val divisionId: String,
    @SerializedName("block_id") val blockId: String,
    @SerializedName("in_time") val inTime: String,
    @SerializedName("in_lat") val inLat: String,
    @SerializedName("in_long") val inLong: String,
    @SerializedName("in_valid_punch") val inValidPunch: String,
    @SerializedName("out_time") val outTime: String?,
    @SerializedName("out_lat") val outLat: String?,
    @SerializedName("out_long") val outLong: String?,
    @SerializedName("out_valid_punch") val outValidPunch: String,
    @SerializedName("over_time_start") val overTimeStart: String?,
    @SerializedName("over_time_end") val overTimeEnd: String?,
    @SerializedName("day_status") val dayStatus: String?,
    @SerializedName("work_status") val workStatus: String,
    @SerializedName("insdt") val insdt: String,
    @SerializedName("moddt") val moddt: String,
    @SerializedName("is_deleted") val isDeleted: String,
    @SerializedName("name") val name: String,
    @SerializedName("emp_code") val empCode: String?
)


data class CoreActivityRes(
    @SerializedName("id") val id: String,
    @SerializedName("activity_name") val activityName: String,
    @SerializedName("operation_type") val operationType: String,
    @SerializedName("category_id") val categoryId: String,
    @SerializedName("category_name") val categoryName: String
)

data class EmpFaceRes(
    @SerializedName("user_id") val empUserId: String,
    @SerializedName("face_access_code") val empFaceData: List<FloatArray>
)


data class ChemicalRes(
    @SerializedName("id") val id: String,
    @SerializedName("chemical_name") val chemicalName: String,
    @SerializedName("dosage_ml") val dosageMl: String,
    @SerializedName("dosage_g") val dosageG: String
)

//Report
data class OnlineData(
    @SerializedName("db_id")
    val dbId: List<OnlineId>
)

data class OnlineId(
    @SerializedName("online_id")
    val onlineId: Int,
    @SerializedName("job_id")
    val jobId: String,
    @SerializedName("emp_user_id")
    val empUserId: String,
    @SerializedName("user_id")
    val userId: String
)

