package com.airei.app.phc.attendance.entity

import com.google.gson.annotations.SerializedName

data class MillLoginResponse(
    @SerializedName("user_id") val userId: String,
    @SerializedName("username") val username: String,
    @SerializedName("name") val name: String,
    @SerializedName("user_role_id") val userRoleId: String,
    @SerializedName("department") val department: String,
    @SerializedName("desigId") val designationId: String?,
    @SerializedName("designation") val designation: String,
    @SerializedName("dept_id") val departmentId: String,
    @SerializedName("language_id") val languageId: String,
    @SerializedName("language") val language: String,
    @SerializedName("millcode") val millCode: String,
    @SerializedName("welcome_msg") val welcomeMsg: String,
    @SerializedName("page_url") val pageUrl: String,
    @SerializedName("mill_lat") val millLat: String,
    @SerializedName("mill_long") val millLong: String,
)


data class PlantationLoginResponse(
    @SerializedName("user_id") val userId: String,
    @SerializedName("username") val username: String,
    @SerializedName("emp_code") val empCode: String,
    @SerializedName("name") val name: String,
    @SerializedName("user_role_id") val userRoleId: String,
    @SerializedName("department") val department: String,
    @SerializedName("desigId") val designationId: String,
    @SerializedName("designation") val designation: String,
    @SerializedName("dept_id") val departmentId: String,
    @SerializedName("millname") val millName: String,
    @SerializedName("mill_lat") val millLat: String,
    @SerializedName("mill_long") val millLong: String,
    @SerializedName("dob") val dob: String,
    @SerializedName("doj") val doj: String,
    @SerializedName("json_location") val jsonLocation: String,
    @SerializedName("face_access_code") val faceAccessCode: String,
    @SerializedName("estate_id") val estateId: String,
    @SerializedName("estate_name") val estateName: String,
    @SerializedName("division_id") val divisionId: String,
    @SerializedName("division_name") val divisionName: String,
    @SerializedName("block_id") val blockId: String,
    @SerializedName("block_name") val blockName: String,
    @SerializedName("password") val password: String,
    @SerializedName("welcome_msg") val welcomeMsg: String,
    @SerializedName("baseurl") val baseUrl: String,
)

data class PlantationEmployeeResponse(

    @SerializedName("user_id") val userId: String,
    @SerializedName("emp_code") val empCode: String,
    @SerializedName("emp_type") val empType: String,
    @SerializedName("emp_type_name") val empTypeName: String,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String?,  // nullable
    @SerializedName("gender") val gender: String,
    @SerializedName("desig_id") val designationId: String?,
    @SerializedName("designation") val designation: String,
    @SerializedName("dept_id") val departmentId: String?,  // nullable
    @SerializedName("department") val department: String,
    @SerializedName("face_access_code") val faceAccessCode: String,
    @SerializedName("estate_id") val estateId: String,
    @SerializedName("estate_name") val estateName: String,
    @SerializedName("division_id") val divisionId: String,
    @SerializedName("division_name") val divisionName: String,
    @SerializedName("block_id") val blockId: String,
    @SerializedName("block_name") val blockName: String,
)

data class MillEmployeeResponse(
    @SerializedName("user_id") val userId: String? = null,
    @SerializedName("location_id") val locationId: String? = null,
    @SerializedName("emp_code") val empCode: String? = null,
    @SerializedName("username") val username: String? = null,
    @SerializedName("users_role") val usersRole: String? = null,
    @SerializedName("password") val password: String? = null,
    @SerializedName("emp_type") val empType: String? = null,
    @SerializedName("user_group_id") val userGroupId: String? = null,
    @SerializedName("user_role_id") val userRoleId: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("desig_id") val designationId: String? = null,
    @SerializedName("dept_id") val departmentId: String? = null,
    @SerializedName("image") val image: String? = null,
)
