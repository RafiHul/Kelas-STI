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
import com.stiproject.kelassti.databinding.FragmentLoginBinding
import com.stiproject.kelassti.model.request.LoginRequest
import com.stiproject.kelassti.util.handleToastApiResult
import com.stiproject.kelassti.viewmodel.UserViewModel
import kotlin.getValue

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    val userViewModel: UserViewModel by activityViewModels()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        binding.buttonToRegister.setOnClickListener{
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.buttonLogin.setOnClickListener{
            val username = binding.editTextTextUsernameLogin.text.toString()
            val password = binding.editTextTextPasswordLogin.text.toString()

            if (username.isEmpty() || password.isEmpty()){
                Toast.makeText(context, "Masukkan username dan password dengan benar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val usernamenum = username.toInt()
                userViewModel.userLogin(requireContext(),LoginRequest(usernamenum,password)){
                    handleToastApiResult(context, it)
                }
            } catch (e: NumberFormatException){
                Toast.makeText(context, "Masukkan Angka NIM", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}