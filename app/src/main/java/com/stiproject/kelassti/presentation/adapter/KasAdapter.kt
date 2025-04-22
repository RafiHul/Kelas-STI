package com.stiproject.kelassti.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.stiproject.kelassti.R
import com.stiproject.kelassti.data.model.response.kas.KasData
import com.stiproject.kelassti.databinding.RecyclerviewitemTransaksikasBinding
import com.stiproject.kelassti.util.convertToRupiahFormat

class KasAdapter(val context: Context, val actionClickCard: (Int) -> Unit):
    PagingDataAdapter<KasData, KasAdapter.MyViewHolder>(
        object : DiffUtil.ItemCallback<KasData>() {
            override fun areItemsTheSame(
                oldItem: KasData,
                newItem: KasData
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: KasData,
                newItem: KasData
            ): Boolean {
                return oldItem == newItem
            }
        }
    ) {
    inner class MyViewHolder(val binding: RecyclerviewitemTransaksikasBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(currentItem: KasData){

            binding.textViewNameMahasiswaKas.text = currentItem.nama
            binding.textViewDeksripsi.text = currentItem.deskripsi
            binding.textViewTanggalKas.text = currentItem.tanggal

            if(currentItem.type == "pemasukan"){
                binding.textViewPemasukanAtauPengeluaranKas.setTextColor(context.getColor(R.color.pemasukan_kas))
                binding.textViewPemasukanAtauPengeluaranKas.text = context.getString(R.string.rupiah_pemasukan_kas,currentItem.nominal.convertToRupiahFormat())
            } else {
                binding.textViewPemasukanAtauPengeluaranKas.setTextColor(context.getColor(R.color.pengeluaran_kas))
                binding.textViewPemasukanAtauPengeluaranKas.text = context.getString(R.string.rupiah_pengeluaran_kas,currentItem.nominal.convertToRupiahFormat())
            }

            binding.root.setOnClickListener{
                actionClickCard(currentItem.id)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            RecyclerviewitemTransaksikasBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        val currentItem = getItem(position)
        holder.bind(currentItem!!)
    }
}