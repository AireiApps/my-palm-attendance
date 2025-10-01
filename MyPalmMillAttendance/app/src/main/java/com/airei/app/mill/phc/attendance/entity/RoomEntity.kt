package com.airei.app.mill.phc.attendance.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.airei.app.plantation.phc.common.AppPreferences
import com.airei.app.plantation.phc.roomdb.DataTypeConverter
import com.google.gson.annotations.SerializedName


enum class ActivityStatus {
    NEW,
    PENDING,
    APPROVED_BY_ESTATE_MANAGER,
    REJECTED_BY_ESTATE_MANAGER,
    APPROVED_BY_DIVISION_MANAGER,
    REJECTED_BY_DIVISION_MANAGER,
    APPROVED_BY_ASSISTANT,
    COMPLETED
}

@Entity(tableName = "user_table")
data class LoginUserTable(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "user_role_id")
    val userRoleId: String,
    @ColumnInfo(name = "dept")
    val department: String,
    @ColumnInfo(name = "desig_id")
    val designationId: String,
    @ColumnInfo(name = "json_location")
    val jsonLocation: String,
    @ColumnInfo(name = "designation")
    val designation: String,
    @ColumnInfo(name = "dept_id")
    val deptId: String,
    @ColumnInfo(name = "mill_name")
    val millName: String,
    @ColumnInfo(name = "mill_lat")
    val millLat: String,
    @ColumnInfo(name = "mill_long")
    val millLong: String,
    @ColumnInfo(name = "dob")
    val dob: String,
    @ColumnInfo(name = "doj")
    val doj: String,
    @ColumnInfo(name = "estate_name")
    val estateName: String,
    @ColumnInfo(name = "estate_id")
    val estateId: String,
    @ColumnInfo(name = "division_name")
    val divisionName: String,
    @ColumnInfo(name = "division_id")
    val divisionId: String,
    @ColumnInfo(name = "block_name")
    val blockName: String,
    @ColumnInfo(name = "block_id")
    val blockId: String,
    @ColumnInfo(name = "password")
    var password: String,
    @ColumnInfo(name = "welcome_msg")
    val welcomeMsg: String,
    @ColumnInfo(name = "base_url")
    val baseUrl: String,
)

@Entity(tableName = "emp_bio_table")
@TypeConverters(DataTypeConverter::class)
data class EmployeeBioTable(
    @PrimaryKey
    @ColumnInfo(name = "emp_user_id")
    @SerializedName("user_id")
    val empUserId: String,
    @ColumnInfo("emp_face_data")
    @SerializedName("emp_face_data")
    var empFaceData: List<FloatArray> = listOf(),
    @ColumnInfo("upload_status")
    @SerializedName("upload_status")
    var uploadStatus: Boolean = false,
    @ColumnInfo(name = "in_date")
    @SerializedName("in_date")
    var inDate: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "mod_date")
    @SerializedName("mod_date")
    var modDate: Long = System.currentTimeMillis(),
)


@Entity(tableName = "employee_table")
data class EmployeeTable(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId: String, // Primary key
    @ColumnInfo(name = "emp_code")
    val empCode: String,
    @ColumnInfo(name = "emp_type")
    val empType: String,
    @ColumnInfo(name = "emp_type_id")
    val empTypeName: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "image")
    val image: String?, // Nullable field
    @ColumnInfo(name = "gender")
    val gender: String,
    @ColumnInfo(name = "desig_id")
    val desigId: String,
    @ColumnInfo(name = "designation")
    val designation: String,
    @ColumnInfo(name = "dept_id")
    val deptId: String,
    @ColumnInfo(name = "department")
    val department: String,
    @ColumnInfo(name = "face_code")
    val faceCode: String,
    @ColumnInfo(name = "estate_id") val estateId: String? = null,
    @ColumnInfo(name = "division_id") val divisionId: String? = null,
    @ColumnInfo(name = "block_id") val blockId: String? = null,
)

@Entity(tableName = "emp_group_table")
data class EmpGroupTable(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("l_id")
    val locId: Int = 0,
    @ColumnInfo("login_id")
    val loginId: String = "",
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
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    @SerializedName("local_id")
    val localId: Int = 0,
    @ColumnInfo(name = "emp_user_id")
    @SerializedName("emp_user_id")
    val empUserId: String = "",
    @ColumnInfo(name = "in_time")
    @SerializedName("in_time")
    val inTime: String = "0",
    @ColumnInfo(name = "out_time")
    @SerializedName("out_time")
    val outTime: String = "0",
    @ColumnInfo(name = "in_status")
    @SerializedName("in_status")
    val inStatus: String = "",
    @ColumnInfo(name = "out_status")
    @SerializedName("out_status")
    val outStatus: String = "",
    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    var userId: String = AppPreferences.loginId,
    @ColumnInfo(name = "site_id")
    @SerializedName("site_id")
    var siteId: String = "",
    @ColumnInfo(name = "estate_id")
    @SerializedName("estate_id")
    var estateId: String = "",
    @ColumnInfo(name = "division_id")
    @SerializedName("division_id")
    var divisionId: String = "",
    @ColumnInfo(name = "block_id")
    @SerializedName("block_id")
    var blockId: String = "",
    @ColumnInfo(name = "in_date")
    @SerializedName("in_date")
    var inDate: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "mod_date")
    @SerializedName("mod_date")
    var modDate: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "upload_status")
    @SerializedName("upload_status")
    var uploadStatus: Boolean = false,
    @ColumnInfo(name = "online_id")
    @SerializedName("online_id")
    var onlineId: String = "",
)

@Entity(tableName = "estate_table")
data class EstateTable(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("l_id")
    val locId: Int = 0,
    @ColumnInfo("login_id")
    val loginId: String = "",
    @ColumnInfo("id")
    val id: String,
    @ColumnInfo("estate_name")
    val estateName: String,
    @ColumnInfo("hectare")
    val hectare: String
)

@Entity(tableName = "division_table")
data class DivisionTable(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("l_id")
    val locId: Int = 0,
    @ColumnInfo("login_id")
    val loginId: String = "",
    @ColumnInfo("id")
    val id: String,
    @ColumnInfo("estate_id")
    val estateId: String,
    @ColumnInfo("division_name")
    val divisionName: String,
    @ColumnInfo("hectare")
    val hectare: String
)


@Entity(tableName = "block_table")
data class BlockTable(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("l_id")
    val locId: Int = 0,
    @ColumnInfo("login_id")
    val loginId: String = "",
    @ColumnInfo("id")
    val id: String,
    @ColumnInfo("estate_id")
    val estateId: String,
    @ColumnInfo("hectare")
    val hectare: String,
    @ColumnInfo("division_id")
    val divisionId: String,
    @ColumnInfo("block_name")
    val blockName: String,
)

