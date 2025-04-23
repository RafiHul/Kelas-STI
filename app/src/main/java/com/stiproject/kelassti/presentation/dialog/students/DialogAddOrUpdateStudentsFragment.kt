package com.stiproject.kelassti.presentation.dialog.students

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.stiproject.kelassti.R
import com.stiproject.kelassti.data.model.request.AddMahasiswaRequest
import com.stiproject.kelassti.databinding.FragmentDialogAddOrUpdateStudentsBinding
import com.stiproject.kelassti.presentation.dialog.kas.DialogAddOrUpdateKasFragment

class DialogAddOrUpdateStudentsFragment : DialogFragment() {

    private var _binding: FragmentDialogAddOrUpdateStudentsBinding? = null
    private val binding get() = _binding!!

    private val dialogStudentsViewModel: DialogStudentsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDialogAddOrUpdateStudentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialogStudentsViewModel.dialogStudentsState.observe(viewLifecycleOwner){
            when(it){
                is DialogStudentsViewModel.DialogStudentsState.ApiAddStudentsFailed -> Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                is DialogStudentsViewModel.DialogStudentsState.ValidationDataFailed -> Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                is DialogStudentsViewModel.DialogStudentsState.ApiAddStudentsSuccess -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    parentFragmentManager.setFragmentResult("statusAddStudents", bundleOf("isSuccess" to true))
                    dismiss()
                }

                is DialogStudentsViewModel.DialogStudentsState.ApiGetTasksSuccess -> {}
                DialogStudentsViewModel.DialogStudentsState.Idle -> {}
            }
        }

        binding.textViewTambahkanStudents.setOnClickListener{
            dialogStudentsViewModel.addStudents(AddMahasiswaRequest(
                binding.editTextNIMMahasiswa.text.toString().let { if(it.isEmpty()) 0 else it.toInt() },
                binding.editTextNameStudents.text.toString(),
                binding.editTextNomorTeleponStudents.text.toString()
            ))
        }

        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
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
        dialogStudentsViewModel.setStudentsStateBackToIdle()
        _binding = null
    }
}