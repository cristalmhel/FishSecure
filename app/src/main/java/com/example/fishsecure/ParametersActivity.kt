package com.example.fishsecure

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.fishsecure.contract.ParametersContract
import com.example.fishsecure.presenter.ParametersPresenter
import com.google.android.material.bottomnavigation.BottomNavigationView

class ParametersActivity : Activity(), ParametersContract.View {

    private lateinit var presenter: ParametersContract.Presenter
    private lateinit var bottomNavigation: BottomNavigationView

    private lateinit var aquariumTitle: TextView
    private lateinit var tempValue: TextView
    private lateinit var tdsValue: TextView
    private lateinit var ecValue: TextView
    private lateinit var commentTv: TextView
    private lateinit var statusValue: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parameters)

        presenter = ParametersPresenter(this)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val aquariumId = intent.getIntExtra("AQUARIUM_ID", -1)
        aquariumTitle = findViewById(R.id.aquariumTitle)
        tempValue = findViewById(R.id.tempValue)
        bottomNavigation = findViewById(R.id.bottomNavigation)
        tdsValue = findViewById(R.id.tdsValue)
        ecValue = findViewById(R.id.ecValue)
        commentTv = findViewById(R.id.commentTv)
        statusValue = findViewById(R.id.statusValue)

        presenter.loadParameters(aquariumId)

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> { navigateTo(HomeActivity::class.java); true }
                R.id.nav_aquarium -> { navigateTo(AquariumActivity::class.java); true }
                R.id.nav_notifications -> { navigateTo(NotificationActivity::class.java); true }
                R.id.nav_settings -> { navigateTo(SettingsActivity::class.java); true }
                else -> false
            }
        }

        btnBack.setOnClickListener {
            startActivity(Intent(this, AquariumActivity::class.java))
            finish()
        }
    }

    override fun showTemperature(value: String, isValid: Boolean) {
        tempValue.text = value
        tempValue.setTextColor(if (isValid) Color.parseColor("#CBC3E3") else Color.RED)
    }

    override fun showTds(value: String, isValid: Boolean) {
        tdsValue.text = value
        tdsValue.setTextColor(if (isValid) Color.parseColor("#CBC3E3") else Color.RED)
    }

    override fun showEc(value: String, isValid: Boolean) {
        ecValue.text = value
        ecValue.setTextColor(if (isValid) Color.parseColor("#CBC3E3") else Color.RED)
    }

    override fun showComment(value: String) {
        commentTv.text = value
    }

    override fun showTitle(value: String) {
        aquariumTitle.text = value
    }

    override fun showStatus(value: String) {
        statusValue.text = value
        statusValue.setTextColor(
            if (value == "Normal") Color.parseColor("#CBC3E3") else Color.RED
        )
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Helper
    private fun navigateTo(destination: Class<*>) {
        val intent = Intent(this, destination)
        startActivity(intent)
        finish()
    }
}
