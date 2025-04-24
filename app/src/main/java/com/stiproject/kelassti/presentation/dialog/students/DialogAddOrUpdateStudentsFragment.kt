package com.stiproject.kelassti.presentation.dialog.students

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.stiproject.kelassti.data.model.request.AddOrUpdateMahasiswaRequest
import com.stiproject.kelassti.databinding.FragmentDialogAddOrUpdateStudentsBinding

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

        val args = arguments
        Log.d("ceknim", args?.getString("mahasiswaNim").toString())
        dialogStudentsViewModel.initelize(args?.getString("mahasiswaNim"))

        dialogStudentsViewModel.dialogStudentsState.observe(viewLifecycleOwner){
            when(it){
                is DialogStudentsViewModel.DialogStudentsState.ApiAddOrUpdateStudentsFailed -> Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                is DialogStudentsViewModel.DialogStudentsState.ValidationDataFailed -> Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                is DialogStudentsViewModel.DialogStudentsState.ApiAddOrUpdateStudentsSuccess -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    parentFragmentManager.setFragmentResult("statusAddStudents", bundleOf("isSuccess" to true))
                    dismiss()
                }
                is DialogStudentsViewModel.DialogStudentsState.GetStudentsFailed -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    dismiss()
                }
                is DialogStudentsViewModel.DialogStudentsState.GetStudentsSuccess -> {
                    val mahasiswaData = it.mahasiswaData
                    if(mahasiswaData != null){
                        binding.editTextNIMMahasiswa.setText(String.format(mahasiswaData.NIM.toString()))
                        binding.editTextNIMMahasiswa.visibility = View.GONE
                        binding.imageViewAddNimIcon.visibility = View.GONE
                        binding.textViewDeleteStudents.visibility = View.VISIBLE
                        binding.textViewTambahkanStudents.text = "Update"

                        binding.editTextNomorTeleponStudents.setText(mahasiswaData.phone)
                        binding.editTextNameStudents.setText(mahasiswaData.name)

                        binding.textViewTambahkanStudents.setOnClickListener{
                            dialogStudentsViewModel.updateStudents(createAddOrUpdateRequest())
                        }

                        binding.textViewDeleteStudents.setOnClickListener{
                            dialogStudentsViewModel.deleteStudents(mahasiswaData.NIM.toString())
                        }

                    } else {
                        binding.textViewTambahkanStudents.text = "Tambahkan"

                        binding.textViewTambahkanStudents.setOnClickListener{
                            dialogStudentsViewModel.addStudents(createAddOrUpdateRequest())
                        }
                    }
                }

                DialogStudentsViewModel.DialogStudentsState.Idle -> {}
            }
        }

        binding.textViewBatalAddStudents.setOnClickListener{
            dismiss()
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

    fun createAddOrUpdateRequest() = AddOrUpdateMahasiswaRequest(
        binding.editTextNIMMahasiswa.text.toString().let { if(it.isEmpty()) 0 else it.toInt() },
        binding.editTextNameStudents.text.toString(),
        binding.editTextNomorTeleponStudents.text.toString()
    )

    companion object{
        fun newInstance(mahasiswaNim: String): DialogAddOrUpdateStudentsFragment {
            val args = Bundle()
            val fragment = DialogAddOrUpdateStudentsFragment()
            args.putString("mahasiswaNim",mahasiswaNim)
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