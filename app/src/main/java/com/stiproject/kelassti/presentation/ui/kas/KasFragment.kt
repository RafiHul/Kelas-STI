package com.stiproject.kelassti.presentation.ui.kas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.stiproject.kelassti.R
import com.stiproject.kelassti.databinding.FragmentKasBinding
import com.stiproject.kelassti.presentation.dialog.DialogAuthorisasiFragment
import com.stiproject.kelassti.presentation.adapter.TransaksiAdapter
import com.stiproject.kelassti.presentation.dialog.DialogAddTransaksiFragment
import com.stiproject.kelassti.util.convertToRupiahFormat
import com.stiproject.kelassti.util.handleToastApiResult
import com.stiproject.kelassti.presentation.ui.kas.TransaksiViewModel
import com.stiproject.kelassti.presentation.ui.profile.UserViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class KasFragment : Fragment(R.layout.fragment_kas) {

    private var _binding: FragmentKasBinding? = null
    private val binding get() = _binding!!

    private val transaksiViewModel: TransaksiViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()

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

        transaksiViewModel.getTransaksi {
            handleToastApiResult(context, it)
        }

        transaksiViewModel.totalTransaksiKas.observe(viewLifecycleOwner){
            binding.textViewKasTotal.text = getString(R.string.rupiah_format,it.convertToRupiahFormat())
            binding.textViewKasTotalLoading.viewLoadingTransparant.visibility = View.INVISIBLE
            binding.textViewKasTotal.visibility = View.VISIBLE
        }

        transaksiViewModel.totalPemasukanKas.observe(viewLifecycleOwner){
            binding.textViewPemasukkan.text = getString(R.string.rupiah_pemasukan_kas,it.convertToRupiahFormat())
            binding.textViewPemasukanLoading.viewLoadingTransparant.visibility = View.INVISIBLE
            binding.textViewPemasukkan.visibility = View.VISIBLE
        }

        transaksiViewModel.totalPengeluaranKas.observe(viewLifecycleOwner){
            binding.textViewPengeluaranKas.text = getString(R.string.rupiah_pengeluaran_kas,it.convertToRupiahFormat())
            binding.textViewPengeluaranKasLoading.viewLoadingTransparant.visibility = View.INVISIBLE
            binding.textViewPengeluaranKas.visibility = View.VISIBLE
        }

        val transaksiAdapter = TransaksiAdapter(requireContext()) {
            DialogAddTransaksiFragment.Companion.newInstance(it)
                .show(parentFragmentManager, "Transaksi Edit")
        }

        userViewModel.userData.observe(viewLifecycleOwner){ user ->
            binding.imageButtonAddTransaksi.setOnClickListener{
                if(user.userData?.role != "admin"){
                    DialogAuthorisasiFragment().show(parentFragmentManager,"auth")
                } else {
                    DialogAddTransaksiFragment().show(parentFragmentManager, "Transaksi New")
                }
            }
        }

        binding.recyclerViewTransaksi.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = transaksiAdapter
        }

        activity?.let {
            lifecycleScope.launch {
                transaksiViewModel.getTransaksiPage.collectLatest {
                    transaksiAdapter.submitData(it)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}