@Entity(tableName = "parcel_table")
data class ParcelTable(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("l_id")
    val locId: Int = 0,
    @ColumnInfo("login_id")
    val loginId: String = "",
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "estate_id")
    val estateId: String,
    @ColumnInfo(name = "division_id")
    val divisionId: String,
    @ColumnInfo(name = "block_id")
    val blockId: String,
    @ColumnInfo(name = "hectare")
    val hectare: String,
    @ColumnInfo(name = "parcel_name")
    val parcelName: String
)

@Entity(tableName = "task_table")
data class TaskTable(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("l_id")
    val locId: Int = 0,
    @ColumnInfo("login_id")
    val loginId: String = "",
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "estate_id") val estateId: String,
    @ColumnInfo(name = "division_id") val divisionId: String,
    @ColumnInfo(name = "block_id") val blockId: String,
    @ColumnInfo(name = "parcel_id") val parcelId: String,
    @ColumnInfo(name = "task") val task: String
)

@Entity(tableName = "activity_table")
data class ActivityTable(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("l_id")val localId: Int = 0,
    @ColumnInfo(name = "login_id")
    val loginId: String = "",
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "activity_name")
    val activityName: String
)

@Entity(tableName = "other_activity_table")
data class OtherActivityTable(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("l_id")val localId: Int = 0,
    @ColumnInfo(name = "login_id")
    val loginId: String = "",
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "activity_name")
    val activityName: String
)

@Entity(tableName = "chemical_table")
data class ChemicalTable(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("l_id")
    val locId: Int = 0,
    @ColumnInfo("login_id")
    val loginId: String = "",
    @ColumnInfo("chemical_id")
    val chemicalId: String,
    @ColumnInfo("chemical_name")
    val chemicalName: String,
    @ColumnInfo("dosage_ml")
    val dosageMl: String,
    @ColumnInfo("dosage_g")
    val dosageG: String
)


// fertilizer
@Entity(tableName = "fertilizer_table")
data class FertilizerTable(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("l_id")
    val locId: Int = 0,
    @ColumnInfo("login_id")
    val loginId: String = "",
    @ColumnInfo("id")
    val id: String,
    @ColumnInfo("name")
    val name: String
)

//pest disease
@Entity(tableName = "pest_disease_table")
data class PestDiseaseTable(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("l_id")
    val locId: Int = 0,
    @ColumnInfo("login_id")
    val loginId: String = "",
    @ColumnInfo("id")
    val id: String,
    @ColumnInfo("pest")
    val pest: String
)

//VehicleRes
@Entity(tableName = "vehicle_table")
data class VehicleTable(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("l_id")
    val locId: Int = 0,
    @ColumnInfo("login_id")
    val loginId: String = "",
    @ColumnInfo("id")
    val id: String,
    @ColumnInfo("truck_number")
    val truckNumber: String,
    @ColumnInfo("truck_code")
    val truckCode: String,
    @ColumnInfo("tare")
    val tare: String
)

@Entity(tableName = "driver_table")
data class DriverTable(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("l_id")
    val locId: Int = 0,
    @ColumnInfo("login_id")
    val loginId: String = "",
    @ColumnInfo("name")
    val name: String,
    @ColumnInfo("user_id")
    val userId: String
)

@Entity(tableName = "core_activity_table")
data class CoreActivityTable(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("l_id")
    val locId: Int = 0,
    @ColumnInfo("id")val id: String,
    @ColumnInfo("activity_name") val activityName: String,
    @ColumnInfo("operation_type") val operationType: String,
    @ColumnInfo("category_id") val categoryId: String,
    @ColumnInfo("category_name") val categoryName: String
)


@Entity(tableName = "activity_calender_table")
data class ActivityCalenderTable(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("l_id")
    @SerializedName("l_id")
    val locId: Int = 0,

    @ColumnInfo("login_id")
    @SerializedName("login_id")
    val loginId: String = "",

    @ColumnInfo("id")
    @SerializedName("id")
    val id: String,

    @ColumnInfo("estate_id")
    @SerializedName("estate_id")
    val estateId: String,

    @ColumnInfo("division_id")
    @SerializedName("division_id")
    val divisionId: String,

    @ColumnInfo("block_id")
    @SerializedName("block_id")
    val blockId: String,

    @ColumnInfo("parcel_id")
    @SerializedName("parcel_id")
    val parcelId: String,

    @ColumnInfo("last_date")
    @SerializedName("last_date")
    val lastDate: String?,

    @ColumnInfo("start_date")
    @SerializedName("start_date")
    val startDate: String,

    @ColumnInfo("frequency_range")
    @SerializedName("frequency_range")
    val frequencyRange: String,

    @ColumnInfo("activity_id")
    @SerializedName("activity_id")
    val activityId: String,

    @ColumnInfo("activity_name")
    @SerializedName("activity_name")
    val activityName: String
)


// record
@Entity(tableName = "Job_ids_table")
data class JobIdList(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    @SerializedName("local_id")
    val localId: Int = 0,
    @ColumnInfo(name = "login_id")
    @SerializedName("login_id")
    var loginId: String = AppPreferences.loginId,
    @ColumnInfo(name = "harvest_id")
    @SerializedName("harvest_id")
    var harvest: String = "HRV-0",
    @ColumnInfo(name = "loose_fruit_id")
    @SerializedName("loose_fruit_id")
    var looseFruit: String = "LFC-0",
    @ColumnInfo(name = "quality_check_id")
    @SerializedName("quality_check_id")
    var qualityCheck: String = "DQC-0",
    @ColumnInfo(name = "other_activity_id")
    @SerializedName("other_activity_id")
    var otherActivity: String = "OA-0",
    @ColumnInfo(name = "crop_loss_id")
    @SerializedName("crop_loss_id")
    var cropLoss: String = "CLI-0",
    @ColumnInfo(name = "evacuation_id")
    @SerializedName("evacuation_id")
    var evacuation: String = "EVC-0",
    @ColumnInfo(name = "fertilizer_id")
    @SerializedName("fertilizer_id")
    var fertilizer: String = "FRT-0",
    @ColumnInfo(name = "pest_disease_id")
    @SerializedName("pest_disease_id")
    var pestDisease: String = "PND-0",
    @ColumnInfo("pruning_id")
    @SerializedName("pruning_id")
    var pruning: String = "PRU-0",
    @ColumnInfo("road_maintenance_id")
    @SerializedName("road_maintenance_id")
    var roadMaintenance: String = "RNM-0",
    @ColumnInfo("ring_spraying_id")
    @SerializedName("ring_spraying_id")
    var ringSpraying: String = "RGS-0",
    @ColumnInfo("selective_spraying_id")
    @SerializedName("selective_spraying_id")
    var selectiveSpraying: String = "SLS-0",
    @ColumnInfo("manual_circle_weeding_id")
    @SerializedName("manual_circle_weeding_id")
    var manualCircleWeeding: String = "MCW-0",
    @ColumnInfo("manual_ring_weeding_id")
    @SerializedName("manual_ring_weeding_id")
    var manualRingWeeding: String = "MRW-0",
    @ColumnInfo("mech_weeding_id")
    @SerializedName("mech_weeding_id")
    var mechanicalWeeding: String = "MNW-0",
    @ColumnInfo("census_id")
    @SerializedName("census_id")
    var census: String = "CSS-0",
    @ColumnInfo("chem_general_weeding_id")
    @SerializedName("chem_general_weeding_id")
    var chemicalGeneralWeeding: String = "CGW-0",
    @ColumnInfo("chem_ring_weeding_id")
    @SerializedName("chem_ring_weeding_id")
    var chemicalRingWeeding: String = "CRW-0",
    @ColumnInfo("last_update")
    @SerializedName("last_update")
    var lastUpdate: Long = System.currentTimeMillis(),
    @ColumnInfo("upload_status")
    @SerializedName("upload_status")
    var uploadStatus: Boolean = false,
)

