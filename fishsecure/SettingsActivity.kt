package com.example.fishsecure

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.example.fishsecure.settings.SettingsContract
import com.example.fishsecure.settings.SettingsPresenter
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsActivity : Activity(), SettingsContract.View {

    private lateinit var btnBack: ImageView
    private lateinit var layoutEditProfile: LinearLayout
    private lateinit var layoutTermsCondition: LinearLayout
    private lateinit var layoutPrivacyPolicy: LinearLayout
    private lateinit var layoutLogout: LinearLayout
    private lateinit var bottomNavigation: BottomNavigationView

    private lateinit var presenter: SettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        btnBack = findViewById(R.id.btnBack)
        layoutEditProfile = findViewById(R.id.layoutEditProfile)
        layoutTermsCondition = findViewById(R.id.layoutTermsCondition)
        layoutPrivacyPolicy = findViewById(R.id.layoutPrivacyPolicy)
        layoutLogout = findViewById(R.id.layoutLogout)
        bottomNavigation = findViewById(R.id.bottomNavigation)

        presenter = SettingsPresenter(this)

        btnBack.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        layoutEditProfile.setOnClickListener {
            presenter.onEditProfileClicked()
        }

        layoutTermsCondition.setOnClickListener {
            presenter.onTermsAndConditionsClicked()
        }

        layoutPrivacyPolicy.setOnClickListener {
            presenter.onPrivacyPolicyClicked()
        }

        layoutLogout.setOnClickListener {
            presenter.onLogoutClicked()
        }

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_aquarium -> {
                    // TODO: Navigate to parameters activity when created
                    true
                }
                R.id.nav_notifications -> {
                    startActivity(Intent(this, NotificationActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_settings -> {
                    // Already on settings
                    true
                }
                else -> false
            }
        }

        // Set the current item as selected
        bottomNavigation.selectedItemId = R.id.nav_settings
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showLogoutConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                presenter.onLogoutConfirmed()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun navigateToEditProfile() {
        val intent = Intent(this, EditProfileActivity::class.java)
        startActivity(intent)
    }

    override fun navigateToTermsAndConditions() {
        // TODO: Create and navigate to TermsAndConditionsActivity
        showMessage("Terms and Conditions - Coming Soon")
    }

    override fun navigateToPrivacyPolicy() {
        // TODO: Create and navigate to PrivacyPolicyActivity
        showMessage("Privacy Policy - Coming Soon")
    }
}