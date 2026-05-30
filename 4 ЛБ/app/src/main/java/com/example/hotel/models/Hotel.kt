package com.example.hotel.models

data class Hotel(
    val id: Int = 0,
    val name: String,
    val address: String,
    val stars: Int = 3,
    val phone: String = ""
)
