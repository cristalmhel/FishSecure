package com.example.fishsecure.presenter

import com.example.fishsecure.contract.ResetPasswordContract
import com.google.firebase.Firebase
import com.google.firebase.database.database

class ResetPasswordPresenter(private val view: ResetPasswordContract.View) :
    ResetPasswordContract.Presenter {

    override fun resetPassword(newPassword: String, confirmPassword: String, email: String) {
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            view.showMessage("Please fill in all fields")
            return
        }

        if (newPassword.length < 6) {
            view.showMessage("Password must be at least 6 characters")
            return
        }

        if (newPassword != confirmPassword) {
            view.showMessage("Passwords do not match")
            return
        }

        view.showLoading()

        // TODO: Replace this with actual backend reset logic

        val database = Firebase.database("https://fish-69805-default-rtdb.asia-southeast1.firebasedatabase.app")
        val usersRef = database.getReference("Users")

        usersRef.orderByChild("email").equalTo(email)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        userSnapshot.ref.child("password").setValue(newPassword)
                    }
                    view.hideLoading()
                    view.onPasswordResetSuccess()
                } else {
                    view.hideLoading()
                    view.showMessage("Email not found!")
                }
            }
            .addOnFailureListener { e ->
                view.hideLoading()
                view.showMessage("Error updating password")
            }
    }
}
