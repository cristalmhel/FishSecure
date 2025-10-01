package com.example.fishsecure.model

class AuthModel {
    fun authenticate(username: String, password: String): Boolean {
        return username == "admin" && password == "1234"
    }
}
