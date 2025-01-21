package com.stiproject.kelassti

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.stiproject.kelassti.databinding.ActivityKasBinding
import com.stiproject.kelassti.viewmodel.TransaksiViewModel
import com.stiproject.kelassti.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class KasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKasBinding

    val transaksiViewModel: TransaksiViewModel by viewModels()
    val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityKasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myToolbar = binding.toolbarKas
        setSupportActionBar(myToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.imageViewBackKasActivity.setOnClickListener{
            finish()
        }
    }
}