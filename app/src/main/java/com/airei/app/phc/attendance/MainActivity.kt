package com.airei.app.phc.attendance

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.airei.app.phc.attendance.common.AppPreferences
import com.airei.app.phc.attendance.databinding.ActivityMainBinding
import com.airei.app.phc.attendance.utils.saveStringToFile
import com.airei.app.phc.attendance.utils.setSystemBarColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private var permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    } else {
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val logDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            "MyPlanAttendanceLog"
        )
        Log.d(TAG, "onCreate: ")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setScreenOrientation()
        // Crash log handler setup
        setCrashLogHandler(logDir)
        deleteOldFiles(logDir.path, 3)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setSystemBarColors(window, getColor(R.color.black))
        if (!checkPermissions()) {
            requestPermissions()
        }
        setNavHostFragment()
        //setBottomNavy()
    }

    private fun deleteOldFiles(dirPath: String, days: Int = 3) {
        try {
            val dir = File(dirPath)
            if (!dir.exists() || !dir.isDirectory) return

            // Calculate cutoff time
            val cutoffTime = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000L)

            dir.listFiles()?.forEach { file ->
                if (file.isFile && file.lastModified() < cutoffTime) {
                    if (file.delete()) {
                        Log.d("FileCleaner", "Deleted old file: ${file.name}")
                    } else {
                        Log.w("FileCleaner", "Failed to delete: ${file.name}")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("FileCleaner", "Error while deleting old files", e)
        }
    }


    @SuppressLint("SimpleDateFormat")
    private fun setCrashLogHandler(logDir: File) {
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            try {
                Log.e(TAG, "Uncaught exception: $throwable")

                // Prepare crash log string
                val crashContent = buildString {
                    append("Thread: ${thread.name}\n")
                    append("Exception: $throwable\n\n")

                    throwable.stackTrace.forEach {
                        append("    at $it\n")
                    }

                    var cause = throwable.cause
                    while (cause != null) {
                        append("\nCaused by: $cause\n")
                        cause.stackTrace.forEach {
                            append("    at $it\n")
                        }
                        cause = cause.cause
                    }
                }

                // Auto-delete logs older than 24h
                if (!logDir.exists()) logDir.mkdirs()

                val cutoff = System.currentTimeMillis() - 24 * 60 * 60 * 1000
                logDir.listFiles()?.forEach { file ->
                    if (file.isFile && file.lastModified() < cutoff) {
                        file.delete()
                    }
                }

                // Save crash log using helper
                val sdf = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")
                val fileName = "${sdf.format(Date())}.txt"

                val path = saveStringToFile(crashContent, "MyPlanAttendanceLog", fileName)
                Log.d(TAG, "Crash log written to: $path")

            } catch (e: Exception) {
                Log.e(TAG, "Failed to write crash log. ${e.message}")
            }

            // Close app gracefully
            finishAffinity()
            exitProcess(1)
        }
    }

    private fun checkPermissions(): Boolean {
        return permissions.all {
            ActivityCompat.checkSelfPermission(
                this, it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    // request for permissions
    private fun requestPermissions() {
        permissionRequest.launch(permissions)
    }

    // Permission result
    private val permissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all { it.value }
            permissions.entries.forEach {
                Log.e(TAG, "${it.key} = ${it.value}")
            }

            if (granted) {
                // All permissions granted, continue your action
                Log.e(TAG, "All permissions granted")
            } else {
                // Some permissions denied, redirect to app settings
                showPermissionSettingsDialog()
            }
        }

    // Function to show dialog and redirect to app settings using MaterialAlertDialogBuilder
    private fun showPermissionSettingsDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Permission Required")
            .setMessage("This app requires permissions to function properly. Please enable them in app settings.")
            .setPositiveButton("Open Settings") { dialog, _ ->
                dialog.dismiss()
                openAppSettings()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    // Open app settings
    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", this.packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun setScreenOrientation() {
        requestedOrientation = SCREEN_ORIENTATION_UNSPECIFIED
    }

    private fun setNavHostFragment() {
        Log.d(TAG, "setNavHostFragment: ")
        navController =
            (supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment).navController
        // Check if current fragment is already the same
        if (navController.currentDestination?.id != R.id.splashFragment) {
            navController.navigate(R.id.splashFragment)
            Log.d(TAG, "Navigating to ${R.id.splashFragment}")
        } else {
            Log.d(TAG, "Already on destination ${R.id.splashFragment}, skip navigation")
        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }

    companion object{
        const val TAG = "MainActivity"
    }
}