package com.airei.app.phc.attendance.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import com.airei.app.phc.attendance.databinding.ItemSpinnerBinding
import java.util.Locale
import kotlin.collections.toList
import kotlin.text.contains
import kotlin.text.isNullOrBlank
import kotlin.text.lowercase
import kotlin.text.trim

class CustomSpinnerAdapter(private val originalData: List<String>, private val listener: OnSpinnerItemClickListener) :
    BaseAdapter(), Filterable {

        private var filteredData: List<String> = originalData.toList()

    override fun getCount(): Int {
        return filteredData.size
    }

    override fun getItem(position: Int): Any {
        return filteredData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val itemBinding: ItemSpinnerBinding
        val itemView: View

        if (convertView == null) {
            // Inflate the layout if convertView is null
            itemBinding = ItemSpinnerBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
            itemView = itemBinding.root
            itemView.tag = itemBinding
        } else {
            // Reuse the convertView if it's not null
            itemView = convertView
            itemBinding = itemView.tag as ItemSpinnerBinding
        }

        // Set the text
        itemBinding.txtName.text = filteredData[position]

        // Set onClickListener to the item
        itemView.setOnClickListener {
            listener.onItemClick(filteredData[position])
        }

        return itemView
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = mutableListOf<String>()

                if (constraint.isNullOrBlank()) {
                    filteredList.addAll(originalData)
                } else {
                    val filterPattern = constraint.toString().lowercase(Locale.ROOT).trim()

                    for (item in originalData) {
                        if (item.lowercase(Locale.ROOT).contains(filterPattern)) {
                            filteredList.add(item)
                        }
                    }
                }

                val results = FilterResults()
                results.values = filteredList
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                val list = results?.values as? List<String> ?: emptyList()
                Log.d("CustomSpinnerAdapter", "results: $list")
                filteredData = list
                Log.d("CustomSpinnerAdapter", "publishResults: $filteredData")
                notifyDataSetChanged()
            }
        }
    }

    // Interface for item click listener
    interface OnSpinnerItemClickListener {
        fun onItemClick(item: String)
    }
}
