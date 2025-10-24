package com.example.fishsecure.presenter

import com.example.fishsecure.contract.ResetPasswordContract

class ResetPasswordPresenter(private val view: ResetPasswordContract.View) :
    ResetPasswordContract.Presenter {

    override fun resetPassword(newPassword: String, confirmPassword: String) {
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            view.showMessage("Please fill in all fields")
            return
        }

        if (newPassword.length < 6) {
            view.showMessage("Password must be at least 6 characters")
            return
        }

        if (newPassword != confirmPassword) {
            view.showMessage("Passwords do not match")
            return
        }

        view.showLoading()

        // TODO: Replace this with actual backend reset logic
        view.hideLoading()
        view.onPasswordResetSuccess()
    }
}
