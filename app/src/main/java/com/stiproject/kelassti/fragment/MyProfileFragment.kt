package com.stiproject.kelassti.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.stiproject.kelassti.R
import com.stiproject.kelassti.adapter.AcakKelompokAdapter
import com.stiproject.kelassti.databinding.FragmentMyProfileBinding
import com.stiproject.kelassti.fragment.dialog.AcakKelompokFragment
import com.stiproject.kelassti.viewmodel.UserViewModel

class MyProfileFragment : Fragment(R.layout.fragment_my_profile) {

    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMyProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.userData.observe(viewLifecycleOwner){
            if(it.userData != null){
                val userDat = it.userData

                binding.textViewUsernameMyProfile.text = userDat.name

                binding.buttonToAcak.setOnClickListener{
                    AcakKelompokFragment().show(parentFragmentManager,"acakkelompok")
                }

                binding.includeProdiSection.apply {
                    textViewLabelProfileInfo.text = "Prodi"
                    textViewValueProfileInfo.text = "Sistem & Teknologi Informasi"
                }
                binding.includeEmailSection.apply {
                    textViewLabelProfileInfo.text = "Email"
                    textViewValueProfileInfo.text = "loremipsum@example.com"
                }
                binding.includeNimSection.apply {
                    textViewLabelProfileInfo.text = "NIM"
                    textViewValueProfileInfo.text = userDat.usernameByNIM.toString()
                }
                binding.includeSemesterSection.apply {
                    textViewLabelProfileInfo.text = "Semester"
                    textViewValueProfileInfo.text = "02"
                }
                binding.includeBirthSection.apply {
                    textViewLabelProfileInfo.text = "Birth"
                    textViewValueProfileInfo.text = "01-01-1945"
                }
            }
        }

        Glide.with(this)
            .load(R.drawable.blank_profile_image)
            .error(R.drawable.baseline_money_off_24)
            .placeholder(R.drawable.blank_profile_image)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .circleCrop()
            .into(binding.imageViewProfilePic)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}