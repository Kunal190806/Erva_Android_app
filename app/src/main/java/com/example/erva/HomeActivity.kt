package com.example.erva

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.erva.databinding.ActivityHomeBinding
import com.google.android.material.navigation.NavigationBarView

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val homeFragment = HomeFragment()
    private val cameraFragment = CameraFragment()
    private val chatFragment = ChatFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the listener for the bottom navigation view
        binding.bottomNavigation.setOnItemSelectedListener(onNavigationItemSelected)

        // Load the default fragment
        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = R.id.navigation_home
        }
    }

    private val onNavigationItemSelected = NavigationBarView.OnItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                loadFragment(homeFragment)
                return@OnItemSelectedListener true
            }
            R.id.navigation_camera -> {
                loadFragment(cameraFragment)
                return@OnItemSelectedListener true
            }
            R.id.navigation_chat -> {
                loadFragment(chatFragment)
                return@OnItemSelectedListener true
            }
        }
        false
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
