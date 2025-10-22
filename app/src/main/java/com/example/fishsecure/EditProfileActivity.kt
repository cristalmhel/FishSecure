package com.example.fishsecure

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.fishsecure.contract.EditProfileContract
import com.example.fishsecure.presenter.EditProfilePresenter
import com.google.android.material.bottomnavigation.BottomNavigationView

class EditProfileActivity : AppCompatActivity(), EditProfileContract.View {

    private lateinit var presenter: EditProfileContract.Presenter
    private lateinit var firstNameField: EditText
    private lateinit var lastNameField: EditText
    private lateinit var emailField: EditText
    private lateinit var saveBtn: Button
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Init views
        firstNameField = findViewById(R.id.editFirstName)
        lastNameField = findViewById(R.id.editLastName)
        emailField = findViewById(R.id.editEmail)
        saveBtn = findViewById(R.id.save_button)
        bottomNavigation = findViewById(R.id.bottomNavigation)

        presenter = EditProfilePresenter(this)

        // Load existing user data
        presenter.loadUserData()

        saveBtn.setOnClickListener {
            presenter.onSaveClicked(
                firstNameField.text.toString(),
                lastNameField.text.toString(),
                emailField.text.toString()
            )
        }

        onBackPressedDispatcher.addCallback(this) {
            navigateBackToSettings()
        }

        // Bottom navigation
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> { navigateTo(HomeActivity::class.java); true }
                R.id.nav_parameters -> { /* TODO */ true }
                R.id.nav_notifications -> { navigateTo(NotificationActivity::class.java); true }
                R.id.nav_settings -> { navigateTo(SettingsActivity::class.java); true }
                else -> false
            }
        }
    }

    // MVP methods
    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun fillUserData(firstName: String, lastName: String, email: String) {
        firstNameField.setText(firstName)
        lastNameField.setText(lastName)
        emailField.setText(email)
    }

    override fun navigateBackToSettings() {
        navigateTo(SettingsActivity::class.java)
    }

    // Helper
    private fun navigateTo(destination: Class<*>) {
        val intent = Intent(this, destination)
        startActivity(intent)
    }
}
