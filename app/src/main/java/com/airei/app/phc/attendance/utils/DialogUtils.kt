package com.airei.app.phc.attendance.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.airei.app.phc.attendance.R
import com.airei.app.phc.attendance.common.MILL_API
import com.airei.app.phc.attendance.common.PLANTATION_API
import com.airei.app.phc.attendance.databinding.DialogApiSelectBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.system.exitProcess
import androidx.core.graphics.drawable.toDrawable
import com.airei.app.phc.attendance.common.AppPreferences

fun showServerSelectDialog(
    context: Context,
    preSelectedMode: String? = null,
    onModeSelected: (String) -> Unit,
): AlertDialog {
    val binding = DialogApiSelectBinding.inflate(LayoutInflater.from(context))

    // Pre-select radio if provided
    when (preSelectedMode) {
        PLANTATION_API -> binding.radioPlantation.isChecked = true
        MILL_API -> binding.radioMill.isChecked = true
    }

    val dialog = MaterialAlertDialogBuilder(context)
        .setView(binding.root)
        .create()
    dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
    var selectedMode: String = AppPreferences.apiType
    if (selectedMode == "") {
        selectedMode = PLANTATION_API
    }
    // Enable button only when a selection is made
    binding.radioGroupMode.setOnCheckedChangeListener { _, checkedId ->
        selectedMode = when (checkedId) {
            R.id.radio_plantation -> PLANTATION_API
            R.id.radio_mill -> MILL_API
            else -> {""}
        }
        binding.btnContinue.isEnabled = true
    }

    // Handle Continue button click
    binding.btnContinue.setOnClickListener {
        selectedMode.let {
            onModeSelected(it)
            dialog.dismiss()
        }
    }

    return dialog
}



fun showRestartApiAlert(
    context: Context,
    onConfirm: () -> Unit,
) {
    val dialog = MaterialAlertDialogBuilder(context)
        .setTitle("Restart API")
        .setMessage("Do you want to restart the API?")
        .setCancelable(false)
        .setPositiveButton("Yes") { _, _ ->
            onConfirm() // callback for restarting API
        }
        .setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss() // just dismiss
        }
        .create()

    dialog.show()
}

fun restartApp(context: Context) {
    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.set(
        AlarmManager.RTC,
        System.currentTimeMillis() + 100, // restart after 100ms
        pendingIntent
    )

    // Kill the current process
    exitProcess(0)
}
