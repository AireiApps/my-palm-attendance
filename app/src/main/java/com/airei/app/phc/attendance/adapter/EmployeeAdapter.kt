package com.airei.app.phc.attendance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airei.app.phc.attendance.MyPalmAttendanceApp
import com.airei.app.phc.attendance.R
import com.airei.app.phc.attendance.databinding.ItemEmpBinding
import com.airei.app.phc.attendance.entity.EmployeeBioTable
import com.airei.app.phc.attendance.entity.EmployeeTable
import kotlin.collections.find
import kotlin.text.substring

class EmployeeAdapter(
    private val empList: List<EmployeeTable>,
    private val empBioList: List<EmployeeBioTable>?,
    private val action: ActionClickListener// List of custom CalendarDay data class items
) : RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {

    init {
        setHasStableIds(true)
    }

    inner class EmployeeViewHolder(
        private val binding: ItemEmpBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(emp: EmployeeTable) {
            with(binding) {
                tvEnroll.text = MyPalmAttendanceApp.instance.getString(R.string.enroll_face)
                if (empBioList != null) {
                    val empBio = empBioList.find { it.empUserId == emp.userId }
                    if (empBio != null) {
                        tvEnroll.text = MyPalmAttendanceApp.instance.getString(R.string.update_str)
                    }
                }
                imgEmpFace.setImageResource(R.drawable.img_empty_profile)
                tvEnroll.setOnClickListener {
                    action.onBtnClick(emp)
                }
                val empName = emp.name
                tvEmpName.text = if (empName.length > 30) {
                    empName.substring(0, 30) + "..."
                } else {
                    empName
                }
                tvEmpCode.text = MyPalmAttendanceApp.instance.getString(R.string.emp_code).plus(emp.empCode)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val binding = ItemEmpBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EmployeeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(empList[position])
    }

    override fun getItemCount(): Int = empList.size

    interface ActionClickListener {
        fun onBtnClick(data: EmployeeTable)
    }
}