@Entity(tableName = "pro_harvest_data")
@TypeConverters(DataTypeConverter::class)
data class HarvestData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    @SerializedName("local_id")
    val locId: Int = 0,
    @ColumnInfo(name = "job_id")
    @SerializedName("job_id")
    val jobId: String,
    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    val userId: String,
    @ColumnInfo(name = "date")
    @SerializedName("date")
    val date: String,
    @ColumnInfo(name = "estate_id")
    @SerializedName("estate_id")
    val estateId: String,
    @ColumnInfo(name = "division_id")
    @SerializedName("division_id")
    val divisionId: String,
    @ColumnInfo(name = "block_id")
    @SerializedName("block_id")
    val blockId: String,
    @ColumnInfo(name = "parcel_id")
    @SerializedName("parcel_id")
    val parcelId: String,
    @ColumnInfo(name = "record_list")
    @SerializedName("record_list")
    var recordList: ArrayList<HarvestRecord>? = null,
    @ColumnInfo(name = "status")
    @SerializedName("status")
    var status: ActivityStatus = ActivityStatus.NEW,
    @ColumnInfo(name = "upload_status")
    @SerializedName("upload_status")
    var uploadStatus: Boolean = false,
    // Approve Flow
    @ColumnInfo(name = "em_approve")
    @SerializedName("em_approve")
    var approveEM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_approve")
    @SerializedName("dm_approve")
    var approveDM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_id")
    @SerializedName("dm_id")
    var dmId: String = "",

    @ColumnInfo(name = "assistant_approve")
    @SerializedName("assistant_approve")
    var approveAP: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "assistant_id")
    @SerializedName("assistant_id")
    var apId: String = "",
    @ColumnInfo(name = "em_id")
    @SerializedName("em_id")
    var emId: String = "",
)

data class HarvestRecord(
    @SerializedName("record_id")
    val recordId: Int,
    @SerializedName("workers_type")
    val workerType: String,
    @SerializedName("group_id")
    val groupId: String,
    @SerializedName("member_ids")
    val membersID: String,
    @SerializedName("task_id")
    val taskId: String,
    @SerializedName("total_ripe_bunches")
    val totalRipeBunches: String,
    @SerializedName("total_unripe_bunches")
    val totalUnripeBunches: String,
    @SerializedName("total_parthonagrape_bunches")
    val totalParthonagarpe: String,
    @SerializedName("total_rotten_bunches")
    val totalRottenBunches: String,
    @SerializedName("total_long_stalk_bunches")
    val totalLongStalkBunches: String,
    @SerializedName("remarks")
    val penalty: String
)


@Entity(tableName = "pro_loose_fruit_data")
@TypeConverters(DataTypeConverter::class)
data class LooseFruitData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    @SerializedName("local_id")
    val locId: Int = 0,
    @ColumnInfo(name = "job_id")
    @SerializedName("job_id")
    val jobId: String,
    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    val userId: String,
    @ColumnInfo(name = "date")
    @SerializedName("date")
    val date: String,
    @ColumnInfo(name = "estate_id")
    @SerializedName("estate_id")
    val estateId: String,
    @ColumnInfo(name = "division_id")
    @SerializedName("division_id")
    val divisionId: String,
    @ColumnInfo(name = "block_id")
    @SerializedName("block_id")
    val blockId: String,
    @ColumnInfo(name = "parcel_id")
    @SerializedName("parcel_id")
    val parcelId: String,
    @ColumnInfo(name = "record_list")
    @SerializedName("record_list")
    var recordList: ArrayList<LooseFruitRecord> = arrayListOf(),
    @ColumnInfo(name = "status")
    @SerializedName("status")
    var status: ActivityStatus = ActivityStatus.NEW,
    // Approve Flow
    @ColumnInfo(name = "em_approve")
    @SerializedName("em_approve")
    var approveEM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_approve")
    @SerializedName("dm_approve")
    var approveDM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_id")
    @SerializedName("dm_id")
    var dmId: String = "",

    @ColumnInfo(name = "assistant_approve")
    @SerializedName("assistant_approve")
    var approveAP: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "assistant_id")
    @SerializedName("assistant_id")
    var apId: String = "",
    @ColumnInfo(name = "em_id")
    @SerializedName("em_id")
    var emId: String = "",
    @ColumnInfo(name = "upload_status")
    @SerializedName("upload_status")
    var uploadStatus: Boolean = false,
)

data class LooseFruitRecord(
    @SerializedName("record_id")
    val recordId: Int = 0,
    @SerializedName("workers_type")
    val workerType: String,
    @SerializedName("group_id")
    val groupId: String,
    @SerializedName("member_ids")
    val membersID: String,
    @SerializedName("task_id")
    val taskId: String,
    @SerializedName("total_weight")
    val totalWeight: Float,
    @SerializedName("remarks")
    val remarks: String
)


