package com.airei.app.phc.attendance.ui.attendance
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.airei.app.phc.attendance.R
import com.airei.app.phc.attendance.adapter.EmployeeAdapter
import com.airei.app.phc.attendance.databinding.FragmentViewEmployee1Binding
import com.airei.app.phc.attendance.entity.EmployeeBioTable
import com.airei.app.phc.attendance.entity.EmployeeTable
import com.airei.app.phc.attendance.viewmodel.RoomViewModel
import com.google.android.material.internal.ViewUtils.hideKeyboard

class ViewEmployeeFragment : Fragment() {
    private var _binding: FragmentViewEmployee1Binding? = null
    private val binding get() = _binding!!

    private val viewModel: RoomViewModel by activityViewModels()

    private var employeeBioTable: List<EmployeeBioTable> = listOf()
    private var employeeTable = mutableListOf<EmployeeTable>()

    private var loadingState = MutableLiveData<Pair<Boolean, Boolean>>(Pair(false, false))

    private var sortType = MutableLiveData<Int>(1)

    /*
    *   1 - office staff
        2 - worker
    * */
    private val employeeTypes = listOf(
        "Office Staff",
        "Worker"
    )

    private val selectOption = MutableLiveData<String>()


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
        setSearchEdittest()
        setToolBar()
        //setEmpTypeDropDown()
        observeData()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setSearchEdittest() {
        with(binding) {

            fun hideKeyboard(view: View) {
                val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }


            // 🔍 Handle text change - show icons dynamically
            etSearch.doAfterTextChanged { text ->
                val editText = etSearch

                // Start icon = search, End icon toggles between search & clear
                if (text.isNullOrEmpty()) {
                    editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search, 0)
                } else {
                    editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_close, 0)
                }

                // Filter employee list (optional)
                // adapter.filter(text.toString())
            }

            // ❌ Handle clear icon click
            etSearch.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    val drawableEnd = etSearch.compoundDrawables[2]
                    if (drawableEnd != null && event.rawX >= (etSearch.right - drawableEnd.bounds.width() - 30)) {
                        etSearch.text?.clear()
                        etSearch.clearFocus()
                        hideKeyboard(etSearch)
                        setEmpListAdapter(empLocList = employeeTable,
                            sort = sortType.value?:1,
                            workType = selectOption.value ?: employeeTypes[0],
                            searchQuery = binding.etSearch.text.toString()
                        )
                        return@setOnTouchListener true
                    }
                }
                false
            }

            // ⌨️ Handle keyboard "Search" button click
            etSearch.setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = etSearch.text.toString().trim()

                    // 🧭 Close keyboard and clear focus
                    etSearch.clearFocus()
                    hideKeyboard(v)

                    // ✅ Start your search action here
                    if (query.isNotEmpty()) {
                        // Example: performSearch(query)
                        val searchQuery = query.orEmpty().trim().lowercase()
                        setEmpListAdapter(empLocList = employeeTable,
                            sort = sortType.value?:1,
                            workType = selectOption.value ?: employeeTypes[0],
                            searchQuery = searchQuery
                        )
                    }

                    true // consume the action
                } else {
                    false
                }
            }
        }
    }


    private fun setEmpListAdapter(empLocList: List<EmployeeTable> = emptyList() , sort : Int = sortType.value ?: 1, workType: String = employeeTypes[0], searchQuery: String = ""){
        val adapter = EmployeeAdapter(object : EmployeeAdapter.ActionClickListener {
            override fun onBtnClick(data: EmployeeTable) {
                // handle button click
                val bundle = Bundle().apply {
                    putString("select_emp_id", data.userId) // selectedEmpId is the value you want to pass
                }
                findNavController().navigate(R.id.faceRegisterFragment, bundle)
            }
        })
        binding.rvEmployee.adapter = adapter
        val filterWorkerType = when (workType) {
            employeeTypes[0] -> empLocList.filter { it.empType == "1" }
            employeeTypes[1] -> empLocList.filter { it.empType == "2" }
            else -> empLocList
        }
        val filteredSearchQueryList = if (searchQuery == ""){
            filterWorkerType
        }else{
            filterWorkerType.filter {
                it.name.lowercase().contains(searchQuery) ||
                        it.empCode.lowercase().contains(searchQuery)
            }
        }

        val sortedList = when (sort) {
            1 -> filteredSearchQueryList.sortedBy { it.name }            // Name A → Z
            2 -> filteredSearchQueryList.sortedByDescending { it.name }  // Name Z → A
            3 -> filteredSearchQueryList.sortedBy { it.empCode }    // Employee ID (optional)
            else -> filteredSearchQueryList
        }

        adapter.setData(sortedList, employeeBioTable)
    }

    private fun setEmpTypeDropDown() {
        with(binding) {
            val adapter = ArrayAdapter(
                requireActivity(),
                android.R.layout.simple_dropdown_item_1line,
                employeeTypes
            )
            etEmployeeType.setAdapter(adapter)
            // ✅ Default selection = first item
            if (employeeTypes.isNotEmpty()) {
                etEmployeeType.setText(employeeTypes[1], false)
                selectOption.postValue(employeeTypes[1])
            }

            // ✅ Handle selection change
            etEmployeeType.setOnItemClickListener { _, _, position, _ ->
                val selected = employeeTypes[position]
                selectOption.postValue(selected)
            }
        }
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
            setEmpListAdapter(empLocList = employeeTable,
                sort = it,
                workType = selectOption.value ?: employeeTypes[0],
                searchQuery = binding.etSearch.text.toString()
            )
        }

        selectOption.observe(viewLifecycleOwner) {
            setEmpListAdapter(empLocList = employeeTable,
                sort = sortType.value?:1,
                workType = it,
                searchQuery = binding.etSearch.text.toString()
            )
        }

        with(viewModel){
            getEmployeeBiosByApiType().observe(viewLifecycleOwner) {
                employeeBioTable = it
                loadingState.postValue(loadingState.value?.copy(first = true))
            }
            getAllEmployees().observe(viewLifecycleOwner) {
                employeeTable.clear()
                employeeTable.addAll(it)
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
                    setEmpTypeDropDown()
                }else{
                    binding.layoutNoData.visibility = View.VISIBLE
                }
            }
        }
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