package com.airei.app.phc.attendance.utils

import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.graphics.drawable.toDrawable
import com.airei.app.phc.attendance.R
import com.airei.app.phc.attendance.adapter.CustomSpinnerAdapter
import com.airei.app.phc.attendance.api.ApiDetails.MILL_API
import com.airei.app.phc.attendance.api.ApiDetails.PLANTATION_API
import com.airei.app.phc.attendance.common.AppPreferences
import com.airei.app.phc.attendance.databinding.DialogApiSelectBinding
import com.airei.app.phc.attendance.databinding.LayoutCommonMsgBinding
import com.airei.app.phc.attendance.databinding.LayoutEmpSaveBinding
import com.airei.app.phc.attendance.databinding.LayoutSpinnerOptionBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.Locale
import kotlin.system.exitProcess

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
        else -> {
            binding.radioPlantation.isChecked = true
        }
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


/*
private fun showCustomAlert() {
    val dialog = CustomAlert(
        context = requireContext(),
        title = "Warning!",
        message = "Do you want to save employee data?",
        positiveText = "Save",
        negativeText = "Cancel",
        onPositive = { Toast.makeText(requireContext(), "Saved!", Toast.LENGTH_SHORT).show() },
        onNegative = { Toast.makeText(requireContext(), "Cancelled!", Toast.LENGTH_SHORT).show() }
    ).build()

    dialog.show() // <-- you decide when to show
}*/

class CustomAlert(
    private val context: Context,
    private val title: String,
    private val message: String,
    private val lottieRes: Int = R.raw.round_warning,
    private val positiveText: String = "OK",
    private val negativeText: String = "Cancel",
    private val onPositive: (() -> Unit)? = null,
    private val onNegative: (() -> Unit)? = null,
) {
    fun build(): AlertDialog {
        val binding = LayoutCommonMsgBinding.inflate(LayoutInflater.from(context))

        val alertDialog =
            AlertDialog.Builder(context).setView(binding.root).setCancelable(false).create()

        alertDialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

        // Set values using binding
        binding.tvTitle.text = title
        binding.tvMessage.text = message
        binding.lottieView.setAnimation(lottieRes)

        binding.saveEmp.text = positiveText
        binding.saveCancel.text = negativeText

        // Button actions
        binding.saveEmp.setOnClickListener {
            onPositive?.invoke()
            alertDialog.dismiss()
        }

        binding.saveCancel.setOnClickListener {
            onNegative?.invoke()
            alertDialog.dismiss()
        }

        return alertDialog
    }
}

fun Context.showEmpDialog(
    bitmap: Bitmap?,
    empName: String,
    onSave: (String) -> Unit
) {
    val binding = LayoutEmpSaveBinding.inflate(LayoutInflater.from(this))

    // set image if available
    bitmap?.let {
        binding.imgEmpFace.setImageBitmap(it)
    }

    // set emp name
    binding.empName.setText(empName)
    binding.empName.inputType = InputType.TYPE_NULL
    binding.empName.isFocusable = false

    val dialog = AlertDialog.Builder(this)
        .setView(binding.root)
        .setCancelable(true)
        .create()

    // handle save button click
    binding.saveEmp.setOnClickListener {
        val updatedData = binding.empName.text.toString().trim()
        onSave(updatedData)
        dialog.dismiss()
    }

    dialog.show()
}

private var SelectionDialog: Dialog? = null

fun showFieldSelectionDialog(
    context: Context,
    txtTitle: String,
    fieldList: List<String>,
    edtField: AppCompatEditText
) {
    if (SelectionDialog != null) {
        if (SelectionDialog!!.isShowing) {
            SelectionDialog!!.dismiss()
        }
    }
    val binding = LayoutSpinnerOptionBinding.inflate(LayoutInflater.from(context))
    SelectionDialog = Dialog(context)
    if (SelectionDialog != null) {
        SelectionDialog!!.setContentView(binding.root)
        SelectionDialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        SelectionDialog!!.show()

        binding.txtTitle.text = txtTitle
        binding.txtEmpty.visibility = View.GONE
        binding.btnClose.setOnClickListener { SelectionDialog!!.dismiss() }

        val spinnerFieldAdapter =
            CustomSpinnerAdapter(
                fieldList,
                object : CustomSpinnerAdapter.OnSpinnerItemClickListener {
                    override fun onItemClick(item: String) {
                        edtField.setText(item)
                        SelectionDialog!!.dismiss()
                    }
                })

        binding.lvSpinner.adapter = spinnerFieldAdapter

        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                spinnerFieldAdapter.filter.filter(s.toString().lowercase(Locale.getDefault()))
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.lvSpinner.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                edtField.setText(fieldList[position])
                SelectionDialog!!.dismiss()
            }
    }
}
