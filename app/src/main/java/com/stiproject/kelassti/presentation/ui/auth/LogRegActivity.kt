package com.stiproject.kelassti.presentation.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.stiproject.kelassti.databinding.ActivityLogRegBinding
import com.stiproject.kelassti.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogRegActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogRegBinding

    val logRegViewModel: LogRegViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLogRegBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logRegViewModel.getLoginJwtToken()

        // TODO: bugs when exception back to LogRegActivity 
        logRegViewModel.jwtToken.observe(this) {
            if(it.isNotEmpty() && it.isNotBlank()){
                val intent = Intent(this@LogRegActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}