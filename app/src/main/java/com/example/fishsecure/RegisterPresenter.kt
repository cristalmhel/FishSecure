package com.example.fishsecure

class RegisterPresenter(private val view: RegisterContract.View) : RegisterContract.Presenter {

    override fun registerUser(
        firstName: String,
        lastName: String,
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

        view.showSuccess("Registered Successfully!")
    }
}
