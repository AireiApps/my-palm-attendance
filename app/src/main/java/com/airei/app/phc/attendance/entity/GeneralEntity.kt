package com.airei.app.phc.attendance.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.UUID

enum class AttendanceStatus {
    IN_BOUNDARY,
    OUT_BOUNDARY
}

data class PlantationLocation(
    val lat: String,
    val long: String
)

@Keep
@Entity(tableName = "sample_data")
data class MatchData(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var data: String = "",
    var empUserId: String = "",
    @Ignore
    var distance: Float = 0f,
)