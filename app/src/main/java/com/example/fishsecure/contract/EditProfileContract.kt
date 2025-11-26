package com.example.fishsecure.contract

interface EditProfileContract {
    interface View {
        fun showMessage(message: String)
        fun fillUserData(firstName: String, middleName: String, lastName: String, email: String)
        fun navigateBackToSettings()

        fun showError(message: String)
    }

    interface Presenter {
        fun loadUserData()
        fun onSaveClicked(firstName: String, middleName: String, lastName: String, email: String)
    }
}
