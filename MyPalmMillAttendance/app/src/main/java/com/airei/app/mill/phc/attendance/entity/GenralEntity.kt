package com.airei.app.plantation.phc.entity

import com.airei.app.mill.phc.attendance.entity.ActivityStatus

data class ToastMsgType(
    val error: String = "error",
    val success: String = "success",
    val info: String = "info",
    val warning: String = "warning",
    val normal: String = "normal"
)

data class PlantationLocation(
    val lat: String,
    val long: String
)

enum class AttendanceStatus {
    IN_BOUNDARY,
    OUT_BOUNDARY
}

data class MainMenu(
    val title: String,
    val icon: Int
)

data class RecordItem(
    val id: Int,
    val recordName: String
)

data class OptionData(
    val block: String = "",
    val data: String = "",
)

data class RecordCardDate(
    val recordName: String,
    val id: String,
    val recordDate: String,
    val estateName: String,
    val divisionName: String,
    val blockName: String,
    val parcelName: String,
    val status: ActivityStatus
)

data class CalendarDay(
    val dayOfMonth: Int = 0,
    val activityCount: Int = 0,
    val color: Int? = null
)