package com.stiproject.kelassti.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.stiproject.kelassti.R
import com.stiproject.kelassti.databinding.RecyclerviewitemTransaksikasBinding
import com.stiproject.kelassti.model.response.transaksi.TransaksiData

class TransaksiAdapter(val context: Context): RecyclerView.Adapter<TransaksiAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding: RecyclerviewitemTransaksikasBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(currentItem: TransaksiData){

            binding.textViewNameMahasiswaKas.text = currentItem.NIM_mahasiswa.toString()
            binding.textViewDeksripsi.text = currentItem.deskripsi
            binding.textViewTanggalKas.text = currentItem.tanggal

            if(currentItem.type == "pemasukan"){
                binding.textViewPemasukanAtauPengeluaranKas.setTextColor(context.getColor(R.color.pemasukan_kas))
                binding.textViewPemasukanAtauPengeluaranKas.text = context.getString(R.string.rupiah_pemasukan_kas,currentItem.nominal.toString())
            } else {
                binding.textViewPemasukanAtauPengeluaranKas.setTextColor(context.getColor(R.color.pengeluaran_kas))
                binding.textViewPemasukanAtauPengeluaranKas.text = context.getString(R.string.rupiah_pengeluaran_kas,currentItem.nominal.toString())
            }

        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<TransaksiData>() {
        override fun areItemsTheSame(
            oldItem: TransaksiData,
            newItem: TransaksiData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: TransaksiData,
            newItem: TransaksiData
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this@TransaksiAdapter,differCallback)

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

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        val currentItem = differ.currentList[position]
        holder.bind(currentItem)
    }
}