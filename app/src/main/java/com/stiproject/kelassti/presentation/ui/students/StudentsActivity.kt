package com.stiproject.kelassti.presentation.ui.students

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.stiproject.kelassti.R
import com.stiproject.kelassti.databinding.ActivityStudentsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityStudentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myToolbar = binding.toolbarStudents
        setSupportActionBar(myToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.imageViewBackStudentsActivity.setOnClickListener{
            finish()
        }
    }
}