//Quality Check
@Entity(tableName = "pro_quality_check_data")
data class QualityCheckData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    @SerializedName("local_id")
    var locId: Int = 0,
    @ColumnInfo(name = "job_id")
    @SerializedName("job_id")
    val jobId: String,
    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    val userId: String,
    @ColumnInfo(name = "date")
    @SerializedName("date")
    val date: String,
    @ColumnInfo(name = "estate_id")
    @SerializedName("estate_id")
    val estateId: String,
    @ColumnInfo(name = "division_id")
    @SerializedName("division_id")
    val divisionId: String,
    @ColumnInfo(name = "block_id")
    @SerializedName("block_id")
    val blockId: String,
    @ColumnInfo(name = "parcel_id")
    @SerializedName("parcel_id")
    val parcelId: String,
    @ColumnInfo(name = "total_ripe")
    @SerializedName("total_ripe_bunches")
    val totalRipe: String,
    @ColumnInfo(name = "total_unripe")
    @SerializedName("total_unripe_bunches")
    val totalUnripe: String,
    @ColumnInfo(name = "total_rotten")
    @SerializedName("total_rotten_bunches")
    val totalRotten: String,
    @ColumnInfo(name = "total_parthonagrape")
    @SerializedName("total_parthonagrape_bunches")
    val totalParthonagrape: String,
    @ColumnInfo(name = "total_long_stalk")
    @SerializedName("total_long_stalk_fruits")
    val totalLongStalk: String,
    @ColumnInfo(name = "remarks")
    @SerializedName("remarks")
    val remarks: String,
    @ColumnInfo(name = "status")
    @SerializedName("status")
    var status: ActivityStatus = ActivityStatus.NEW,
// Approve Flow
    @ColumnInfo(name = "em_approve")
    @SerializedName("em_approve")
    var approveEM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_approve")
    @SerializedName("dm_approve")
    var approveDM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_id")
    @SerializedName("dm_id")
    var dmId: String = "",

    @ColumnInfo(name = "assistant_approve")
    @SerializedName("assistant_approve")
    var approveAP: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "assistant_id")
    @SerializedName("assistant_id")
    var apId: String = "",
    @ColumnInfo(name = "em_id")
    @SerializedName("em_id")
    var emId: String = "",
    @ColumnInfo(name = "upload_status")
    @SerializedName("upload_status")
    var uploadStatus: Boolean = false,
)

//Evacuation
@Entity(tableName = "pro_evacuation_data")
data class EvacuationData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    @SerializedName("local_id")
    val locId: Int = 0,
    @ColumnInfo(name = "job_id")
    @SerializedName("job_id")
    val jobId: String,
    @ColumnInfo(name = "harvest_job_id")
    @SerializedName("ffb_harvest_job_id")
    val hJobId: String,
    @ColumnInfo(name = "lf_collect_job_id")
    @SerializedName("lf_collect_job_id")
    val lfcJobId: String,
    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    val userId: String,
    @ColumnInfo(name = "date")
    @SerializedName("date")
    val date: String,
    @ColumnInfo(name = "estate_id")
    @SerializedName("estate_id")
    val estateId: String,
    @ColumnInfo(name = "division_id")
    @SerializedName("division_id")
    val divisionId: String,
    @ColumnInfo(name = "block_id")
    @SerializedName("block_id")
    val blockId: String,
    @ColumnInfo(name = "parcel_id")
    @SerializedName("parcel_id")
    val parcelId: String,
    @ColumnInfo(name = "loader_id")
    @SerializedName("loader_id")
    val loaderId: String,
    @ColumnInfo(name = "driver_id")
    @SerializedName("driver_id")
    val driverId: String,
    @ColumnInfo(name = "lorry_no")
    @SerializedName("lorry_no")
    val vehicleNo: String,
    @ColumnInfo(name = "total_ffb")
    @SerializedName("total_ffb")
    val totalFFB: String,
    @ColumnInfo(name = "total_estimated_wt")
    @SerializedName("total_estimated_weight")
    val totalEstimatedWt: String,
    @ColumnInfo(name = "total_lf_wt")
    @SerializedName("total_loose_fruit_weight")
    val totalLfWt: String,
    @ColumnInfo(name = "remarks")
    @SerializedName("remarks")
    val remarks: String,
    @ColumnInfo(name = "status")
    @SerializedName("status")
    var status: ActivityStatus = ActivityStatus.NEW,
    // Approve Flow
    @ColumnInfo(name = "em_approve")
    @SerializedName("em_approve")
    var approveEM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_approve")
    @SerializedName("dm_approve")
    var approveDM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_id")
    @SerializedName("dm_id")
    var dmId: String = "",

    @ColumnInfo(name = "assistant_approve")
    @SerializedName("assistant_approve")
    var approveAP: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "assistant_id")
    @SerializedName("assistant_id")
    var apId: String = "",
    @ColumnInfo(name = "em_id")
    @SerializedName("em_id")
    var emId: String = "",
    @ColumnInfo(name = "upload_status")
    @SerializedName("upload_status")
    var uploadStatus: Boolean = false,
)


//Crop Loss
@Entity(tableName = "pro_crop_loss_data")
@TypeConverters(DataTypeConverter::class)
data class CropLossData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    @SerializedName("local_id")
    val locId: Int = 0,
    @ColumnInfo(name = "job_id")
    @SerializedName("job_id")
    val jobId: String,
    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    val userId: String,
    @ColumnInfo(name = "date")
    @SerializedName("date")
    val date: String,
    @ColumnInfo(name = "harvest_data")
    @SerializedName("harvested_date")
    val harvestData: String,
    @ColumnInfo(name = "estate_id")
    @SerializedName("estate_id")
    val estateId: String,
    @ColumnInfo(name = "division_id")
    @SerializedName("division_id")
    val divisionId: String,
    @ColumnInfo(name = "block_id")
    @SerializedName("block_id")
    val blockId: String,
    @ColumnInfo(name = "parcel_id")
    @SerializedName("parcel_id")
    val parcelId: String,
    @ColumnInfo(name = "record_list")
    @SerializedName("record_list")
    var recordList: ArrayList<CropLossRecord> = arrayListOf(),
    @ColumnInfo(name = "status")
    @SerializedName("status")
    var status: ActivityStatus = ActivityStatus.NEW,
    // Approve Flow
    @ColumnInfo(name = "em_approve")
    @SerializedName("em_approve")
    var approveEM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_approve")
    @SerializedName("dm_approve")
    var approveDM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_id")
    @SerializedName("dm_id")
    var dmId: String = "",

    @ColumnInfo(name = "assistant_approve")
    @SerializedName("assistant_approve")
    var approveAP: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "assistant_id")
    @SerializedName("assistant_id")
    var apId: String = "",
    @ColumnInfo(name = "em_id")
    @SerializedName("em_id")
    var emId: String = "",
    @ColumnInfo(name = "upload_status")
    @SerializedName("upload_status")
    var uploadStatus: Boolean = false,
)

