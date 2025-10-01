package com.airei.app.phc.attendance

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
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
import com.airei.app.phc.attendance.utils.setSystemBarColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setScreenOrientation()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setSystemBarColors(window, getColor(R.color.black))
        if (!checkPermissions()) {
            requestPermissions()
        }
        setNavHostFragment()
        //setBottomNavy()
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
        requestedOrientation = SCREEN_ORIENTATION_PORTRAIT
    }

    private fun setNavHostFragment() {
        Log.d(TAG, "setNavHostFragment: ")
        navController =
            (supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment).navController
        if (AppPreferences.loginId == ""){
            navController.navigate(R.id.loginFragment)
        }else{
            if (AppPreferences.isDataDownloaded){
                navController.navigate(R.id.attendanceHomeFragment)
            }else{
                navController.navigate(R.id.loadingFragment)
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object{
        const val TAG = "MainActivity"
    }
}