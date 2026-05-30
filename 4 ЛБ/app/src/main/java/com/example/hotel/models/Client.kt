package com.example.hotel.models

data class Client(
    val id: Int = 0,
    val fullName: String,
    val passportNumber: String,
    val email: String = ""
)
