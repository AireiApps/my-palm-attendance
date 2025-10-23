package com.airei.app.phc.attendance.adapter

import android.annotation.SuppressLint
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
    private val action: ActionClickListener
) : RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {

    private var empList: List<EmployeeTable> = emptyList()
    private var empBioList: List<EmployeeBioTable>? = null
    private var filteredList: List<EmployeeTable> = emptyList()

    init {
        setHasStableIds(true)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(empList: List<EmployeeTable>, empBioList: List<EmployeeBioTable>?) {
        // Clear old data
        this.empList = emptyList()
        this.filteredList = emptyList()
        notifyDataSetChanged() // Clear old views from RecyclerView

        // Set new data
        this.empList = empList
        this.empBioList = empBioList
        this.filteredList = empList

        // Refresh new data
        notifyDataSetChanged()
    }


    /** 🔍 Filter list by name or code */
    fun filter(query: String) {
        val searchText = query.trim().lowercase()
        filteredList = if (searchText.isEmpty()) {
            empList
        } else {
            empList.filter {
                it.name.lowercase().contains(searchText) ||
                        it.empCode.lowercase().contains(searchText)
            }
        }
        notifyDataSetChanged()
    }

    inner class EmployeeViewHolder(
        private val binding: ItemEmpBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(emp: EmployeeTable) {
            with(binding) {
                tvEnroll.text = MyPalmAttendanceApp.instance.getString(R.string.enroll_face)

                // Update text if bio exists
                empBioList?.find { it.empUserId == emp.userId }?.let {
                    tvEnroll.text = MyPalmAttendanceApp.instance.getString(R.string.update_str)
                }

                imgEmpFace.setImageResource(R.drawable.img_empty_profile)

                tvEnroll.setOnClickListener { action.onBtnClick(emp) }

                tvEmpName.text = if (emp.name.length > 30) emp.name.take(30) + "..." else emp.name
                tvEmpCode.text = MyPalmAttendanceApp.instance
                    .getString(R.string.emp_code)
                    .plus(" : ${emp.empCode}")
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
        holder.bind(filteredList[position])
    }

    override fun getItemCount(): Int = filteredList.size

    override fun getItemId(position: Int): Long = filteredList[position].userId.hashCode().toLong()

    interface ActionClickListener {
        fun onBtnClick(data: EmployeeTable)
    }
}
