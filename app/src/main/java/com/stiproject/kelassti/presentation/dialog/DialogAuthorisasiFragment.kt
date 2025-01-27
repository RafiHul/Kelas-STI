package com.stiproject.kelassti.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.stiproject.kelassti.R
import com.stiproject.kelassti.databinding.FragmentDialogAuthorisasiBinding

class DialogAuthorisasiFragment : DialogFragment(R.layout.fragment_dialog_authorisasi) {

    private var _binding: FragmentDialogAuthorisasiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDialogAuthorisasiBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageButtonBackDialogAuth.setOnClickListener{
            dismiss()
        }

        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            650,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}