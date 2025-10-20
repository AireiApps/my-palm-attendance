package com.airei.app.phc.attendance.entity

data class AttendanceReq(
    val data: List<EmpAttendanceTable> = listOf()
)

data class EmpFaceAccessReq(
    val data: List<EmployeeBioTable> = listOf()
)