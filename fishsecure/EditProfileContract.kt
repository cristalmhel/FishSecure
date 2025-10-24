package com.example.fishsecure

interface EditProfileContract {
    interface View {
        fun showMessage(message: String)
        fun fillUserData(firstName: String, lastName: String, username: String, email: String)
        fun navigateBackToSettings()
    }

    interface Presenter {
        fun loadUserData()
        fun onSaveClicked(firstName: String, lastName: String, username: String, email: String)
    }
}