package com.example.fishsecure

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.fishsecure.contract.ResetPasswordContract
import com.example.fishsecure.presenter.ResetPasswordPresenter

class ResetPasswordActivity : Activity(), ResetPasswordContract.View {

    private lateinit var presenter: ResetPasswordPresenter
    private lateinit var newPasswordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var resetPasswordButton: Button
    private lateinit var backToLoginText: TextView
    private lateinit var progressBar: ProgressBar
    private var userEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        presenter = ResetPasswordPresenter(this)

        // Get email passed from ForgotPasswordActivity
        userEmail = intent.getStringExtra("EMAIL")

        initViews()
        setupListeners()
    }

    private fun initViews() {
        newPasswordInput = findViewById(R.id.etNewPassword)
        confirmPasswordInput = findViewById(R.id.etConfirmPassword)
        resetPasswordButton = findViewById(R.id.btnResetPassword)
        backToLoginText = findViewById(R.id.tvBackToLogin)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupListeners() {
        resetPasswordButton.setOnClickListener {
            presenter.resetPassword(
                newPasswordInput.text.toString().trim(),
                confirmPasswordInput.text.toString().trim(),
                userEmail.toString()
            )
        }

        backToLoginText.setOnClickListener {
            finish()
        }
    }

    // === Contract View Implementation ===
    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onPasswordResetSuccess() {
        Toast.makeText(this, "Password reset successful!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
