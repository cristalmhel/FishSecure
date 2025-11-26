package com.example.fishsecure.model

data class Aquarium(
    val id: Int? = null,
    var name: String =  "",
    var description: String = "",
    var status: String = "Active",
    var comment: String = "",
    var user: String = "",
    var ec: Float = 0f,
    var temp: Float = 0f,
    var tds: Float = 0f
)