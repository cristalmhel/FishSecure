package com.example.fishsecure.presenter

import android.util.Patterns
import com.example.fishsecure.contract.ForgotPasswordContract
import com.google.firebase.Firebase
import com.google.firebase.database.database

class ForgotPasswordPresenter(private val view: ForgotPasswordContract.View) :
    ForgotPasswordContract.Presenter {

    override fun resetPassword(email: String) {
        if (email.isEmpty()) {
            view.showError("Please enter your email")
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            view.showError("Invalid email address")
            return
        }

        view.showProgress()

        // TODO: actual backend call firebase
        val database = Firebase.database("https://fish-69805-default-rtdb.asia-southeast1.firebasedatabase.app")
        val usersRef = database.getReference("Users")


        // Check if the email already exists
        usersRef.orderByChild("email").equalTo(email)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    // Email already exists
                    view.hideProgress()
                    view.showSuccess("Check your email for reset instructions")
                } else {
                    // Email does not exist, safe to add new user
                    view.hideProgress()
                    view.showError("Email already registered")
                }
            }
            .addOnFailureListener { e ->
                view.hideProgress()
                view.showError("Error checking email" + e.message.toString())
            }
    }
}
