package com.example.fishsecure

interface LoginView {
    fun onLoginSuccess(message: String)
    fun onLoginError(error: String)
}
