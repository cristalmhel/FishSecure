package com.example.fishsecure.model

data class Aquarium(
    val id: Int,
    var name: String,
    var user: String = "",
    var ec: Float = 0f,
    var temp: Float = 0f,
    var tds: Float = 0f,
    var status: String = "Normal",
    var comment: String = ""
)