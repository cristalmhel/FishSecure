package com.example.fishsecure

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.fishsecure.contract.LogoutContract
import com.example.fishsecure.presenter.LogoutPresenter

class LogoutActivity : AppCompatActivity(), LogoutContract.View {

    private lateinit var presenter: LogoutContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logout)

        presenter = LogoutPresenter(this)

        val btnCancel = findViewById<Button>(R.id.btnCancel)
        val btnLogout = findViewById<Button>(R.id.btnConfirmLogout)

        btnCancel.setOnClickListener { presenter.onCancelClicked() }
        btnLogout.setOnClickListener { presenter.onLogoutClicked() }
    }

    override fun navigateToLogin() {
        UserManager.currentUser = null
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun closeLogoutScreen() {
        finish()
    }
}