data class CropLossRecord(
    @SerializedName("record_id")
    val recordId: Int = 0,
    @SerializedName("task_id")
    val taskId: String,
    @SerializedName("total_palms_surveyed")
    val totalPalms: String,
    @SerializedName("hectare_control")
    val hentare: String,
    @SerializedName("total_harvested_bunches")
    val totalHarvestBunch: String,
    @SerializedName("total_unharvested_bunches")
    val totalUnHarvestBunch: String,
    @SerializedName("total_left_harvested_bunches")
    val totalLeftBunch: String,
    @SerializedName("total_rotten_bunches")
    val totalRottenBunch: String,
    @SerializedName("total_uncollected_loose_fruits")
    val totalUncollectLF: String,
    @SerializedName("total_front_stacking")
    val totalFrontStack: String,
    @SerializedName("total_poor_front_bud")
    val totalPoorFront: String,
    @SerializedName("remarks")
    val remarks: String
)

//Other Activities
@Entity(tableName = "pro_other_activity_data")
data class OtherActivityData(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "local_id") @SerializedName("local_id") val locId: Int = 0,
    @ColumnInfo(name = "job_id") @SerializedName("job_id") val jobId: String,
    @ColumnInfo(name = "date") @SerializedName("date") val date: String,
    @ColumnInfo(name = "user_id") @SerializedName("user_id") val userId: String,
    @ColumnInfo(name = "estate_id") @SerializedName("estate_id") val estateId: String,
    @ColumnInfo(name = "division_id") @SerializedName("division_id") val divisionId: String,
    @ColumnInfo(name = "block_id") @SerializedName("block_id") val blockId: String,
    @ColumnInfo(name = "parcel_id") @SerializedName("parcel_id") val parcelId: String,
    @ColumnInfo(name = "worker_id") @SerializedName("worker_id") val workerId: String,
    @ColumnInfo(name = "workers_type") @SerializedName("workers_type") val workerType: String = "",
    @ColumnInfo(name = "start_time") @SerializedName("start_time") val startTime: String,
    @ColumnInfo(name = "end_time") @SerializedName("end_time") val endTime: String,
    @ColumnInfo(name = "activity_id") @SerializedName("activity_id") val activityId: String,
    @ColumnInfo(name = "remarks") @SerializedName("remarks") val remarks: String,
    @ColumnInfo(name = "status") @SerializedName("status") var status: ActivityStatus = ActivityStatus.NEW,
    // Approve Flow
    @ColumnInfo(name = "em_approve")
    @SerializedName("em_approve")
    var approveEM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_approve")
    @SerializedName("dm_approve")
    var approveDM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_id")
    @SerializedName("dm_id")
    var dmId: String = "",

    @ColumnInfo(name = "assistant_approve")
    @SerializedName("assistant_approve")
    var approveAP: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "assistant_id")
    @SerializedName("assistant_id")
    var apId: String = "",
    @ColumnInfo(name = "em_id")
    @SerializedName("em_id")
    var emId: String = "",
    @ColumnInfo(name = "upload_status")
    @SerializedName("upload_status") var uploadStatus: Boolean = false,
)


// Maintenance

@Entity(tableName = "mnt_chemical_weeding_data")
@TypeConverters(DataTypeConverter::class)
data class ChemicalWeedingData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    var locId: Int = 0,
    @ColumnInfo(name = "job_id")
    @SerializedName("job_id")
    val jobId: String,
    @ColumnInfo(name = "weeding_type")
    @SerializedName("type")
    val weedingType: String = "", //type(1-general,2-Ring)
    @ColumnInfo(name = "date")
    @SerializedName("date")
    val date: String = "",
    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    val userId: String,
    @ColumnInfo(name = "estate_id")
    @SerializedName("estate_id")
    val estateId: String,
    @ColumnInfo(name = "division_id")
    @SerializedName("division_id")
    val divisionId: String,
    @ColumnInfo(name = "block_id")
    @SerializedName("block_id")
    val blockId: String,
    @ColumnInfo(name = "parcel_id")
    @SerializedName("parcel_id")
    val parcelId: String,
    @ColumnInfo("day_records")
    @SerializedName("record_list")
    val dayRecords: ArrayList<ChemWeedDailyRecord> = arrayListOf(),
    @ColumnInfo(name = "status")
    var status: ActivityStatus = ActivityStatus.NEW,
// Approve Flow
    @ColumnInfo(name = "em_approve")
    @SerializedName("em_approve")
    var approveEM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_approve")
    @SerializedName("dm_approve")
    var approveDM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_id")
    @SerializedName("dm_id")
    var dmId: String = "",
    @ColumnInfo(name = "assistant_approve")
    @SerializedName("assistant_approve")
    var approveAP: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "assistant_id")
    @SerializedName("assistant_id")
    var apId: String = "",
    @ColumnInfo(name = "em_id")
    @SerializedName("em_id")
    var emId: String = "",
    @ColumnInfo(name = "upload_status")
    @SerializedName("upload_status")
    var uploadStatus: Boolean = false,
)

@Entity(tableName = "mnt_ring_weeding_data")
@TypeConverters(DataTypeConverter::class)
data class RingWeedingData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    @SerializedName("local_id")
    val locId: Int = 0,
    @ColumnInfo(name = "job_id")
    @SerializedName("job_id")
    val jobId: String,
    @ColumnInfo(name = "date")
    @SerializedName("date")
    val date: String,
    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    val userId: String,
    @ColumnInfo(name = "estate_id")
    @SerializedName("estate_id")
    val estateId: String,
    @ColumnInfo(name = "division_id")
    @SerializedName("division_id")
    val divisionId: String,
    @ColumnInfo(name = "block_id")
    @SerializedName("block_id")
    val blockId: String,
    @ColumnInfo(name = "parcel_id")
    @SerializedName("parcel_id")
    val parcelId: String,
    @ColumnInfo("day_records")
    @SerializedName("record_list")
    val dayRecords: ArrayList<WeedDailyRecord> = arrayListOf(),
    @ColumnInfo(name = "status")
    var status: ActivityStatus = ActivityStatus.NEW,
// Approve Flow
    @ColumnInfo(name = "em_approve")
    @SerializedName("em_approve")
    var approveEM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_approve")
    @SerializedName("dm_approve")
    var approveDM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_id")
    @SerializedName("dm_id")
    var dmId: String = "",

    @ColumnInfo(name = "assistant_approve")
    @SerializedName("assistant_approve")
    var approveAP: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "assistant_id")
    @SerializedName("assistant_id")
    var apId: String = "",
    @ColumnInfo(name = "em_id")
    @SerializedName("em_id")
    var emId: String = "",
    @ColumnInfo(name = "upload_status")
    @SerializedName("upload_status")
    var uploadStatus: Boolean = false,
)

data class WeedDailyRecord(
    @SerializedName("record_id")
    val recordId: Int,
    @SerializedName("record_date")
    val recordDate: String,
    @SerializedName("workers_type")
    val workerType: String,
    @SerializedName("group_id")
    val groupId: String,
    @SerializedName("member_ids")
    val membersID: String,
    @SerializedName("total_palm_completed")
    val totalPalms: String,
    @SerializedName("remarks")
    val remarks: String
)

