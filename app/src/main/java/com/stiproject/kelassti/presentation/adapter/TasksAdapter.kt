package com.stiproject.kelassti.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.stiproject.kelassti.data.model.response.tasks.TasksData
import com.stiproject.kelassti.databinding.RecyclerviewitemTasksBinding

class TasksAdapter(val onClick: (Int) -> Unit):
    PagingDataAdapter<TasksData, TasksAdapter.MyViewHolder>(
    object : DiffUtil.ItemCallback<TasksData>() {
        override fun areItemsTheSame(
            oldItem: TasksData,
            newItem: TasksData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: TasksData,
            newItem: TasksData
        ): Boolean {
            return oldItem == newItem
        }
    }
    ){
        inner class MyViewHolder(val binding: RecyclerviewitemTasksBinding): RecyclerView.ViewHolder(binding.root){
            fun bind(currentItem: TasksData){
                binding.textViewTime.text = currentItem.deadline.split("T")[0]
                binding.textViewTugas.text = currentItem.title
                binding.textViewNamaDosen.text = currentItem.dosen.name

                binding.root.setOnClickListener{
                    onClick(currentItem.id)
                }
            }
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            RecyclerviewitemTasksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position)!!)
    }

}