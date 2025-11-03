package com.airei.app.phc.attendance.utils

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.gson.Gson
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.OutputStream
import java.util.Calendar


fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun AppCompatEditText.disable() {
    isEnabled = false
    isFocusable = false
    isFocusableInTouchMode = false
}
fun AppCompatEditText.enable(editable: Boolean = true) {
    isEnabled = true
    isFocusable = editable
    isFocusableInTouchMode = editable
}



fun setSystemBarColors(window: Window, statusBarColor: Int) {
    window.statusBarColor = statusBarColor
    window.navigationBarColor = statusBarColor

    val controller = WindowCompat.getInsetsController(window, window.decorView)
    controller.apply {
        isAppearanceLightStatusBars = false // Status bar text/icons will be white
        isAppearanceLightNavigationBars = false // Navigation bar text/icons will be white
        systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
    }
}

fun getTodayEndTimeMillis(): Long {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }
    return calendar.timeInMillis
}

fun getTodayStartTimeMillis(): Long {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return calendar.timeInMillis
}



fun saveStringToFile(
    content: String,
    dirName: String = "MyPlanAttendanceLog",
    fileName: String = "${System.currentTimeMillis()}.txt"
): String? {
    return try {
        // Create app-specific external storage dir
        val logDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            dirName
        )
        if (!logDir.exists()) logDir.mkdirs()

        // Create the file
        val logFile = File(logDir, fileName)

        FileWriter(logFile).use { writer ->
            writer.write(content)
        }
        Log.d("saveStringToFile", "saveStringToFile: ${logFile.absolutePath}")
        logFile.absolutePath // return path for reference
    } catch (e: Exception) {
        Log.e("saveStringToFile", "saveStringToFile: ${e.message}")
        e.printStackTrace()
        null
    }
}