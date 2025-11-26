package com.example.fishsecure.presenter

import com.example.fishsecure.UserManager
import com.example.fishsecure.contract.EditProfileContract
import com.example.fishsecure.model.User
import com.google.firebase.Firebase
import com.google.firebase.database.database

class EditProfilePresenter(private val view: EditProfileContract.View) : EditProfileContract.Presenter {

    val user = UserManager.currentUser
    override fun loadUserData() {
        val firstName = user?.firstName ?: ""
        val middleName = user?.middleName ?: ""
        val lastName = user?.lastName ?: ""
        val email = user?.email ?: ""
        view.fillUserData(firstName, middleName, lastName, email)
    }

    override fun onSaveClicked(firstName: String, middleName: String, lastName: String, email: String) {
        if (firstName.isBlank() || lastName.isBlank() || email.isBlank()) {
            view.showMessage("All fields are required")
            return
        }

        val database = Firebase.database("https://fish-69805-default-rtdb.asia-southeast1.firebasedatabase.app")
        val usersRef = database.getReference("Users")

        // Create a User object
        val username = user?.username ?: ""
        val oldEmail = user?.email ?: ""
        val user = User(
            firstName = firstName,
            lastName = lastName,
            middleName = middleName,
            username = username,
            email = email,
            password = user?.password ?: ""
        )

        // Check if username exists first
        usersRef.child(username).get()
            .addOnSuccessListener { usernameSnapshot ->
                if (usernameSnapshot.exists()) {
                    // Then check if email exists
                    usersRef.orderByChild("email").equalTo(email)
                        .get()
                        .addOnSuccessListener { emailSnapshot ->
                            // Case 1: No user has this email → OK
                            if (!emailSnapshot.exists()) {
                                usersRef.child(username).setValue(user)
                                    .addOnSuccessListener {
                                        UserManager.currentUser = user
                                        view.showMessage("Profile updated successfully")
                                        view.navigateBackToSettings()
                                    }
                                    .addOnFailureListener { e ->
                                        view.showError("Failed to register: ${e.message}")
                                    }
                                return@addOnSuccessListener
                            }

                            // Case 2 or 3: Email exists → check who owns it
                            val ownerUid = emailSnapshot.children.first().key   // username of the owner
                            val existingEmail = emailSnapshot.children.first()
                                .child("email")
                                .getValue(String::class.java)

                            // If the owner is the same user (email unchanged)
                            if (existingEmail == oldEmail) {
                                usersRef.child(username).setValue(user)
                                    .addOnSuccessListener {
                                        UserManager.currentUser = user
                                        view.showMessage("Profile updated successfully")
                                        view.navigateBackToSettings()
                                    }
                                    .addOnFailureListener { e ->
                                        view.showError("Failed to register: ${e.message}")
                                    }
                                return@addOnSuccessListener
                            }

                            // Otherwise, email belongs to another user → BLOCK
                            view.showError("Email already registered from other user")
                        }
                } else {
                    view.showError("User not exist")
                }
            }
            .addOnFailureListener { e ->
                view.showError("Failed to register:" + e.message)
            }
    }
}
