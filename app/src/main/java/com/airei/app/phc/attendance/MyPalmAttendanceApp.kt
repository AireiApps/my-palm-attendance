package com.airei.app.phc.attendance

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.airei.app.phc.attendance.common.AppPreferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyPalmAttendanceApp : Application() {

    companion object {
        lateinit var instance: MyPalmAttendanceApp
            private set
        private const val TAG = "MyPalmAttendanceApp"
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate() {
        super.onCreate()
        instance = this
        AppPreferences.init(this)
    }
}
