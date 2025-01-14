package com.stiproject.kelassti.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.stiproject.kelassti.R
import com.stiproject.kelassti.adapter.TransaksiAdapter
import com.stiproject.kelassti.databinding.FragmentKasBinding
import com.stiproject.kelassti.viewmodel.TransaksiViewModel

class KasFragment : Fragment(R.layout.fragment_kas) {

    private var _binding: FragmentKasBinding? = null
    private val binding get() = _binding!!

    private val transaksiViewModel: TransaksiViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentKasBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transaksiViewModel.getTransaksiKas {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        transaksiViewModel.totalTransaksiKas.observe(viewLifecycleOwner){
            binding.textViewKasTotal.text = getString(R.string.rupiah_format,it.toString())
        }

        transaksiViewModel.totalPemasukanKas.observe(viewLifecycleOwner){
            binding.textViewPemasukkan.text = getString(R.string.rupiah_pemasukan_kas,it.toString())
        }

        transaksiViewModel.totalPengeluaranKas.observe(viewLifecycleOwner){
            binding.textViewPengeluaranKas.text = getString(R.string.rupiah_pengeluaran_kas,it.toString())
        }
        // ini jadiin 1 aja sama pair atau map biar gk ribet

        val transaksiAdapter = TransaksiAdapter(requireContext()){
            DialogAddTransaksiFragment.newInstance(it).show(parentFragmentManager,"Transaksi Edit")
        }

        binding.imageButtonAddTransaksi.setOnClickListener{
            DialogAddTransaksiFragment().show(parentFragmentManager,"Transaksi New")
        }

        binding.recyclerViewTransaksi.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            adapter = transaksiAdapter
        }

        activity?.let {
            transaksiViewModel.transaksiKas.observe(viewLifecycleOwner){
                transaksiAdapter.differ.submitList(it)
            }
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}