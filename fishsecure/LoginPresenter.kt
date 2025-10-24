package com.example.fishsecure

import com.example.fishsecure.LoginView

class LoginPresenter(private val view: LoginView) {

    fun login(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            view.onLoginError("Username and password cannot be empty")
            return
        }

        if (username == "admin" && password == "1234") {
            view.onLoginSuccess("Login successful! Welcome, $username.")
        } else {
            view.onLoginError("Invalid username or password")
        }
    }
}
