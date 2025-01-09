package com.stiproject.kelassti.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.stiproject.kelassti.R
import com.stiproject.kelassti.databinding.FragmentProfileBinding
import com.stiproject.kelassti.model.response.mahasiswa.MahasiswaData
import com.stiproject.kelassti.viewmodel.UserViewModel

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get () = _binding!!

    private val userViewModel: UserViewModel by activityViewModels()

    private lateinit var userData: MahasiswaData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        userViewModel.userData.observe(viewLifecycleOwner){
//            binding.textViewNameProfile.text = requireContext().getString(R.string.name_profile,it?.name)
//            binding.textViewNimProfile.text = requireContext().getString(R.string.nim_profile,it?.usernameByNIM.toString())
//            binding.textViewRoleProfile.text = requireContext().getString(R.string.role_profile,it?.role)
//        }
    }
}