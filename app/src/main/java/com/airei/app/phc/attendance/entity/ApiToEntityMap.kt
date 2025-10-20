package com.airei.app.phc.attendance.entity

import com.airei.app.phc.attendance.api.ApiDetails
import com.airei.app.phc.attendance.common.AppPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun PlantationLoginResponse.toUserEntity(): UserTable {
    return UserTable(
        userId = this.userId,
        username = this.username,
        designationId = this.designationId,
        designationName = this.designation,
        name = this.name,
        password = this.password,
        welcomeMsg = this.welcomeMsg,
        apiType = ApiDetails.PLANTATION_API                 // Differentiates the source API
    )
}

fun MillLoginResponse.toUserEntity(): UserTable {
    return UserTable(
        userId = this.userId,
        username = this.username,
        designationId = this.designationId?: "",                  // Mill API doesn't have designation ID
        designationName = "",                  // Mill API doesn't have designation name
        name = this.name,
        password = "",
        welcomeMsg = this.welcomeMsg,
        apiType = ApiDetails.PLANTATION_API                       // You can use this to differentiate
    )
}

fun EmployeeFaceRes.toEmployeeFaceTable(): EmployeeBioTable {
    val gson = Gson()
    var uploadStatus = false
    // 🧠 Parse faceCode string safely
    val faceCode: List<List<Float>> = try {
        if (faceCode.isNullOrEmpty() || faceCode == "[]") {
            uploadStatus = false
            emptyList()
        } else {
            val type = object : TypeToken<List<List<Float>>>() {}.type
            uploadStatus = true
            gson.fromJson(faceCode, type)

        }
    } catch (e: Exception) {
        e.printStackTrace()
        uploadStatus = false
        emptyList()
    }

    // 🧩 Build the entity
    return EmployeeBioTable(
        empUserId = userId,
        empFaceData = faceCode.map { it.toFloatArray() },
        uploadStatus = uploadStatus,
        onlineId = if(uploadStatus) userId else "",
        apiType = AppPreferences.apiType,
        inDate = System.currentTimeMillis(),
        modDate = System.currentTimeMillis()
    )
}



fun MillEmployeeResponse.toEmployeeTable(): EmployeeTable {
    return EmployeeTable(
        userId = this.userId ?: "",
        empCode = this.empCode ?: "",
        empType = this.empType ?: "",
        empTypeName = this.name ?: "", // Mill response doesn’t have empTypeName
        name = this.name ?: "",
        image = this.image,
        desigId = this.designationId ?: "",
        designation = this.designationId ?: "",
        deptId = this.departmentId ?: "",
        department = "", // Mill response doesn’t have department name
        faceCode = "", // Mill response doesn’t have face code
        estateId = null,
        divisionId = null,
        blockId = null,
        apiType = ApiDetails.MILL_API                     // You can use this to differentiate
    )
}

fun PlantationEmployeeResponse.toEmployeeTable(): EmployeeTable {
    return EmployeeTable(
        userId = this.userId,
        empCode = this.empCode,
        empType = this.empType,
        empTypeName = this.empTypeName,
        name = this.name,
        image = this.image,
        desigId = this.designationId?: "",
        designation = this.designation,
        deptId = this.departmentId ?: "",
        department = this.department,
        faceCode = this.faceAccessCode,
        estateId = this.estateId,
        //estateName = this.estateName?: "",
        divisionId = this.divisionId,
        //divisionName = this.divisionName?:"",
        blockId = this.blockId,
        //blockName = this.blockName?:"",
        apiType = ApiDetails.PLANTATION_API  // Differentiates Plantation from Mill
    )
}


// 🏠 Estate
fun EstateRes.toEntity(): EstateEntity = EstateEntity(
    id = id,
    estateName = estateName,
    hectare = hectare
)

// 🏢 Division
fun DivisionRes.toEntity(): DivisionEntity = DivisionEntity(
    id = id,
    estateId = estateId,
    divisionName = divisionName,
    hectare = hectare,
    estateName = estateName
)

// 🌳 Block
fun BlockRes.toEntity(): BlockEntity = BlockEntity(
    id = id,
    estateId = estateId,
    divisionId = divisionId,
    blockName = blockName?:"N/A",
    hectare = hectare,
    estateName = estateName,
    divisionName = divisionName
)

// 📦 Parcel
fun ParcelRes.toEntity(): ParcelEntity = ParcelEntity(
    id = id,
    estateId = estateId,
    divisionId = divisionId,
    blockId = blockId,
    parcelName = parcelName,
    hectare = hectare,
    estateName = estateName,
    divisionName = divisionName,
    blockName = blockName?:"N/A"
)




