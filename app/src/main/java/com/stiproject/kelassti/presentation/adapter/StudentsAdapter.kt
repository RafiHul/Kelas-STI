package com.stiproject.kelassti.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.stiproject.kelassti.data.model.response.mahasiswa.MahasiswaData
import com.stiproject.kelassti.data.model.response.mahasiswa.MahasiswaDataArray
import com.stiproject.kelassti.databinding.RecyclerviewitemStudentsBinding

class StudentsAdapter(val onClickItem: (String) -> Unit): RecyclerView.Adapter<StudentsAdapter.MyViewHolder>(), Filterable {

    inner class MyViewHolder(val binding: RecyclerviewitemStudentsBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(currentItem: MahasiswaData){
            val nim = String.format(currentItem.NIM.toString())
            binding.textViewNameStudents.text = currentItem.name
            binding.textViewNimStudents.text = nim
            binding.textViewNomorStudents.text = currentItem.phone

            binding.root.setOnClickListener{
                onClickItem(nim)
            }
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<MahasiswaData>(){
        override fun areItemsTheSame(
            oldItem: MahasiswaData,
            newItem: MahasiswaData
        ): Boolean {
            return oldItem.NIM == newItem.NIM
        }

        override fun areContentsTheSame(
            oldItem: MahasiswaData,
            newItem: MahasiswaData
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)
    var originalData = emptyList<MahasiswaData>()

    fun submitNewList(newData: MahasiswaDataArray?){
        originalData = newData as List<MahasiswaData>
        differ.submitList(newData)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            RecyclerviewitemStudentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getFilter(): Filter? {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults? {
                val filter = if(constraint == null || constraint.isEmpty()){
                    originalData
                } else {
                    differ.currentList.filter { it.name.lowercase().contains(constraint) }
                }
                return FilterResults().apply { values = filter }
            }

            override fun publishResults(
                constraint: CharSequence?,
                results: FilterResults?
            ) {
                differ.submitList(results?.values as List<MahasiswaData>)
            }

        }
    }
}