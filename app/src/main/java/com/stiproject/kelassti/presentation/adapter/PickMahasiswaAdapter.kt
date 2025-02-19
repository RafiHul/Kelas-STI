package com.stiproject.kelassti.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.stiproject.kelassti.data.model.response.mahasiswa.MahasiswaData
import com.stiproject.kelassti.databinding.ItemPickMahasiswaBinding

class PickMahasiswaAdapter(val action: (String) -> Unit): RecyclerView.Adapter<PickMahasiswaAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding: ItemPickMahasiswaBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(currentItem: MahasiswaData){
            val nim = String.format(currentItem.NIM.toString())
            binding.textViewPickMahasiswaNim.text = nim
            binding.textViewPickMahasiswaName.text = currentItem.name

            binding.root.setOnClickListener{
                action(nim)
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