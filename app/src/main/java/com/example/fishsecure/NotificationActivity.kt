package com.example.fishsecure

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import com.google.android.material.bottomnavigation.BottomNavigationView

class NotificationActivity : Activity(), NotificationContract.View {

    private lateinit var btnBack: ImageView
    private lateinit var bottomNavigation: BottomNavigationView

    private lateinit var presenter: NotificationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        // Initialize views
        btnBack = findViewById(R.id.btnBack)
        bottomNavigation = findViewById(R.id.bottomNavigation)

        // Initialize presenter
        presenter = NotificationPresenter(this)

        // Setup click listeners
        setupClickListeners()

        // Load notifications
        presenter.loadNotifications()
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_parameters -> {
                    startActivity(Intent(this, ParametersActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_notifications -> {
                    // Already on notifications
                    true
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

        // Set the current item as selected
        bottomNavigation.selectedItemId = R.id.nav_notifications
    }

    // === Contract View Implementation ===
    override fun displayNotifications() {
        // Notifications are already displayed in the static layout
        // This method can be used for future dynamic updates
    }

    override fun showError(message: String) {
        // Could show toast or dialog for errors
    }

    override fun showLoading() {
        // Could show progress indicator if needed
    }

    override fun hideLoading() {
        // Hide progress indicator
    }
}