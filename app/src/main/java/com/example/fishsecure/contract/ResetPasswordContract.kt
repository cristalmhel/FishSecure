package com.example.fishsecure.contract

interface ResetPasswordContract {
    interface View {
        fun showLoading()
        fun hideLoading()
        fun showMessage(message: String)
        fun onPasswordResetSuccess()
    }

    interface Presenter {
        fun resetPassword(newPassword: String, confirmPassword: String, email: String)
    }
}
