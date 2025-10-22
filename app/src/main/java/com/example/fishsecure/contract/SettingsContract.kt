package com.example.fishsecure.contract

interface SettingsContract {
    interface View {
        fun showMessage(message: String)
        fun showLogoutConfirmation()
        fun navigateToLogin()
        fun navigateToEditProfile()
        fun navigateToTermsAndConditions()
        fun navigateToPrivacyPolicy()
    }

    interface Presenter {
        fun onEditProfileClicked()
        fun onTermsAndConditionsClicked()
        fun onPrivacyPolicyClicked()
        fun onLogoutClicked()
        fun onLogoutConfirmed()
    }
}
