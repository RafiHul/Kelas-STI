package com.stiproject.kelassti

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.stiproject.kelassti.databinding.ActivityMainBinding
import com.stiproject.kelassti.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val myDrawerLayout = binding.mainDrawerLayout
        val myToolbar = binding.myToolbar
        setContentView(binding.root)
        setSupportActionBar(myToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val toggle = ActionBarDrawerToggle(this,myDrawerLayout,myToolbar,R.string.drawer_open,R.string.drawer_close)

        toggle.isDrawerIndicatorEnabled = true
        myDrawerLayout.addDrawerListener(toggle)

        toggle.syncState()
        toggle.setHomeAsUpIndicator(R.drawable.baseline_attach_money_24)

        lifecycleScope.launch{
            userViewModel.getUsersByJwt(application){
                if (it == "401"){
                    val intent = Intent(application, LogRegActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                Toast.makeText(application, it, Toast.LENGTH_SHORT).show()
                //tidak ada error handling ketika jwt expired
            }
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        val navController = navHostFragment.navController
        val popupMenu = PopupMenu(this,null)
        popupMenu.inflate(R.menu.bottom_nav_menu)
        binding.navigationBottomMain.setupWithNavController(popupMenu.menu,navController)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}