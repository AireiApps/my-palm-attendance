package com.airei.app.phc.attendance.api

object ApiDetails {
    const val PLANTATION_API = "1"
    const val MILL_API = "2"

    const val PLANTATION_BASE_URL = "https://phc.plant.mypalm.com.my/MobileAPI/"
    const val MILL_BASE_URL = "https://phc.mypalm.com.my/MobileAPI/"

    // Endpoints
    const val LOG_IN = "login_attendance"

    const val MILL_EMPLOYEE_LIST = "employee_list"
    const val PLANTATION_EMPLOYEE_LIST = "userList"

    const val EMP_FACE_LIST = "faceAccessList"

    const val EMP_ATTENDANCE_SAVE = "attendance_save"
    const val EMP_FACE_SAVE = "update_face_code"

    const val ESTATE_LIST = "estateList"
    const val DIVISION_LIST = "divisionList"
    const val BLOCK_LIST = "blocksList"
    const val PARCEL_LIST = "parcelList"

}
