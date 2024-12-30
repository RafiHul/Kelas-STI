package com.stiproject.kelassti

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.stiproject.kelassti.databinding.ActivityLogRegBinding
import com.stiproject.kelassti.util.DataStoreUtil
import com.stiproject.kelassti.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LogRegActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogRegBinding

    val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLogRegBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch{
            DataStoreUtil.getLoginToken(application).collect{
                if (it.isNotEmpty()) {
                    val intent = Intent(application, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}