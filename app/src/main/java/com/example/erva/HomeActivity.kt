package com.example.erva

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.erva.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val homeFragment = HomeFragment()
    private val cameraFragment = CameraFragment()
    private var activeFragment: Fragment = homeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup fragments
        supportFragmentManager.beginTransaction().apply {
            add(R.id.fragment_container, cameraFragment, "2").hide(cameraFragment)
            add(R.id.fragment_container, homeFragment, "1")
        }.commit()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit()
                    activeFragment = homeFragment
                    true
                }
                R.id.navigation_camera -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment).show(cameraFragment).commit()
                    activeFragment = cameraFragment
                    true
                }
                R.id.navigation_chat -> {
                    startActivity(Intent(this, ChatbotActivity::class.java))
                    // Do not mark as true, so the tab doesn't get selected
                    false
                }
                else -> false
            }
        }

        // Set the default fragment
        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = R.id.navigation_home
        }
    }
}
