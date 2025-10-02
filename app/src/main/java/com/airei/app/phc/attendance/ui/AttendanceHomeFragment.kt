package com.airei.app.phc.attendance.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.airei.app.phc.attendance.R
import com.airei.app.phc.attendance.databinding.FragmentAttendanceHomeBinding
import com.airei.app.phc.attendance.utils.getTodayEndTimeMillis
import com.airei.app.phc.attendance.utils.getTodayStartTimeMillis
import com.airei.app.phc.attendance.viewmodel.RoomViewModel
import java.util.Calendar


class AttendanceHomeFragment : Fragment() {

    private var _binding: FragmentAttendanceHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RoomViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAttendanceHomeBinding.inflate(layoutInflater, container, false)
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
        setToolBar()
        dataObserve()
        clickActions()

    }

    private fun dataObserve() {
        with(viewModel) {
            getAllEmployees().observe(viewLifecycleOwner) {
                try {
                    if (it.isNotEmpty()) {
                        val totalEmployees = it.size
                        binding.tvTotalEmployees.text = totalEmployees.toString()
                        getAllTodayAttendance(
                            getTodayStartTimeMillis(),
                            getTodayEndTimeMillis()
                        ).observe(viewLifecycleOwner) {
                            val totalPresent = it.distinctBy { it.empUserId }.size
                            binding.tvTotalPresent.text = totalPresent.toString()
                            binding.tvTotalAbsent.text = (totalEmployees - totalPresent).toString()
                        }
                    } else {
                        binding.tvTotalEmployees.text = "--"
                        binding.tvTotalPresent.text = "--"
                        binding.tvTotalAbsent.text = "--"
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    binding.tvTotalEmployees.text = "--"
                    binding.tvTotalPresent.text = "--"
                    binding.tvTotalAbsent.text = "--"
                }

            }


        }
    }

    private fun clickActions() {
        with(binding) {
            btnViewEmployees.setOnClickListener {
                findNavController().navigate(R.id.viewEmployeeFragment)
            }
            btnViewTakeAttendance.setOnClickListener {
                findNavController().navigate(R.id.faceRecognitionFragment)
            }
        }
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

            imgOption.visibility = View.VISIBLE

            imgOption.setImageResource(R.drawable.ic_user)


            imgOption.setOnClickListener { view ->
                findNavController().navigate(R.id.profileFragment)
            }
        }
    }

    private fun goBackPage() {
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG: String = "AttendanceHomeFragment"
    }
}