package com.airei.app.mill.phc.attendance.roomdb

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataTypeConverter {
    @TypeConverter
    fun toFloatArrayList(value: String): List<FloatArray>? {
        val listType = object : TypeToken<List<List<Float>>>() {}.type
        val floatListOfLists: List<List<Float>>? = Gson().fromJson(value, listType)
        return floatListOfLists?.map { list -> list.toFloatArray() }
    }

    @TypeConverter
    fun fromFloatArrayList(floatArrays: List<FloatArray>?): String {
        val listOfFloatLists = floatArrays?.map { floatArray -> floatArray.toList() }
        return Gson().toJson(listOfFloatLists)
    }

}