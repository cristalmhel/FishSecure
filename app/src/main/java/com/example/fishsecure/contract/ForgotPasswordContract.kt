package com.example.fishsecure.contract

interface ForgotPasswordContract {
    interface View {
        fun showProgress()
        fun hideProgress()
        fun showError(message: String)
        fun showSuccess(message: String)
    }

    interface Presenter {
        fun resetPassword(email: String)
    }
}
