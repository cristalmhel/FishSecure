package com.example.fishsecure

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class PrivacyPolicyActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var btnBack: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy)


        bottomNavigation = findViewById(R.id.bottomNavigation)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener {
            navigateTo(SettingsActivity::class.java)
        }

        onBackPressedDispatcher.addCallback(this) {
            navigateTo(SettingsActivity::class.java)
        }

        // Bottom navigation
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> { navigateTo(HomeActivity::class.java); true }
                R.id.nav_aquarium -> { navigateTo(AquariumActivity::class.java); true }
                R.id.nav_notifications -> { navigateTo(NotificationActivity::class.java); true }
                R.id.nav_settings -> { navigateTo(SettingsActivity::class.java); true }
                else -> false
            }
        }
    }

    private fun navigateTo(destination: Class<*>) {
        val intent = Intent(this, destination)
        startActivity(intent)
        finish()
    }
}
