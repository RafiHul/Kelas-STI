package com.stiproject.kelassti.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.stiproject.kelassti.KasActivity
import com.stiproject.kelassti.R
import com.stiproject.kelassti.adapter.TasksAdapter
import com.stiproject.kelassti.databinding.FragmentHomePageBinding
import com.stiproject.kelassti.util.TempData
import com.stiproject.kelassti.viewmodel.UserViewModel

class HomePageFragment : Fragment() {

    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!

    private val userViewModel : UserViewModel by activityViewModels()

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

        userViewModel.userData.observe(viewLifecycleOwner){
            binding.textViewUsernameHomePage.text = getString(R.string.name_homepage,it.userData?.name ?: "null")
        }

        val tasksAdapter = TasksAdapter(TempData.tasksTempItem)
        binding.recyclerViewTasks.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
            adapter = tasksAdapter
        }

        binding.recyclerViewAnnoucement.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
            adapter = tasksAdapter
        }

        binding.materialCardView3.setOnClickListener{
            val intent = Intent(context,KasActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}