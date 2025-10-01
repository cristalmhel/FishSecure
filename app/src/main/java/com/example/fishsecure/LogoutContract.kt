package com.example.fishsecure

interface LogoutContract {
    interface View {
        fun navigateToLogin()
        fun closeLogoutScreen()
    }

    interface Presenter {
        fun onCancelClicked()
        fun onLogoutClicked()
    }
}
