package com.example.fishsecure

import com.example.fishsecure.model.User
import com.google.firebase.Firebase
import com.google.firebase.database.database

class RegisterPresenter(private val view: RegisterContract.View) : RegisterContract.Presenter {

    override fun registerUser(
        firstName: String,
        lastName: String,
        middleName: String,
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() ||
            email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
        ) {
            view.showError("Please fill all fields")
            return
        }

        if (password != confirmPassword) {
            view.showError("Passwords do not match")
            return
        }
        val database = Firebase.database("https://fish-69805-default-rtdb.asia-southeast1.firebasedatabase.app")
        val myRef = database.getReference("Users")
        val user = User(firstName, lastName, middleName, username, email, password)
        myRef.child(username)
            .setValue(user)
            .addOnSuccessListener {
                view.showSuccess("User Registered Successfully!")
            }
            .addOnFailureListener { error ->
                view.showError("Error:" + error.message)
            }
    }
}
