package com.example.fishsecure

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class TermsAndConditionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)

        // 🔙 Back arrow → go to Settings
        val backArrow = findViewById<Button>(R.id.back_arrow)
        backArrow.setOnClickListener {
            navigateTo(SettingsActivity::class.java)
        }

        // ⬅️ System back button → also go to Settings
        onBackPressedDispatcher.addCallback(this) {
            navigateTo(SettingsActivity::class.java)
        }

        // 🔽 Bottom navigation
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    navigateTo(HomeActivity::class.java)
                    true
                }
                R.id.nav_aquarium -> {
                    // TODO: Replace with ParametersActivity when available
                    true
                }
                R.id.nav_notifications -> {
                    navigateTo(NotificationActivity::class.java)
                    true
                }
                R.id.nav_settings -> {
                    navigateTo(SettingsActivity::class.java)
                    true
                }
                else -> false
            }
        }

        // ✅ Highlight Settings tab
        bottomNavigation.selectedItemId = R.id.nav_settings
    }

    // 🔁 Helper function to reduce duplicate code
    private fun navigateTo(destination: Class<*>) {
        val intent = Intent(this, destination)
        startActivity(intent)
        finish()
    }
}