data class ChemWeedDailyRecord(
    @SerializedName("record_id")
    val recordId: Int,
    @SerializedName("record_date")
    val recordDate: String,
    @SerializedName("workers_type")
    val workerType: String,
    @SerializedName("group_id")
    val groupId: String,
    @SerializedName("chemical_id")
    val chemicalIdType: String,
    @SerializedName("member_ids")
    val membersID: String,
    @SerializedName("total_palm_completed")
    val totalPalms: String,
    @SerializedName("remarks")
    val remarks: String
)

@Entity(tableName = "mnt_circle_weeding_data")
@TypeConverters(DataTypeConverter::class)
data class CircleWeedingData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    @SerializedName("local_id")
    val locId: Int = 0,
    @ColumnInfo(name = "job_id")
    @SerializedName("job_id")
    val jobId: String,
    @ColumnInfo(name = "date")
    @SerializedName("date")
    val date: String,
    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    val userId: String,
    @ColumnInfo(name = "estate_id")
    @SerializedName("estate_id")
    val estateId: String,
    @ColumnInfo(name = "division_id")
    @SerializedName("division_id")
    val divisionId: String,
    @ColumnInfo(name = "block_id")
    @SerializedName("block_id")
    val blockId: String,
    @ColumnInfo(name = "parcel_id")
    @SerializedName("parcel_id")
    val parcelId: String,
    @ColumnInfo("day_records")
    @SerializedName("record_list")
    val dayRecords: ArrayList<WeedDailyRecord> = arrayListOf(),
    @ColumnInfo(name = "status")
    @SerializedName("status")
    var status: ActivityStatus = ActivityStatus.NEW,
// Approve Flow
    @ColumnInfo(name = "em_approve")
    @SerializedName("em_approve")
    var approveEM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_approve")
    @SerializedName("dm_approve")
    var approveDM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_id")
    @SerializedName("dm_id")
    var dmId: String = "",

    @ColumnInfo(name = "assistant_approve")
    @SerializedName("assistant_approve")
    var approveAP: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "assistant_id")
    @SerializedName("assistant_id")
    var apId: String = "",
    @ColumnInfo(name = "em_id")
    @SerializedName("em_id")
    var emId: String = "",
    @ColumnInfo(name = "upload_status")
    @SerializedName("upload_status")
    var uploadStatus: Boolean = false,
)

@Entity(tableName = "mnt_pruning_data")
data class PruningData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    val locId: Int = 0,
    @ColumnInfo(name = "job_id")
    @SerializedName("job_id")
    val jobId: String,
    @ColumnInfo(name = "date")
    @SerializedName("date")
    val date: String,
    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    val userId: String,
    @ColumnInfo(name = "estate_id")
    @SerializedName("estate_id")
    val estateId: String,
    @ColumnInfo(name = "division_id")
    @SerializedName("division_id")
    val divisionId: String,
    @ColumnInfo(name = "block_id")
    @SerializedName("block_id")
    val blockId: String,
    @ColumnInfo(name = "parcel_id")
    @SerializedName("parcel_id")
    val parcelId: String,
    @ColumnInfo(name = "worker_type")
    @SerializedName("workers_type")
    val workerType: String = "",
    @ColumnInfo(name = "group_id")
    @SerializedName("group_id")
    val groupId: String,
    @ColumnInfo(name = "members_id")
    @SerializedName("member_ids")
    val membersId: String,
    @ColumnInfo(name = "total_palm_completed")
    @SerializedName("total_palm_completed")
    val totalPalms: String,
    @ColumnInfo(name = "remarks")
    val remarks: String,
    @ColumnInfo(name = "status")
    @SerializedName("status")
    var status: ActivityStatus = ActivityStatus.NEW,
// Approve Flow
    @ColumnInfo(name = "em_approve")
    @SerializedName("em_approve")
    var approveEM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_approve")
    @SerializedName("dm_approve")
    var approveDM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_id")
    @SerializedName("dm_id")
    var dmId: String = "",

    @ColumnInfo(name = "assistant_approve")
    @SerializedName("assistant_approve")
    var approveAP: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "assistant_id")
    @SerializedName("assistant_id")
    var apId: String = "",
    @ColumnInfo(name = "em_id")
    @SerializedName("em_id")
    var emId: String = "",
    @ColumnInfo(name = "upload_status")
    @SerializedName("upload_status")
    var uploadStatus: Boolean = false,
)

@Entity(tableName = "mnt_fertilizing_data")
data class FertilizingData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    val locId: Int = 0,
    @ColumnInfo(name = "job_id")
    @SerializedName("job_id")
    val jobId: String,
    @ColumnInfo(name = "date")
    @SerializedName("date")
    val date: String,
    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    val userId: String,
    @ColumnInfo(name = "estate_id")
    @SerializedName("estate_id")
    val estateId: String,
    @ColumnInfo(name = "division_id")
    @SerializedName("division_id")
    val divisionId: String,
    @ColumnInfo(name = "block_id")
    @SerializedName("block_id")
    val blockId: String,
    @ColumnInfo(name = "parcel_id")
    @SerializedName("parcel_id")
    val parcelId: String,
    @ColumnInfo(name = "workers_type")
    @SerializedName("workers_type")
    val workerType: String = "",
    @ColumnInfo(name = "member_ids")
    @SerializedName("member_ids")
    val membersId: String,
    @ColumnInfo(name = "group_id")
    @SerializedName("group_id")
    val groupId: String,
    @ColumnInfo(name = "total_palm_completed")
    @SerializedName("total_palm_completed")
    val totalPalms: String,
    @ColumnInfo(name = "type_of_fertiizer")
    @SerializedName("type_of_fertiizer")
    val typeOfFertiizer: String,
    @ColumnInfo(name = "amount_per_palm")
    @SerializedName("amount_per_palm")
    val amountPerPalm: String,
    @ColumnInfo(name = "remarks")
    @SerializedName("remarks")
    val remarks: String,
    @ColumnInfo(name = "status")
    @SerializedName("status")
    var status: ActivityStatus = ActivityStatus.NEW,
// Approve Flow
    @ColumnInfo(name = "em_approve")
    @SerializedName("em_approve")
    var approveEM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_approve")
    @SerializedName("dm_approve")
    var approveDM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_id")
    @SerializedName("dm_id")
    var dmId: String = "",

    @ColumnInfo(name = "assistant_approve")
    @SerializedName("assistant_approve")
    var approveAP: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "assistant_id")
    @SerializedName("assistant_id")
    var apId: String = "",
    @ColumnInfo(name = "em_id")
    @SerializedName("em_id")
    var emId: String = "",
    @ColumnInfo(name = "upload_status")
    @SerializedName("upload_status")
    var uploadStatus: Boolean = false,
)

