package com.airei.app.phc.attendance.utils

import android.view.View
import android.view.Window
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
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