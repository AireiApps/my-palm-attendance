package com.airei.app.phc.attendance.ui.attendance
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.airei.app.phc.attendance.R
import com.airei.app.phc.attendance.adapter.EmployeeAdapter
import com.airei.app.phc.attendance.databinding.FragmentViewEmployee1Binding
import com.airei.app.phc.attendance.entity.EmployeeBioTable
import com.airei.app.phc.attendance.entity.EmployeeTable
import com.airei.app.phc.attendance.viewmodel.RoomViewModel
import kotlin.collections.filter

class ViewEmployeeFragment : Fragment() {
    private var _binding: FragmentViewEmployee1Binding? = null
    private val binding get() = _binding!!

    private val viewModel: RoomViewModel by activityViewModels()

    private var employeeBioTable: List<EmployeeBioTable> = listOf()
    private var employeeTable = mutableListOf<EmployeeTable>()

    private var loadingState = MutableLiveData<Pair<Boolean, Boolean>>(Pair(false, false))

    private var sortType = MutableLiveData<Int>(1)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewEmployee1Binding.inflate(layoutInflater, container, false)
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
        observeData()
    }

    private fun goBackPage() {
        try {
            findNavController().popBackStack(R.id.attendanceHomeFragment, false)
        } catch (e: Exception) {
            //viewModel.pageNavigation.postValue(R.id.attendanceHomeFragment)
        }
    }

    private fun observeData() {

        sortType.observe(viewLifecycleOwner) {
            setEmployeeList(employeeTable, it)
        }

        with(viewModel){
            getEmployeeBiosByApiType().observe(viewLifecycleOwner) {
                employeeBioTable = it
                loadingState.postValue(loadingState.value?.copy(first = true))
            }
            getAllEmployees().observe(viewLifecycleOwner) {
                employeeTable.clear()
                employeeTable.addAll(it.filter { e -> e.empType == "2"})
               /* if (viewModel.loginEstate != "") {
                    employeeTable = employeeTable.filter { e -> e.estateId == viewModel.loginEstate }.toMutableList()
                }
                if (viewModel.loginDivision != "") {
                    employeeTable = employeeTable.filter { e -> e.divisionId == viewModel.loginDivision }.toMutableList()
                }
                if (viewModel.loginBlock != "") {
                    employeeTable = employeeTable.filter { e -> e.blockId == viewModel.loginBlock }.toMutableList()
                }*/
                loadingState.postValue(loadingState.value?.copy(second = true))
            }
            loadingState.observe(viewLifecycleOwner) {
                Log.i(TAG, "observeData: $it")
                if (employeeTable.isNotEmpty()) {
                    binding.layoutNoData.visibility = View.GONE
                    setEmployeeList(employeeTable)
                }else{
                    binding.layoutNoData.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setEmployeeList(empList:MutableList<EmployeeTable> , sort: Int = sortType.value ?: 1) {
        // Sort based on sortType
        val sortedList = when (sort) {
            1 -> empList.sortedBy { it.name }            // Name A → Z
            2 -> empList.sortedByDescending { it.name }  // Name Z → A
            3 -> empList.sortedBy { it.empCode }    // Employee ID (optional)
            else -> empList
        }

        binding.rvEmployee.adapter =
            EmployeeAdapter(
                sortedList,
                employeeBioTable,
                object : EmployeeAdapter.ActionClickListener {
                    override fun onBtnClick(data: EmployeeTable) {
                        //viewModel.enrollEmpData.postValue(data)
                        val bundle = Bundle().apply {
                            putString("select_emp_id", data.userId) // selectedEmpId is the value you want to pass
                        }
                        findNavController().navigate(R.id.registerFaceFragment, bundle)
                    }
                })
    }

    private fun setToolBar() {
        with(binding.topToolBar) {
            imgBack.visibility = View.VISIBLE
            tvTitle.text = getString(R.string.view_employees)
            imgBack.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            imgOption.visibility = View.VISIBLE
            imgOption.setOnClickListener {
                showSortMenu(it)
            }
        }
    }


    private fun showSortMenu(anchor: View) {
        val popupMenu = PopupMenu(requireContext(), anchor)

        // Add menu items programmatically
        popupMenu.menu.add(0, 1, 0, "Sort by Name (A → Z)")
        popupMenu.menu.add(0, 2, 1, "Sort by Name (Z → A)")
        popupMenu.menu.add(0, 3, 2, "Sort by Employee ID")

        // Set the currently selected item as checked
        popupMenu.menu.setGroupCheckable(0, true, true)
        popupMenu.menu.findItem(sortType.value ?: 1)?.isChecked = true

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                1, 2, 3 -> {
                    sortType.postValue(item.itemId)
                    item.isChecked = true // mark selected
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG: String = "ViewEmployeeFragment"
    }
}