@Entity(tableName = "mnt_road_maintenance_data")
data class RoadMaintenanceData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    val locId: Int = 0,
    @ColumnInfo(name = "job_id")
    @SerializedName("job_id")
    val jobId: String,
    @ColumnInfo(name = "date")
    @SerializedName("start_date")
    val startDate: String,
    @ColumnInfo(name = "end_date")
    @SerializedName("end_date")
    val endDate: String,
    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    val userId: String,
    @ColumnInfo(name = "estate_id")
    @SerializedName("estate_id")
    val estateId: String,
    @ColumnInfo(name = "division_id")
    @SerializedName("division_id")
    val divisionId: String,
    @ColumnInfo(name = "block_id")
    @SerializedName("block_id")
    val blockId: String,
    @ColumnInfo(name = "parcel_id")
    @SerializedName("parcel_id")
    val parcelId: String,
    @ColumnInfo(name = "group_id")
    @SerializedName("group_id")
    val groupId: String,
    @ColumnInfo(name = "member_ids")
    @SerializedName("member_ids")
    val membersId: String,
    @ColumnInfo(name = "workers_type")
    @SerializedName("workers_type")
    val workerType: String = "",
    @ColumnInfo(name = "type_of_activity")
    @SerializedName("type_of_activity")
    val typeOfActivity: String,
    @ColumnInfo(name = "remarks")
    @SerializedName("remarks")
    val remarks: String,
    @ColumnInfo(name = "status")
    @SerializedName("status")
    var status: ActivityStatus = ActivityStatus.NEW,
// Approve Flow
    @ColumnInfo(name = "em_approve")
    @SerializedName("em_approve")
    var approveEM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_approve")
    @SerializedName("dm_approve")
    var approveDM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_id")
    @SerializedName("dm_id")
    var dmId: String = "",

    @ColumnInfo(name = "assistant_approve")
    @SerializedName("assistant_approve")
    var approveAP: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "assistant_id")
    @SerializedName("assistant_id")
    var apId: String = "",
    @ColumnInfo(name = "em_id")
    @SerializedName("em_id")
    var emId: String = "",
    @ColumnInfo(name = "upload_status")
    @SerializedName("upload_status")
    var uploadStatus: Boolean = false,
)

@Entity(tableName = "mnt_pest_diseases_data")
data class PestDiseasesData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    val locId: Int = 0,
    @ColumnInfo(name = "job_id")
    @SerializedName("job_id")
    val jobId: String,
    @ColumnInfo(name = "date")
    @SerializedName("date")
    val date: String,
    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    val userId: String,
    @ColumnInfo(name = "estate_id")
    @SerializedName("estate_id")
    val estateId: String,
    @ColumnInfo(name = "division_id")
    @SerializedName("division_id")
    val divisionId: String,
    @ColumnInfo(name = "block_id")
    @SerializedName("block_id")
    val blockId: String,
    @ColumnInfo(name = "parcel_id")
    @SerializedName("parcel_id")
    val parcelId: String,
    @ColumnInfo(name = "group_id")
    @SerializedName("group_id")
    val groupId: String,
    @ColumnInfo(name = "member_ids")
    @SerializedName("member_ids")
    val membersId: String,
    @ColumnInfo(name = "workers_type")
    @SerializedName("workers_type")
    val workersType: String = "2",
    @ColumnInfo(name = "type_of_pest")
    @SerializedName("type_of_pest_disease")
    val typeOfPestDisease: String,
    @ColumnInfo(name = "total_palm_affect")
    @SerializedName("total_palm_affected")
    val totalPalmAffected: String,
    @ColumnInfo(name = "remarks")
    @SerializedName("remarks")
    val remarks: String,
    @ColumnInfo(name = "status")
    @SerializedName("status")
    var status: ActivityStatus = ActivityStatus.NEW,
// Approve Flow
    @ColumnInfo(name = "em_approve")
    @SerializedName("em_approve")
    var approveEM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_approve")
    @SerializedName("dm_approve")
    var approveDM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_id")
    @SerializedName("dm_id")
    var dmId: String = "",

    @ColumnInfo(name = "assistant_approve")
    @SerializedName("assistant_approve")
    var approveAP: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "assistant_id")
    @SerializedName("assistant_id")
    var apId: String = "",
    @ColumnInfo(name = "em_id")
    @SerializedName("em_id")
    var emId: String = "",
    @ColumnInfo(name = "upload_status")
    @SerializedName("upload_status")
    var uploadStatus: Boolean = false,
)

@Entity(tableName = "mnt_ring_spraying_data")
data class RingSprayingData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    val locId: Int = 0,
    @ColumnInfo(name = "job_id")
    @SerializedName("job_id")
    val jobId: String,
    @ColumnInfo(name = "date")
    @SerializedName("date")
    val date: String,
    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    val userId: String,
    @ColumnInfo(name = "estate_id")
    @SerializedName("estate_id")
    val estateId: String,
    @ColumnInfo(name = "division_id")
    @SerializedName("division_id")
    val divisionId: String,
    @ColumnInfo(name = "block_id")
    @SerializedName("block_id")
    val blockId: String,
    @ColumnInfo(name = "parcel_id")
    @SerializedName("parcel_id")
    val parcelId: String,
    @ColumnInfo(name = "group_id")
    @SerializedName("group_id")
    val groupId: String,
    @ColumnInfo(name = "member_ids")
    @SerializedName("member_ids")
    val membersId: String,
    @ColumnInfo(name = "workers_type")
    @SerializedName("workers_type")
    val workerType: String = "",
    @ColumnInfo(name = "total_palm_completed")
    @SerializedName("total_palm_completed")
    val totalPalms: String,
    @ColumnInfo(name = "remarks")
    @SerializedName("remarks")
    val remarks: String,
    @ColumnInfo(name = "status")
    @SerializedName("status")
    var status: ActivityStatus = ActivityStatus.NEW,
// Approve Flow
    @ColumnInfo(name = "em_approve")
    @SerializedName("em_approve")
    var approveEM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_approve")
    @SerializedName("dm_approve")
    var approveDM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_id")
    @SerializedName("dm_id")
    var dmId: String = "",

    @ColumnInfo(name = "assistant_approve")
    @SerializedName("assistant_approve")
    var approveAP: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "assistant_id")
    @SerializedName("assistant_id")
    var apId: String = "",
    @ColumnInfo(name = "em_id")
    @SerializedName("em_id")
    var emId: String = "",
    @ColumnInfo(name = "upload_status")
    @SerializedName("upload_status")
    var uploadStatus: Boolean = false,
)

