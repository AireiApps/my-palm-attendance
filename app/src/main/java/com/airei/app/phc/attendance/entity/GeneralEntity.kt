package com.airei.app.phc.attendance.entity

enum class AttendanceStatus {
    IN_BOUNDARY,
    OUT_BOUNDARY
}

data class PlantationLocation(
    val lat: String,
    val long: String
)