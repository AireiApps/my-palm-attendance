package com.airei.app.phc.attendance.api

import com.airei.app.phc.attendance.entity.MillEmployeeResponse
import com.airei.app.phc.attendance.entity.MillLoginResponse
import com.airei.app.phc.attendance.entity.PlantationEmployeeResponse
import com.airei.app.phc.attendance.entity.PlantationLoginResponse
import retrofit2.Response
import retrofit2.http.GET
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
    suspend fun getMillEmployeeList(): Response<ApiResponse<List<MillEmployeeResponse>>>

    @GET(ApiDetails.PLANTATION_EMPLOYEE_LIST)
    suspend fun getPlantationEmployeeList(): Response<ApiResponse<List<PlantationEmployeeResponse>>>
}