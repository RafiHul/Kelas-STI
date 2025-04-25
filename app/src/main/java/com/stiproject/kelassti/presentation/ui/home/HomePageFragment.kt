package com.stiproject.kelassti.presentation.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.stiproject.kelassti.presentation.ui.kas.KasActivity
import com.stiproject.kelassti.R
import com.stiproject.kelassti.databinding.FragmentHomePageBinding
import com.stiproject.kelassti.presentation.adapter.TasksAdapter
import com.stiproject.kelassti.presentation.dialog.home.DialogAddOrUpdateTasksFragment
import com.stiproject.kelassti.presentation.ui.students.StudentsActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomePageFragment : Fragment() {

    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!

    private val homePageViewModel : HomePageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomePageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homePageViewModel.userData.observe(viewLifecycleOwner){
            if (it != null){
                binding.textViewUsernameHomePageLoading.viewLoadingTransparant.visibility = View.GONE
                binding.textViewUsernameHomePage.text = getString(R.string.name_homepage,it.userData?.name)
                binding.textViewUsernameHomePage.visibility = View.VISIBLE
                binding.progressBarMenuItemHomePage.visibility = View.GONE
                binding.constraintLayoutMenuItemHomePage.visibility = View.VISIBLE
            }
        }

        val tasksAdapter = TasksAdapter()
        binding.recyclerViewTasks.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
            adapter = tasksAdapter
        }

        activity?.let {
            lifecycleScope.launch {
                homePageViewModel.getTasksPage.collectLatest {
                    tasksAdapter.submitData(it)
                }
            }
        }

        binding.recyclerViewAnnoucement.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = tasksAdapter
        }

        binding.materialCardViewKas.setOnClickListener{
            val intent = Intent(context, KasActivity::class.java)
            startActivity(intent)
        }

        binding.imageButtonAddTasks.setOnClickListener{
            DialogAddOrUpdateTasksFragment().show(parentFragmentManager,"task")
        }
        
        binding.materialCardViewStudents.setOnClickListener{
            val intent = Intent(context, StudentsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}