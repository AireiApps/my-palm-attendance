package com.airei.app.phc.attendance.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.airei.app.phc.attendance.R
import com.airei.app.phc.attendance.api.ApiDetails
import com.airei.app.phc.attendance.common.AppPreferences
import com.airei.app.phc.attendance.databinding.FragmentLoadingBinding
import com.airei.app.phc.attendance.entity.EmployeeTable
import com.airei.app.phc.attendance.entity.toEmployeeTable
import com.airei.app.phc.attendance.viewmodel.ApiViewModel
import com.airei.app.phc.attendance.viewmodel.RoomViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.system.exitProcess


class LoadingFragment : Fragment() {
    private var _binding: FragmentLoadingBinding? = null
    private val binding get() = _binding!!

    private val apiViewModel: ApiViewModel by activityViewModels()
    private val roomViewModel: RoomViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoadingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    goBackPage()
                }
            })
        setToolBar("Loading", false, false)

        binding.btnSync.setOnClickListener {
            callApi()
        }
        callApi()
    }

    private fun callApi() {
        if(AppPreferences.apiType == ApiDetails.MILL_API){
            fetchMillEmployees()
        }else{
            fetchPlantationEmployees()
        }
    }

    private fun setToolBar(
        screenTitle: String = getString(R.string.app_name),
        showBack: Boolean = false,
        showAppLogo: Boolean = false,
    ) {
        with(binding.topToolBar) {
            tvTitle.text = screenTitle
            // set start icon → logo or back arrow
            if (showBack) {
                imgBack.setImageResource(R.drawable.ic_arrow_back)
                imgBack.setOnClickListener {
                    goBackPage()
                }
            } else {
                if(showAppLogo){
                    imgBack.setImageResource(R.drawable.img_logo_my_palm_2x)
                    imgBack.setOnClickListener(null)
                }else{
                    imgBack.visibility = View.INVISIBLE
                }

            }
            imgOption.visibility = View.INVISIBLE
        }
    }

    private var progressDialog: AlertDialog? = null

    private fun goBackPage() {
        if (progressDialog?.isShowing == true) return

        val progressBar = ProgressBar(requireContext()).apply {
            isIndeterminate = true
        }

        progressDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Please wait")
            .setMessage("Processing your request...")
            .setView(progressBar)
            .setCancelable(false) // don’t allow dismiss by outside touch
            .setNegativeButton("Close Dialog") { dialog, _ ->
                dialog.dismiss()
                progressDialog = null
            }
            .setPositiveButton("Exit App") { dialog, _ ->
                dialog.dismiss()
                progressDialog = null
                finishAffinity(requireActivity())   // closes all activities
                exitProcess(0)     // kills the process
            }
            .create()

        progressDialog?.show()
    }

    // Call this when process is done
    private fun dismissWaitDialog() {
        if (progressDialog == null) return
        if (progressDialog?.isShowing == true) return
        progressDialog?.dismiss()
        progressDialog = null
    }


    private fun fetchMillEmployees() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                try {
                    val response = apiViewModel.getMillEmployeeList()
                    if (response.isSuccessful) {
                        if (response.body()?.httpcode == 200) {
                            val employees = response.body()?.data
                            if (!employees.isNullOrEmpty()) {
                                Log.e(TAG, "Mill Employee List success: ${employees.size} items")

                                // Convert to Room entity
                                val dbEmpList = employees.map { emp ->
                                    emp.toEmployeeTable()
                                }

                                // Save to Room
                                saveRoomEmpData(dbEmpList)

                                // Hide sync button
                                binding.btnSync.visibility = View.GONE

                                // Show success toast
                                Toast.makeText(requireContext(), "Mill employees loaded successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                Log.e(TAG, "No Mill Employee data found")
                                binding.btnSync.visibility = View.VISIBLE
                                Toast.makeText(requireContext(), "No Mill Employee data found", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Log.e(TAG, "Mill Employee List failed with httpcode: ${response.body()?.httpcode}")
                            binding.btnSync.visibility = View.VISIBLE
                            Toast.makeText(requireContext(), "Failed to fetch Mill employees", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e(TAG, "Mill Employee List failed: ${response.code()} - ${response.message()}")
                        binding.btnSync.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), "API error: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Mill Employee List exception: ${e.message}")
                    binding.btnSync.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), "Exception occurred: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun fetchPlantationEmployees() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                try {
                    val response = apiViewModel.getPlantationEmployeeList()
                    if (response.isSuccessful) {
                        if (response.body()?.httpcode == 200) {
                            val employees = response.body()?.data
                            if (!employees.isNullOrEmpty()) {
                                Log.e(TAG, "Plantation Employee List success: ${employees.size} items")

                                // Convert to Room entity
                                val dbEmpList = employees.map { emp ->
                                    emp.toEmployeeTable()
                                }

                                // Save to Room
                                saveRoomEmpData(dbEmpList)

                                // Hide sync button
                                binding.btnSync.visibility = View.GONE

                                // Show success toast
                                Toast.makeText(requireContext(), "Plantation employees loaded successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                Log.e(TAG, "No Plantation Employee data found")
                                binding.btnSync.visibility = View.VISIBLE
                                Toast.makeText(requireContext(), "No Plantation Employee data found", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Log.e(TAG, "Plantation Employee List failed with httpcode: ${response.body()?.httpcode}")
                            binding.btnSync.visibility = View.VISIBLE
                            Toast.makeText(requireContext(), "Failed to fetch Plantation employees", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e(TAG, "Plantation Employee List failed: ${response.code()} - ${response.message()}")
                        binding.btnSync.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), "API error: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Plantation Employee List exception: ${e.message}")
                    binding.btnSync.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), "Exception occurred: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun CoroutineScope.saveRoomEmpData(dbEmpList: List<EmployeeTable>, apiType: String = ApiDetails.MILL_API) {
        roomViewModel.deleteEmployeeList(apiType)
        roomViewModel.insertEmployeeList(dbEmpList)
        dismissWaitDialog()
        if (isAdded){
            AppPreferences.isDataDownloaded = true
            findNavController().navigate(R.id.attendanceHomeFragment)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG: String = "LoadingFragment"
    }
}