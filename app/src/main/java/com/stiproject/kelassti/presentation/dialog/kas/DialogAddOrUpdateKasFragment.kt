package com.stiproject.kelassti.presentation.dialog.kas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.SearchView
import android.widget.Spinner
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stiproject.kelassti.R
import com.stiproject.kelassti.databinding.FragmentDialogAddTransaksiBinding
import com.stiproject.kelassti.data.model.request.KasRequest
import com.stiproject.kelassti.data.model.response.mahasiswa.MahasiswaDataArray
import com.stiproject.kelassti.presentation.adapter.PickMahasiswaAdapter
import com.stiproject.kelassti.util.ApiResult
import kotlinx.coroutines.launch
import kotlin.toString

class DialogAddOrUpdateKasFragment : DialogFragment(R.layout.fragment_dialog_add_transaksi), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentDialogAddTransaksiBinding? = null
    private val binding get() = _binding!!

    private val dialogKasViewModel: DialogKasViewModel by activityViewModels()

    private lateinit var bindingNimMahasiswaKasInput: SearchView
    private lateinit var bindingNominalKasInput: EditText
    private lateinit var bindingDeskripsiKasInput: EditText
    private lateinit var typeSpinner: Spinner
    private lateinit var pickMahasiswaAdapter: PickMahasiswaAdapter
    private lateinit var recyclerViewPickMahasiswa: RecyclerView

    private var typeKasInput: String? = null
    private var MahasiswaNameAndNimSelectedFromSearch = Pair<String?,Int?>(null,null) //<Name,Nim>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDialogAddTransaksiBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments

        val kasId = args?.getInt("kasId")

        dialogKasViewModel.initialize(kasId)

        typeSpinner = binding.spinnerTypeKas
        typeSpinner.onItemSelectedListener = this

        bindingNimMahasiswaKasInput = binding.searchViewNIMMahasiswaKas

        pickMahasiswaAdapter = PickMahasiswaAdapter{
            MahasiswaNameAndNimSelectedFromSearch = it
            bindingNimMahasiswaKasInput.setQuery(it.first,false)
        }

        recyclerViewPickMahasiswa = binding.recyclerViewPickMahasiswa.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            adapter = pickMahasiswaAdapter
        }

        lifecycleScope.launch{
            dialogKasViewModel.searchMahasiswaByNameResult.collect{
                when(it){
                    is ApiResult.Failed -> {
                        recyclerViewPickMahasiswa.visibility = View.GONE
//                            Toast.makeText(context, it.messageFailed.message, Toast.LENGTH_SHORT).show()
                    }
                    is ApiResult.Success<*> -> {
                        pickMahasiswaAdapter.differ.submitList(it.data as MahasiswaDataArray)
                        recyclerViewPickMahasiswa.visibility = View.VISIBLE
                    }
                }
            }
        }

        bindingNominalKasInput = binding.editTextTextNominalKas
        bindingDeskripsiKasInput = binding.editTextDeskripsiKas

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.kas_type,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            typeSpinner.adapter = it
        }

        dialogKasViewModel.dialogKasState.observe(viewLifecycleOwner){
            when(it){

                is DialogKasViewModel.DialogKasState.ApiFailed -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    dismiss()
                }
                is DialogKasViewModel.DialogKasState.ValidationDataFailed -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }

                is DialogKasViewModel.DialogKasState.ApiPostSuccess -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    parentFragmentManager.setFragmentResult("status", bundleOf("isAddOrUpdate" to true))
                    dismiss()
                }

                is DialogKasViewModel.DialogKasState.ApiGetSuccess -> {
                    val data = it.data

                    if(data != null){
                        binding.textViewTambahkanKas.text = "Edit"

                        MahasiswaNameAndNimSelectedFromSearch = Pair(data.nama, data.NIM_mahasiswa)

                        binding.imageView.visibility = View.GONE
                        bindingNimMahasiswaKasInput.visibility = View.GONE

                        bindingNimMahasiswaKasInput.setQuery(
                            MahasiswaNameAndNimSelectedFromSearch.first,
                            true
                        )

                        bindingDeskripsiKasInput.setText(data.deskripsi)
                        bindingNominalKasInput.setText(String.format(data.nominal.toString()))

                        binding.textViewTambahkanKas.setOnClickListener {
                            if (!dialogKasViewModel.isUserAdmin()) {
                                Toast.makeText(context, "Anda Bukan Admin", Toast.LENGTH_SHORT).show()
                                dismiss()
                            }

                            dialogKasViewModel.updateKasById(data.id, createKasRequest())
                        }
                    } else {
                        binding.textViewTambahkanKas.text = "Simpan"

                        binding.textViewTambahkanKas.setOnClickListener {

                            if(!dialogKasViewModel.isUserAdmin()){
                                Toast.makeText(context, "Anda Bukan Admin", Toast.LENGTH_SHORT).show()
                                return@setOnClickListener
                            }

                            if(MahasiswaNameAndNimSelectedFromSearch.second == null){
                                Toast.makeText(context, "Harap Mengisi Data Nama Mahasiswa Dengan benar", Toast.LENGTH_SHORT).show()
                                return@setOnClickListener
                            }

                            if(bindingNimMahasiswaKasInput.query.toString() != MahasiswaNameAndNimSelectedFromSearch.first){
                                Toast.makeText(context, "Harap menulisakan nama dengan lengkap dan benar", Toast.LENGTH_SHORT).show()
                                return@setOnClickListener
                            }

                            dialogKasViewModel.addKasData(createKasRequest())
                        }
                    }

                }
                DialogKasViewModel.DialogKasState.Idle -> {}
            }
        }

        binding.textViewBatalKas.setOnClickListener{
            dismiss()
        }

        bindingNimMahasiswaKasInput.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.recyclerViewPickMahasiswa.visibility = View.GONE
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                dialogKasViewModel.searchMahasiswaByName(newText.toString())
                return false
            }
        })


        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.window?.setIcon(R.drawable.baseline_dehaze_24)
    }

    private fun createKasRequest(): KasRequest {

        return KasRequest(
            MahasiswaNameAndNimSelectedFromSearch.second ?: 0,
            bindingDeskripsiKasInput.text.toString(),
            bindingNominalKasInput.text.toString().let { if(it.isEmpty()) 0 else it.toInt() },
            typeKasInput ?: ""
        )

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        typeKasInput = parent?.getItemAtPosition(position).toString().lowercase()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    companion object{
        fun newInstance(kasId: Int): DialogAddOrUpdateKasFragment {
            val args = Bundle()
            val fragment = DialogAddOrUpdateKasFragment()
            args.putInt("kasId",kasId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dialogKasViewModel.setAddOrUpdateStateBackToIdle()
        _binding = null
    }
}