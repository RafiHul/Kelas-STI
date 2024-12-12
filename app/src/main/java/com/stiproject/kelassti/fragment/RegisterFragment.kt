package com.stiproject.kelassti.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.stiproject.kelassti.R
import com.stiproject.kelassti.databinding.FragmentRegisterBinding
import com.stiproject.kelassti.model.request.RegisterRequest
import com.stiproject.kelassti.viewmodel.UserViewModel
import kotlin.getValue

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    val userViewModel: UserViewModel by activityViewModels()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        binding.buttonToLogin.setOnClickListener{
            navController.navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.buttonRegister.setOnClickListener{
            val name = binding.editTextTextNameRegister.text.toString()
            val username = binding.editTextTextPasswordRegister.text.toString()
            val password = binding.editTextTextPasswordRegister.text.toString()
            userViewModel.userRegister(
                RegisterRequest(username,password,name)
            ){
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}