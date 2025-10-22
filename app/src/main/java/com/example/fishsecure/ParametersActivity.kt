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

    private lateinit var tempValue: TextView
    private lateinit var phValue: TextView
    private lateinit var oxygenValue: TextView
    private lateinit var statusValue: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parameters)

        presenter = ParametersPresenter(this)

        val btnBack = findViewById<ImageView>(R.id.backButton)
        tempValue = findViewById(R.id.tempValue)
        phValue = findViewById(R.id.phValue)
        oxygenValue = findViewById(R.id.oxygenValue)
        statusValue = findViewById(R.id.statusValue)

        presenter.loadParameters(aquariumId = 1)

//        bottomNavigation.setOnItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.nav_home -> { navigateTo(HomeActivity::class.java); true }
//                R.id.nav_parameters -> { true }
//                R.id.nav_notifications -> { navigateTo(NotificationActivity::class.java); true }
//                R.id.nav_settings -> { navigateTo(SettingsActivity::class.java); true }
//                else -> false
//            }
//        }

        btnBack.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    override fun showTemperature(value: String, isValid: Boolean) {
        tempValue.text = value
        tempValue.setTextColor(if (isValid) Color.parseColor("#3F1C91") else Color.RED)
    }

    override fun showPH(value: String, isValid: Boolean) {
        phValue.text = value
        phValue.setTextColor(if (isValid) Color.parseColor("#3F1C91") else Color.RED)
    }

    override fun showOxygen(value: String, isValid: Boolean) {
        oxygenValue.text = value
        oxygenValue.setTextColor(if (isValid) Color.parseColor("#3F1C91") else Color.RED)
    }

    override fun showStatus(value: String) {
        statusValue.text = value
        statusValue.setTextColor(
            if (value == "Normal") Color.parseColor("#3F1C91") else Color.RED
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
