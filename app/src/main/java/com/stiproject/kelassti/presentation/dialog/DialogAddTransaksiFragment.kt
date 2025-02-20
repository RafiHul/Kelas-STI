package com.stiproject.kelassti.presentation.dialog

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
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stiproject.kelassti.R
import com.stiproject.kelassti.databinding.FragmentDialogAddTransaksiBinding
import com.stiproject.kelassti.data.model.request.KasRequest
import com.stiproject.kelassti.presentation.adapter.PickMahasiswaAdapter
import com.stiproject.kelassti.util.ApiResult
import com.stiproject.kelassti.util.handleToastApiResult
import com.stiproject.kelassti.presentation.ui.kas.TransaksiViewModel
import com.stiproject.kelassti.presentation.ui.profile.UserViewModel
import kotlin.toString

class DialogAddTransaksiFragment : DialogFragment(R.layout.fragment_dialog_add_transaksi), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentDialogAddTransaksiBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels()
    private val transaksiViewModel: TransaksiViewModel by activityViewModels()

    private lateinit var bindingNimMahasiswaKasInput: SearchView
    private lateinit var bindingNominalKasInput: EditText
    private lateinit var bindingDeskripsiKasInput: EditText
    private lateinit var typeSpinner: Spinner
    private lateinit var pickMahasiswaAdapter: PickMahasiswaAdapter
    private lateinit var recyclerViewPickMahasiswa: RecyclerView

    private var kasId: Int? = null
    private var typeKasInput: String? = null
    private var MahasiswaNameAndNimSelected = Pair<String?,Int?>(null,null) //<Name,Nim>

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
        val userData = userViewModel.userData.value
        val userJwtBearer = userViewModel.getJwtBearer()
        val args = arguments
        kasId = args?.getInt("kasId")

        typeSpinner = binding.spinnerTypeKas
        typeSpinner.onItemSelectedListener = this

        bindingNimMahasiswaKasInput = binding.searchViewNIMMahasiswaKas

        pickMahasiswaAdapter = PickMahasiswaAdapter{
            MahasiswaNameAndNimSelected = it
            bindingNimMahasiswaKasInput.setQuery(it.first,false)
        }

        recyclerViewPickMahasiswa = binding.recyclerViewPickMahasiswa.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            adapter = pickMahasiswaAdapter
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


        kasId?.let { kasId ->
            binding.textViewTambahkanKas.text = "Edit"

            transaksiViewModel.getTransaksiById(kasId){ result ->
                when(result){
                    is ApiResult.Failed -> {
                        Toast.makeText(context, "Terjadi Kesalahan saat mengambil data", Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                    is ApiResult.Success -> {
                        val data = result.messageSuccess!!

                        MahasiswaNameAndNimSelected = Pair(data.nama,data.NIM_mahasiswa)

                        binding.imageView.visibility = View.GONE
                        bindingNimMahasiswaKasInput.visibility = View.GONE

                        bindingNimMahasiswaKasInput.setQuery(MahasiswaNameAndNimSelected.first,true)
                        bindingDeskripsiKasInput.setText(data.deskripsi)
                        bindingNominalKasInput.setText(String.format(data.nominal.toString()))

                        binding.textViewTambahkanKas.setOnClickListener {

                            if(userData?.userData?.role != "admin"){
                                Toast.makeText(context, "Anda Bukan Admin", Toast.LENGTH_SHORT).show()
                                return@setOnClickListener
                            }

                            val kasRequest = getKasRequest()

                            if (kasRequest == null) {
                                Toast.makeText(context, "Tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show()
                                return@setOnClickListener
                            }

                            transaksiViewModel.updateTransaksiById(data.id, userJwtBearer, kasRequest) {
                                handleToastApiResult(context,it)
                                dismiss()
                            }
                        }
                    }
                }
            }
        } ?: run {
            binding.textViewTambahkanKas.text = "Simpan"
            binding.textViewTambahkanKas.setOnClickListener {

                if(userData?.userData?.role != "admin"){
                    Toast.makeText(context, "Anda Bukan Admin", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if(MahasiswaNameAndNimSelected.second == null){
                    Toast.makeText(context, "Harap Mengisi Data Nama Mahasiswa Dengan benar", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val kasRequest = getKasRequest()

                if (kasRequest == null){
                    Toast.makeText(context, "Tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                transaksiViewModel.addTransaksiKas(userJwtBearer, kasRequest) {
                    handleToastApiResult(context, it)
                    dismiss()
                }
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
                userViewModel.getMahasiswaByName(newText.toString()){ apiResult ->
                    when(apiResult){
                        is ApiResult.Failed -> {
                            recyclerViewPickMahasiswa.visibility = View.GONE
//                            Toast.makeText(context, apiResult.messageFailed.message, Toast.LENGTH_SHORT).show()
                        }
                        is ApiResult.Success -> {
                            pickMahasiswaAdapter.differ.submitList(apiResult.messageSuccess.data)
                            recyclerViewPickMahasiswa.visibility = View.VISIBLE
                        }
                    }
                }

                return false
            }

        })


        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.window?.setIcon(R.drawable.baseline_dehaze_24)
    }

    private fun getKasRequest(): KasRequest? {
        val nimMahasiswaKasInput = MahasiswaNameAndNimSelected.second ?: return null //if user not select mahasiswa
        val deskripsiKasInput = bindingDeskripsiKasInput.text.toString()
        val nominalKasInput = bindingNominalKasInput.text.toString()

        if (nominalKasInput.isNotEmpty() && deskripsiKasInput.isNotEmpty() && typeKasInput != null) {
            val kasRequest = KasRequest(
                nimMahasiswaKasInput,
                deskripsiKasInput,
                nominalKasInput.toInt(),
                typeKasInput.toString().lowercase()
            )
            return kasRequest
        }
        return null
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            650
        )
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        typeKasInput = parent?.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    companion object{
        fun newInstance(kasId: Int): DialogAddTransaksiFragment {
            val args = Bundle()
            val fragment = DialogAddTransaksiFragment()
            args.putInt("kasId",kasId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}