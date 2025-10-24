package com.example.fishsecure.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.fishsecure.EditProfileActivity
import com.example.fishsecure.LoginActivity
import com.example.fishsecure.PrivacyPolicyActivity
import com.example.fishsecure.TermsAndConditionsActivity

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



