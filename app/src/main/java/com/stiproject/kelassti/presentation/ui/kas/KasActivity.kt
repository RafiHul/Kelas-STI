package com.stiproject.kelassti.presentation.ui.kas

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.stiproject.kelassti.databinding.ActivityKasBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKasBinding

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