package com.example.fishsecure

interface RegisterContract {
    interface View {
        fun showError(message: String)
        fun showSuccess(message: String)
    }

    interface Presenter {
        fun registerUser(
            firstName: String,
            lastName: String,
            middleName: String,
            username: String,
            email: String,
            password: String,
            confirmPassword: String
        )
    }
}
