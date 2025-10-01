package com.airei.app.phc.attendance.ui

import android.os.Bundle
import android.util.Log
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
import com.airei.app.phc.attendance.databinding.FragmentLoginBinding
import com.airei.app.phc.attendance.entity.MillLoginResponse
import com.airei.app.phc.attendance.entity.PlantationLoginResponse
import com.airei.app.phc.attendance.entity.toUserEntity
import com.airei.app.phc.attendance.utils.restartApp
import com.airei.app.phc.attendance.utils.showRestartApiAlert
import com.airei.app.phc.attendance.utils.showServerSelectDialog
import com.airei.app.phc.attendance.viewmodel.ApiViewModel
import com.airei.app.phc.attendance.viewmodel.RoomViewModel


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var dialog: AlertDialog? = null
    private val apiViewModel: ApiViewModel by activityViewModels()
    private val roomViewModel: RoomViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    goBackPage()
                }
            })
        observerLogin()
        setlayout(AppPreferences.apiType)
        checkLoginServer()
        clickAction()
    }

    private fun observerLogin() {
        apiViewModel.millLoginState.observe(viewLifecycleOwner) { result ->
            try {
                result?.let { res ->
                    Log.e(TAG, "Mill Login State: ${res.getOrNull()}")
                    res.fold(
                        onSuccess = { user ->
                            if (user == null) {
                                Toast.makeText(
                                    requireContext(),
                                    "Mill Login Failed: Null response",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Log.e(TAG, "Mill Login Success: Code ${user.status}")
                                if (user.httpcode == 200) {
                                    val data = user.data
                                    if (data != null) {
                                        AppPreferences.loginId = data.userId
                                        AppPreferences.isDataDownloaded = false
                                        Toast.makeText(
                                            requireContext(),
                                            "Mill Login Success: ${data.username}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        saveLoginData(data)
                                        findNavController().navigate(R.id.loadingFragment)
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "Mill Login Failed: ${user.status} : ${user.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "Mill Login Failed: ${user.status} : ${user.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        },
                        onFailure = { error ->
                            Log.e(TAG, "Mill Login Failed: ${error.message}")
                            Toast.makeText(
                                requireContext(),
                                "Mill Login Failed: ${error.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Mill Login Failed: ${e.message}")
                Toast.makeText(
                    requireContext(),
                    "Mill Login Failed: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                showLoading(false)
            }
        }

        apiViewModel.plantationLoginState.observe(viewLifecycleOwner) { result ->
            try {
                result?.let { res ->
                    Log.e(TAG, "Plantation Login State: ${res.getOrNull()}")
                    res.fold(
                        onSuccess = { user ->
                            if (user == null) {
                                Toast.makeText(
                                    requireContext(),
                                    "Plantation Login Failed: Null response",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Log.e(TAG, "Plantation Login Success: Code ${user.status}")
                                if (user.httpcode == 200) {
                                    val data = user.data
                                    if (data != null) {
                                        saveLoginData(data)
                                        AppPreferences.loginId = data.userId
                                        AppPreferences.isDataDownloaded = false
                                        Toast.makeText(
                                            requireContext(),
                                            "Plantation Login Success: ${data.username}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        findNavController().navigate(R.id.loadingFragment)
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "Plantation Login Failed: ${user.status} : ${user.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "Plantation Login Failed: ${user.status} : ${user.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        },
                        onFailure = { error ->
                            Log.e(TAG, "Plantation Login Failed: ${error.message}")
                            Toast.makeText(
                                requireContext(),
                                "Plantation Login Failed: ${error.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Plantation Login Failed: ${e.message}")
                Toast.makeText(
                    requireContext(),
                    "Plantation Login Failed: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }finally {
                showLoading(false)
            }
        }
    }


    private fun setlayout(apiType: String) {
        if (apiType == PLANTATION_API) {
            binding.tlMillCode.visibility = View.GONE
        } else {
            binding.tlMillCode.visibility = View.VISIBLE
            binding.etMillCode.setText("1034")
        }
    }

    private fun showLoading(isLoading: Boolean = false){
        with(binding){
            if (isLoading) {
                btnLogin.visibility = View.GONE
                pbLoading.visibility = View.VISIBLE
            } else{
                btnLogin.visibility = View.VISIBLE
                pbLoading.visibility = View.GONE
            }
        }
    }

    private fun clickAction() {
        with(binding) {
            tvServerSelect.setOnClickListener {
                showAlertDialog()
            }
            tvForgotPassword.setOnClickListener {
                Toast.makeText(requireContext(), "Forgot Password", Toast.LENGTH_SHORT).show()
            }
            btnLogin.setOnClickListener {

                val loginId = etUsername.text.toString()
                val password = etPassword.text.toString()
                val millCode = etMillCode.text.toString()
                if (loginId == "" || password == "") {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.kindly_fill_all_fields),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (AppPreferences.apiType == PLANTATION_API) {
                        Log.e(TAG, "Plantation Login: $loginId, $password")
                        apiViewModel.loginUserPlantation(loginId, password)
                    } else {
                        Log.e(TAG, "Mill Login: $millCode, $loginId, $password")
                        apiViewModel.loginUserMill(millCode, loginId, password)
                    }
                    showLoading(true)
                    Toast.makeText(requireContext(), "Proceeding to Login", Toast.LENGTH_SHORT)
                        .show()

                }
            }
        }
    }

    private fun saveLoginData(data: PlantationLoginResponse) {
        val dbData = data.toUserEntity()
        roomViewModel.deleteUserID(dbData.userId)
        roomViewModel.insertUser(dbData)
    }

    private fun saveLoginData(data: MillLoginResponse) {
        val dbData = data.toUserEntity()
        roomViewModel.deleteUserID(dbData.userId)
        roomViewModel.insertUser(dbData)
    }

    private fun goBackPage() {
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        if (dialog != null) {
            if (dialog?.isShowing == true) {
                dialog?.dismiss()
            }
        }
    }

    private fun checkLoginServer() {
        // 1. Plantation API
        // 2. Mill API
        val apiType = AppPreferences.apiType
        Log.e(TAG, "API Type: $apiType")
        if (apiType == "") {
            showAlertDialog()
        }
    }

    private fun showAlertDialog() {
        dialog = showServerSelectDialog(
            requireActivity(), preSelectedMode = PLANTATION_API
        ) { mode ->
            Toast.makeText(requireContext(), "Selected: $mode", Toast.LENGTH_SHORT).show()
            AppPreferences.apiType = mode
            Log.e(TAG, "API Type Show AlertDialog:  $mode")
            showRestartApiAlert(requireActivity()) {
                Toast.makeText(requireContext(), "Restarting App", Toast.LENGTH_SHORT).show()
                restartApp(requireActivity())
            }
        }
        dialog?.show()
    }

    companion object {
        const val TAG: String = "LoginFragment"
    }
}


