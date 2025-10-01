package com.airei.app.phc.attendance.api

enum class ApiStatus {
    SUCCESS,
    ERROR,
    LOADING
}

data class ApiResponse<out T>(
    val status: String,
    val data: T?,
    val message: String? = null,
    val httpcode: Int = 0
) {
    companion object {
        fun <T> success(data: T?, code: Int): ApiResponse<T> {
            return ApiResponse(ApiStatus.SUCCESS.toString(), data, null, code)
        }

        fun <T> error(msg: String, data: T?, code: Int): ApiResponse<T> {
            return ApiResponse(ApiStatus.ERROR.toString(), data, msg, code)
        }

        fun <T> loading(data: T?): ApiResponse<T> {
            return ApiResponse(ApiStatus.LOADING.toString(), data)
        }
    }
}