package com.example.fishsecure.view

interface LoginView {
    fun onLoginSuccess(message: String)
    fun onLoginError(error: String)
}
