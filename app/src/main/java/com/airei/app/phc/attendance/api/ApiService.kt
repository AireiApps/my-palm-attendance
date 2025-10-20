package com.airei.app.phc.attendance.api

import com.airei.app.phc.attendance.api.ApiDetails.BLOCK_LIST
import com.airei.app.phc.attendance.api.ApiDetails.DIVISION_LIST
import com.airei.app.phc.attendance.api.ApiDetails.EMP_ATTENDANCE_SAVE
import com.airei.app.phc.attendance.api.ApiDetails.EMP_FACE_LIST
import com.airei.app.phc.attendance.api.ApiDetails.EMP_FACE_SAVE
import com.airei.app.phc.attendance.api.ApiDetails.ESTATE_LIST
import com.airei.app.phc.attendance.api.ApiDetails.PARCEL_LIST
import com.airei.app.phc.attendance.entity.AttendanceReq
import com.airei.app.phc.attendance.entity.BlockRes
import com.airei.app.phc.attendance.entity.DivisionRes
import com.airei.app.phc.attendance.entity.EmpFaceAccessReq
import com.airei.app.phc.attendance.entity.EmployeeFaceRes
import com.airei.app.phc.attendance.entity.EstateRes
import com.airei.app.phc.attendance.entity.MillEmployeeResponse
import com.airei.app.phc.attendance.entity.MillLoginResponse
import com.airei.app.phc.attendance.entity.OnlineData
import com.airei.app.phc.attendance.entity.ParcelRes
import com.airei.app.phc.attendance.entity.PlantationEmployeeResponse
import com.airei.app.phc.attendance.entity.PlantationLoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET(ApiDetails.LOG_IN)
    suspend fun loginUserMill(
        @Query("millcode") millCode: String,
        @Query("username") username: String,
        @Query("password") password: String
    ): Response<ApiResponse<MillLoginResponse>>

    @GET(ApiDetails.LOG_IN)
    suspend fun loginUserPlantation(
        @Query("username") username: String,
        @Query("password") password: String
    ): Response<ApiResponse<PlantationLoginResponse>>

    @GET(ApiDetails.MILL_EMPLOYEE_LIST)
    fun getMillEmployeeList(): Call<ApiResponse<List<MillEmployeeResponse>>>

    @GET(ApiDetails.PLANTATION_EMPLOYEE_LIST)
    fun getPlantationEmployeeList(): Call<ApiResponse<List<PlantationEmployeeResponse>>>

    @GET(EMP_FACE_LIST)
    fun empFaceList(): Call<ApiResponse<List<EmployeeFaceRes>>>

    @POST(EMP_ATTENDANCE_SAVE)
    fun saveEmpAttendance(@Body data: AttendanceReq): Call<ApiResponse<OnlineData>>

    @POST(EMP_FACE_SAVE)
    fun saveEmpFace(@Body data: EmpFaceAccessReq): Call<ApiResponse<OnlineData>>

    @GET(ESTATE_LIST)
    fun estateList(): Call<ApiResponse<List<EstateRes>>>

    @GET(DIVISION_LIST)
    fun divisionList(): Call<ApiResponse<List<DivisionRes>>>

    @GET(BLOCK_LIST)
    fun blockList(): Call<ApiResponse<List<BlockRes>>>

    @GET(PARCEL_LIST)
    fun parcelList(): Call<ApiResponse<List<ParcelRes>>>
}