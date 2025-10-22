package com.example.fishsecure.contract

// import com.google.firebase.auth.FirebaseAuth  // if you're using Firebase
class SettingsPresenter(private val view: SettingsContract.View) : SettingsContract.Presenter {

    override fun onEditProfileClicked() {
        view.navigateToEditProfile()
    }

    override fun onTermsAndConditionsClicked() {
        view.navigateToTermsAndConditions()
    }

    override fun onPrivacyPolicyClicked() {
        view.navigateToPrivacyPolicy()
    }

    override fun onLogoutClicked() {
        view.showLogoutConfirmation()
    }

    override fun onLogoutConfirmed() {
        view.navigateToLogin()
    }
}



