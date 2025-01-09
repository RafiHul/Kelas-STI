package com.stiproject.kelassti

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.stiproject.kelassti.databinding.ActivityKasBinding

class KasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityKasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myToolbar = binding.toolbarKas
        myToolbar.title
        setSupportActionBar(myToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }
}