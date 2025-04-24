package com.stiproject.kelassti.presentation.ui.students

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.stiproject.kelassti.databinding.FragmentStudentsBinding
import com.stiproject.kelassti.presentation.adapter.StudentsAdapter
import com.stiproject.kelassti.presentation.dialog.students.DialogAddOrUpdateStudentsFragment
import kotlinx.coroutines.launch

class StudentsFragment : Fragment() {

    private var _binding: FragmentStudentsBinding? = null
    private val binding get() = _binding!!

    private val studentsViewModel: StudentsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStudentsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val studentsAdapter = StudentsAdapter{
            DialogAddOrUpdateStudentsFragment.newInstance(it).show(parentFragmentManager, "updateStudents")
        }

        binding.recyclerViewStudents.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = studentsAdapter
        }

        studentsViewModel.getAllMahasiswa()

        studentsViewModel.mahasiswaState.observe(viewLifecycleOwner){
            when(it){
                is StudentsViewModel.MahasiswaState.FailedGetData -> Toast.makeText(
                    context,
                    it.message,
                    Toast.LENGTH_SHORT
                ).show()

                is StudentsViewModel.MahasiswaState.SuccessGetData -> {
                    lifecycleScope.launch{
                        binding.textViewTotalMahasiswa.text = "${it.data?.size} Orang"
                        studentsAdapter.submitNewList(it.data)
                    }
                }
                StudentsViewModel.MahasiswaState.Idle -> {}
            }
        }

        parentFragmentManager.setFragmentResultListener("statusAddStudents", viewLifecycleOwner){_, bundle ->
            val result = bundle.getBoolean("isSuccess")
            if (result){
                studentsViewModel.getAllMahasiswa()
            }
        }

        binding.imageButtonAddStudents.setOnClickListener{
            DialogAddOrUpdateStudentsFragment().show(parentFragmentManager, "addStudents")
        }

        binding.searchViewStudents.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                studentsAdapter.filter?.filter(newText)
                return false
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}