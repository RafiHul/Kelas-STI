package com.stiproject.kelassti.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stiproject.kelassti.data.model.response.TasksResponse
import com.stiproject.kelassti.databinding.RecyclerviewitemTasksBinding

class TasksAdapter(val item: MutableList<TasksResponse>): RecyclerView.Adapter<TasksAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: RecyclerviewitemTasksBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(currentItem: TasksResponse){
            binding.textViewTime.text = currentItem.time
            binding.textViewNamaDosen.text = currentItem.namaDosen
            binding.textViewTugas.text = currentItem.tugas
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            RecyclerviewitemTasksBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        holder.bind(item[position])
    }
}