@Entity(tableName = "mnt_selective_spraying_data")
data class SelectiveSprayingData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    val locId: Int = 0,
    @ColumnInfo(name = "job_id")
    @SerializedName("job_id")
    val jobId: String,
    @ColumnInfo(name = "date")
    @SerializedName("date")
    val date: String,
    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    val userId: String,
    @ColumnInfo(name = "estate_id")
    @SerializedName("estate_id")
    val estateId: String,
    @ColumnInfo(name = "division_id")
    @SerializedName("division_id")
    val divisionId: String,
    @ColumnInfo(name = "block_id")
    @SerializedName("block_id")
    val blockId: String,
    @ColumnInfo(name = "parcel_id")
    @SerializedName("parcel_id")
    val parcelId: String,
    @ColumnInfo(name = "group_id")
    @SerializedName("group_id")
    val groupId: String,
    @ColumnInfo(name = "member_ids")
    @SerializedName("member_ids")
    val membersId: String,
    @ColumnInfo(name = "workers_type")
    @SerializedName("workers_type")
    val workerType: String = "",
    @ColumnInfo(name = "total_palm_completed")
    @SerializedName("total_palm_completed")
    val totalPalms: String,
    @ColumnInfo(name = "remarks")
    @SerializedName("remarks")
    val remarks: String,
    @ColumnInfo(name = "status")
    @SerializedName("status")
    var status: ActivityStatus = ActivityStatus.NEW,
// Approve Flow
    @ColumnInfo(name = "em_approve")
    @SerializedName("em_approve")
    var approveEM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_approve")
    @SerializedName("dm_approve")
    var approveDM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_id")
    @SerializedName("dm_id")
    var dmId: String = "",
    @ColumnInfo(name = "assistant_approve")
    @SerializedName("assistant_approve")
    var approveAP: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "assistant_id")
    @SerializedName("assistant_id")
    var apId: String = "",
    @ColumnInfo(name = "em_id")
    @SerializedName("em_id")
    var emId: String = "",
    @ColumnInfo(name = "upload_status")
    @SerializedName("upload_status")
    var uploadStatus: Boolean = false,
)

@Entity(tableName = "mnt_census_data")
data class CensusData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    var locId: Int = 0,
    @ColumnInfo(name = "job_id")
    @SerializedName("job_id")
    val jobId: String,
    @ColumnInfo(name = "date")
    @SerializedName("date")
    val date: String,
    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    val userId: String,
    @ColumnInfo(name = "estate_id")
    @SerializedName("estate_id")
    val estateId: String,
    @ColumnInfo(name = "division_id")
    @SerializedName("division_id")
    val divisionId: String,
    @ColumnInfo(name = "block_id")
    @SerializedName("block_id")
    val blockId: String,
    @ColumnInfo(name = "parcel_id")
    @SerializedName("parcel_id")
    val parcelId: String,
    @ColumnInfo(name = "group_id")
    @SerializedName("group_id")
    val groupId: String,
    @ColumnInfo(name = "member_ids")
    @SerializedName("member_ids")
    val membersId: String,
    @ColumnInfo(name = "workers_type")
    @SerializedName("workers_type")
    val workerType: String = "",
    @ColumnInfo(name = "type_of_pest_disease")
    @SerializedName("type_of_pest_disease")
    val typePestDisease: String = "0",
    @ColumnInfo(name = "type_of_black_bunch")
    @SerializedName("type_of_black_bunch")
    val typeBlackBunch: String = "0",
    @ColumnInfo(name = "type_of_palm_counting")
    @SerializedName("type_of_palm_counting")
    val typePalmCounting: String = "0",
    @ColumnInfo(name = "total_palm_counting")
    @SerializedName("total_palm_counting")
    val totalPalmCounting: String,
    @ColumnInfo(name = "total_black_bunch")
    @SerializedName("total_black_bunch")
    val totalBlackBunch: String,
    @ColumnInfo(name = "total_pest_disease")
    @SerializedName("total_pest_disease")
    val totalPestDisease: String,
    @ColumnInfo(name = "remarks")
    @SerializedName("remarks")
    val remarks: String,
    @ColumnInfo(name = "status")
    @SerializedName("status")
    var status: ActivityStatus = ActivityStatus.NEW,
    // Approve Flow
    @ColumnInfo(name = "em_approve")
    @SerializedName("em_approve")
    var approveEM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_approve")
    @SerializedName("dm_approve")
    var approveDM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_id")
    @SerializedName("dm_id")
    var dmId: String = "",
    @ColumnInfo(name = "assistant_approve")
    @SerializedName("assistant_approve")
    var approveAP: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "assistant_id")
    @SerializedName("assistant_id")
    var apId: String = "",
    @ColumnInfo(name = "em_id")
    @SerializedName("em_id")
    var emId: String = "",
    @ColumnInfo(name = "upload_status")
    @SerializedName("upload_status")
    var uploadStatus: Boolean = false,
)

@Entity(tableName = "mnt_mech_weeding_data")
@TypeConverters(DataTypeConverter::class)
data class MechWeedingData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    val locId: Int = 0,
    @ColumnInfo(name = "job_id")
    @SerializedName("job_id")
    val jobId: String,
    @ColumnInfo(name = "date")
    @SerializedName("date")
    val date: String,
    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    val userId: String,
    @ColumnInfo(name = "estate_id")
    @SerializedName("estate_id")
    val estateId: String,
    @ColumnInfo(name = "division_id")
    @SerializedName("division_id")
    val divisionId: String,
    @ColumnInfo(name = "block_id")
    @SerializedName("block_id")
    val blockId: String,
    @ColumnInfo(name = "parcel_id")
    @SerializedName("parcel_id")
    val parcelId: String,
    @ColumnInfo("day_records")
    @SerializedName("record_list")
    val dayRecords: ArrayList<WeedDailyRecord> = arrayListOf(),
    @ColumnInfo(name = "status")
    var status: ActivityStatus = ActivityStatus.NEW,
    // Approve Flow
    @ColumnInfo(name = "em_approve")
    @SerializedName("em_approve")
    var approveEM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_approve")
    @SerializedName("dm_approve")
    var approveDM: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "dm_id")
    @SerializedName("dm_id")
    var dmId: String = "",

    @ColumnInfo(name = "assistant_approve")
    @SerializedName("assistant_approve")
    var approveAP: String = "", // 0 - reject, 1 - approve
    @ColumnInfo(name = "assistant_id")
    @SerializedName("assistant_id")
    var apId: String = "",
    @ColumnInfo(name = "em_id")
    @SerializedName("em_id")
    var emId: String = "",
    @ColumnInfo(name = "upload_status")
    @SerializedName("upload_status")
    var uploadStatus: Boolean = false,
)

