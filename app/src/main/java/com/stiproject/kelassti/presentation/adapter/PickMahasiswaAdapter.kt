package com.stiproject.kelassti.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.stiproject.kelassti.data.model.response.mahasiswa.MahasiswaData
import com.stiproject.kelassti.databinding.ItemPickMahasiswaBinding

class PickMahasiswaAdapter(val action: (Int) -> Unit): RecyclerView.Adapter<PickMahasiswaAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding: ItemPickMahasiswaBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(currentItem: MahasiswaData){
            binding.textViewPickMahasiswaNim.text = String.format(currentItem.usernameByNIM.toString())
            binding.textViewPickMahasiswaName.text = currentItem.name

            binding.root.setOnClickListener{
                action(currentItem.usernameByNIM)
            }
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<MahasiswaData>(){
        override fun areItemsTheSame(
            oldItem: MahasiswaData,
            newItem: MahasiswaData
        ): Boolean {
            return newItem.name == oldItem.name
        }

        override fun areContentsTheSame(
            oldItem: MahasiswaData,
            newItem: MahasiswaData
        ): Boolean {
            return newItem == oldItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(ItemPickMahasiswaBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ){
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}