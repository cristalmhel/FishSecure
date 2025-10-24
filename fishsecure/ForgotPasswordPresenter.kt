package com.example.fishsecure

import android.util.Patterns

class ForgotPasswordPresenter(private val view: ForgotPasswordContract.View) :
    ForgotPasswordContract.Presenter {

    override fun resetPassword(email: String) {
        if (email.isEmpty()) {
            view.showError("Please enter your email")
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.showError("Invalid email address")
            return
        }

        view.showProgress()

        // TODO: Replace with actual backend call
        view.hideProgress()
        view.showSuccess("Check your email for reset instructions")
    }
}
