package com.airei.app.phc.attendance.entity

import com.airei.app.phc.attendance.api.ApiDetails

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



