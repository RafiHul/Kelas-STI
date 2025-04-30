package com.stiproject.kelassti.presentation.ui.kas

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.filter
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.stiproject.kelassti.R
import com.stiproject.kelassti.data.model.response.kas.KasData
import com.stiproject.kelassti.databinding.FragmentKasBinding
import com.stiproject.kelassti.presentation.dialog.DialogAuthorisasiFragment
import com.stiproject.kelassti.presentation.adapter.KasAdapter
import com.stiproject.kelassti.presentation.dialog.kas.DialogAddOrUpdateKasFragment
import com.stiproject.kelassti.util.convertToRupiahFormat
import com.stiproject.kelassti.util.handleToastApiResult
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.ArrayList

class KasFragment : Fragment(R.layout.fragment_kas) {

    private var _binding: FragmentKasBinding? = null
    private val binding get() = _binding!!

    private val kasViewModel: KasViewModel by activityViewModels()

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

        kasViewModel.kasSummary.observe(viewLifecycleOwner){
            binding.textViewKasTotal.text = getString(R.string.rupiah_format,it.total.convertToRupiahFormat())
            binding.textViewKasTotalLoading.viewLoadingTransparant.visibility = View.INVISIBLE
            binding.textViewKasTotal.visibility = View.VISIBLE

            binding.textViewPemasukkan.text = getString(R.string.rupiah_pemasukan_kas,it.pemasukan.convertToRupiahFormat())
            binding.textViewPemasukanLoading.viewLoadingTransparant.visibility = View.INVISIBLE
            binding.textViewPemasukkan.visibility = View.VISIBLE

            binding.textViewPengeluaranKas.text = getString(R.string.rupiah_pengeluaran_kas,it.pengeluaran.convertToRupiahFormat())
            binding.textViewPengeluaranKasLoading.viewLoadingTransparant.visibility = View.INVISIBLE
            binding.textViewPengeluaranKas.visibility = View.VISIBLE
        }

        val kasAdapter = KasAdapter(requireContext()) {
            DialogAddOrUpdateKasFragment.Companion.newInstance(it)
                .show(parentFragmentManager, "Transaksi Edit")
        }

        binding.imageButtonAddTransaksi.setOnClickListener{
            if(kasViewModel.isUserAdmin()){
                DialogAddOrUpdateKasFragment().show(parentFragmentManager, "Transaksi New")
            } else {
                DialogAuthorisasiFragment().show(parentFragmentManager,"auth")
            }
        }

        binding.recyclerViewTransaksi.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = kasAdapter
        }

        activity?.let {
            lifecycleScope.launch {
                kasViewModel.getKasPage.collectLatest {
                    kasAdapter.submitData(it)
                }
            }
        }

        binding.searchViewKasByName.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                kasViewModel.fetchKasDataByName(query.toString())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText == null || newText.isEmpty()){
                    kasViewModel.fetchAllKasData()
                }
                return false
            }

        })

        parentFragmentManager.setFragmentResultListener("status", viewLifecycleOwner){ _, result ->

            val isAddOrUpdate = result.getBoolean("isAddOrUpdate")

            if(isAddOrUpdate){
                kasViewModel.fetchAllKasData()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}