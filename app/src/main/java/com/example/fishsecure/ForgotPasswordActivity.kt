package com.example.fishsecure

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.fishsecure.ForgotPasswordContract
import com.example.fishsecure.ForgotPasswordPresenter

class ForgotPasswordActivity : Activity(), ForgotPasswordContract.View {

    private lateinit var etEmail: EditText
    private lateinit var btnResetPassword: Button
    private lateinit var tvBackToLogin: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var presenter: ForgotPasswordContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        // Initialize views
        etEmail = findViewById(R.id.etEmail)
        btnResetPassword = findViewById(R.id.btnResetPassword)
        tvBackToLogin = findViewById(R.id.tvBackToLogin)
        progressBar = findViewById(R.id.progressBar)

        presenter = ForgotPasswordPresenter(this)

        // Reset Password Button
        btnResetPassword.setOnClickListener {
            presenter.resetPassword(etEmail.text.toString().trim())
        }

        // Back to Login
        tvBackToLogin.setOnClickListener {
            finish()
        }
    }

    // --- MVP View methods ---
    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        // Navigate to ResetPasswordActivity
        val email = etEmail.text.toString().trim()
        val intent = Intent(this, ResetPasswordActivity::class.java)
        intent.putExtra("EMAIL", email)
        startActivity(intent)
        finish()
    }
}
