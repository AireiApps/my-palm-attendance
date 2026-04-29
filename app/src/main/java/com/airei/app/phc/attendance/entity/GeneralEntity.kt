package com.airei.app.phc.attendance.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.airei.app.phc.attendance.api.ApiDetails.PLANTATION_BASE_URL
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

data class ClientBaseIP(
    val siteName: String,
    val ip: String,
    val path: String,
    val isSelect: Boolean = false
) {
    fun getFullLink(): String {
        val normalizedPath = if (path.startsWith("/")) path else "/$path"
        return "http://$ip$normalizedPath"
    }
}

