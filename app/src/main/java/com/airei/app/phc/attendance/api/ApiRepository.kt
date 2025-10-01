package com.airei.app.phc.attendance.api

import com.airei.app.phc.attendance.entity.MillEmployeeResponse
import com.airei.app.phc.attendance.entity.MillLoginResponse
import com.airei.app.phc.attendance.entity.PlantationLoginResponse
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val apiService: ApiService,
) {
    /*   suspend fun loginUserMill(millCode: String,username: String, password: String) = apiService.loginUserMill(millCode = millCode, username = username, password = password)
       suspend fun loginUserPlantation(username: String, password: String) = apiService.loginUserPlantation( username = username, password = password)
   */
    suspend fun loginUserMill(
        millCode: String,
        username: String,
        password: String,
    ): ApiResponse<MillLoginResponse>? {
        val response = apiService.loginUserMill(millCode, username, password)
        if (response.isSuccessful) {
            return response.body()
        } else {
            return ApiResponse.error(response.errorBody()?.string() ?: "Mill login failed", null, response.code())
            //throw Exception(response.errorBody()?.string() ?: "Mill login failed")
        }
    }

    suspend fun loginUserPlantation(
        username: String,
        password: String,
    ): ApiResponse<PlantationLoginResponse>? {
        val response = apiService.loginUserPlantation(username, password)
        if (response.isSuccessful) {
            return response.body()
        } else {
            return ApiResponse.error(response.errorBody()?.string() ?: "Plantation login failed", null, response.code())
            //throw Exception(response.errorBody()?.string() ?: "Plantation login failed")
        }
    }

    suspend fun getMillEmployeeList()= apiService.getMillEmployeeList()
    suspend fun getPlantationEmployeeList()= apiService.getPlantationEmployeeList()
}