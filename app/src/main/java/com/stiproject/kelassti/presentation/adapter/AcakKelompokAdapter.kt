package com.stiproject.kelassti.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.stiproject.kelassti.databinding.RecyclerviewitemAcakkelompokBinding

class AcakKelompokAdapter(): RecyclerView.Adapter<AcakKelompokAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding: RecyclerviewitemAcakkelompokBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(currentItem: MutableList<String>,pos: Int){

            binding.textViewNamaKelompok.text = "Kelompok ${pos+1}"
            val namaAnggota = currentItem.joinToString("\n")
            binding.textViewAnggotaKelompok.text = namaAnggota

        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<MutableList<String>>(){
        override fun areItemsTheSame(
            oldItem: MutableList<String>,
            newItem: MutableList<String>
        ): Boolean {
            return oldItem[0] == newItem[0]
        }

        override fun areContentsTheSame(
            oldItem: MutableList<String>,
            newItem: MutableList<String>
        ): Boolean {
            return oldItem[1] == newItem[1]
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            RecyclerviewitemAcakkelompokBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        ))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        holder.bind(differ.currentList[position], position)
    }
}