package com.stiproject.kelassti.fragment.dialog

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.stiproject.kelassti.R
import com.stiproject.kelassti.adapter.AcakKelompokAdapter
import com.stiproject.kelassti.databinding.FragmentAcakKelompokBinding
import com.stiproject.kelassti.util.acakKelompok

class DialogAcakKelompokFragment : DialogFragment() {

    private var _binding: FragmentAcakKelompokBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAcakKelompokBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: UI need to remade

        val dataMhs = resources.getStringArray(R.array.NamaMahasiswa)
        val acakKelompokAdapter = AcakKelompokAdapter()

        binding.recyclerViewAcakKelompok.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            adapter = acakKelompokAdapter
        }

        binding.buttonAcak.setOnClickListener{
            val jum = binding.editTextJumlahKelompok.text.toString()

            if (jum.isNotEmpty()){ // TODO: need exception handling
                val result = dataMhs.acakKelompok(jum.toInt())
                acakKelompokAdapter.differ.submitList(result)
                binding.recyclerViewAcakKelompok.visibility = View.VISIBLE
                binding.buttonSalinKelompok.visibility = View.VISIBLE
                binding.buttonSalinKelompok.setOnClickListener{

                    var h = ""
                    for (i in result.indices){
                        h += "====Kelompok ${i+1}====\n${result[i].joinToString("\n")}\n\n"
                    }

                    val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Kelompok Acak",h)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(context, "Berhasil Menyimpan ke clipboard", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}