package com.airei.app.phc.attendance.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.airei.app.phc.attendance.R
import com.airei.app.phc.attendance.common.AppPreferences
import com.airei.app.phc.attendance.common.PLANTATION_API
import com.airei.app.phc.attendance.databinding.FragmentProfileBinding
import com.airei.app.phc.attendance.entity.UserTable
import com.airei.app.phc.attendance.utils.restartApp
import com.airei.app.phc.attendance.utils.showRestartApiAlert
import com.airei.app.phc.attendance.utils.showServerSelectDialog
import com.airei.app.phc.attendance.viewmodel.RoomViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RoomViewModel by activityViewModels()

    private var userData: UserTable? = null
    private var dialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
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
        setToolBar(
            screenTitle = getString(R.string.profile),
            showBack = true
        )
        setProfileData()
        setClickListener()
    }

    private fun setClickListener() {
        with(binding) {

            btnSyncData.setOnClickListener {
                AppPreferences.isDataDownloaded = false
                findNavController().navigate(R.id.loadingFragment)
            }

            btnIpConfig.setOnClickListener {
                dialog = showServerSelectDialog(
                    requireActivity(), preSelectedMode = PLANTATION_API
                ) { mode ->
                    val oldMode = AppPreferences.apiType
                    Toast.makeText(requireContext(), "Selected: $mode", Toast.LENGTH_SHORT).show()
                    if (oldMode != mode) {
                        AppPreferences.apiType = mode
                        showRestartApiAlert(requireActivity()) {
                            Toast.makeText(requireContext(), "Restarting App", Toast.LENGTH_SHORT)
                                .show()
                            restartApp(requireActivity())
                        }
                    }else{
                        Toast.makeText(requireContext(), "continue with same mode", Toast.LENGTH_SHORT).show()
                    }
                }
                dialog?.show()
            }

            btnLogout.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Confirm Logout")
                    .setMessage("Before login please ensure local data is all updated.\n\nDo you really want to logout?")
                    .setPositiveButton("Logout") { dialog, _ ->
                        logout() // call your logout function
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setCancelable(false) // optional, prevent dismiss by outside touch
                    .show()
            }
        }
    }

    private fun logout() {
        AppPreferences.loginId = ""
        AppPreferences.isDataDownloaded = false
        findNavController().navigate(R.id.loginFragment)
    }

    private fun setToolBar(
        screenTitle: String = getString(R.string.app_name),
        showBack: Boolean = false,
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
                imgBack.setImageResource(R.drawable.img_logo_my_palm_2x)
                imgBack.setOnClickListener(null)
            }
            imgOption.visibility = View.INVISIBLE
        }
    }

    private fun setProfileData() {
        with(binding) {
            tvDesignation.text = userData?.designationName ?: tvDesignation.text
            tvUsername.text = userData?.name ?: "Unknown"
            viewModel.getAllUsers().observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    userData = it.find { it.userId == AppPreferences.loginId }
                    tvDesignation.text = userData?.designationName ?: tvDesignation.text
                    tvUsername.text = userData?.name ?: "Unknown"
                }
            }
        }
    }

    private fun goBackPage() {
        findNavController().navigate(R.id.attendanceHomeFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dialog?.dismiss()
        _binding = null
    }

    companion object {
        const val TAG: String = "ProfileFragment"
    }
}