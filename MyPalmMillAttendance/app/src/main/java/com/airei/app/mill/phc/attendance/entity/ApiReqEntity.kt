package com.airei.app.plantation.phc.entity

import com.airei.app.mill.phc.attendance.entity.ActivityCalenderTable
import com.airei.app.mill.phc.attendance.entity.CensusData
import com.airei.app.mill.phc.attendance.entity.ChemicalWeedingData
import com.airei.app.mill.phc.attendance.entity.CircleWeedingData
import com.airei.app.mill.phc.attendance.entity.CropLossData
import com.airei.app.mill.phc.attendance.entity.EmpAttendanceTable
import com.airei.app.mill.phc.attendance.entity.EmployeeBioTable
import com.airei.app.mill.phc.attendance.entity.EvacuationData
import com.airei.app.mill.phc.attendance.entity.FertilizingData
import com.airei.app.mill.phc.attendance.entity.HarvestData
import com.airei.app.mill.phc.attendance.entity.JobIdList
import com.airei.app.mill.phc.attendance.entity.LooseFruitData
import com.airei.app.mill.phc.attendance.entity.MechWeedingData
import com.airei.app.mill.phc.attendance.entity.OtherActivityData
import com.airei.app.mill.phc.attendance.entity.PestDiseasesData
import com.airei.app.mill.phc.attendance.entity.PruningData
import com.airei.app.mill.phc.attendance.entity.QualityCheckData
import com.airei.app.mill.phc.attendance.entity.RingSprayingData
import com.airei.app.mill.phc.attendance.entity.RingWeedingData
import com.airei.app.mill.phc.attendance.entity.RoadMaintenanceData
import com.airei.app.mill.phc.attendance.entity.SelectiveSprayingData
import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username") val userName: String,
    @SerializedName("password") val password: String
)

data class JobIdListRes(
    val data: List<JobIdList> = listOf()
)

data class AttendanceReq(
    val data: List<EmpAttendanceTable> = listOf()
)

data class EmpFaceAccessReq(
    val data: List<EmployeeBioTable> = listOf()
)

data class HarvestReq(
    val data: List<HarvestData> = listOf()
)

data class LooseFruitRes(
    val data: List<LooseFruitData> = listOf()
)

data class QualityCheckRes(
    val data: List<QualityCheckData> = listOf()
)

data class EvacuationRes(
    val data: List<EvacuationData> = listOf()
)

data class CropLossInspectionRes(
    val data: List<CropLossData> = listOf()
)

data class OtherActivitiesRes(
    val data: List<OtherActivityData> = listOf()
)
data class RingWeedingRes(
    val data: List<RingWeedingData> = listOf()
)
 data class CircleWeedingRes(
     val data: List<CircleWeedingData> = listOf()
 )
 data class PruningRes(
     val data: List<PruningData> = listOf()
 )
 data class FertilizingRes(
     val data: List<FertilizingData> = listOf()
 )
 data class RoadMaintenanceRes(
     val data: List<RoadMaintenanceData> = listOf()
 )
 data class PestDiseasesRes(
     val data: List<PestDiseasesData> = listOf()
 )
 data class RingSprayingRes(
     val data: List<RingSprayingData> = listOf()
 )
 data class SelectiveSprayingRes(
     val data: List<SelectiveSprayingData> = listOf()
 )
 data class CensusRes(
     val data: List<CensusData> = listOf()
 )
 data class MechWeedingRes(
     val data: List<MechWeedingData> = listOf()
 )
 data class ChemicalWeedingRes(
     val data: List<ChemicalWeedingData> = listOf()
 )
 data class CalenderActivityRes(
     val data: List<ActivityCalenderTable> = listOf()
 )

