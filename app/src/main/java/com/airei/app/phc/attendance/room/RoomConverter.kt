package com.airei.app.phc.attendance.room

import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FloatArrayConverter {

    private val gson = Gson()

    // Convert List<FloatArray> to JSON String
    @TypeConverter
    fun fromFloatArrayList(value: List<FloatArray>?): String {
        if (value == null) return "[]"

        // Convert each FloatArray -> List<Float>
        val listOfLists = value.map { it.toList() }
        return gson.toJson(listOfLists)
    }

    // Convert JSON String back to List<FloatArray>
    @TypeConverter
    fun toFloatArrayList(value: String?): List<FloatArray> {
        if (value.isNullOrEmpty()) return emptyList()

        val listType = object : TypeToken<List<List<Float>>>() {}.type
        val listOfLists: List<List<Float>> = gson.fromJson(value, listType)

        // Convert each List<Float> -> FloatArray
        return listOfLists.map { it.toFloatArray() }
    }
}
