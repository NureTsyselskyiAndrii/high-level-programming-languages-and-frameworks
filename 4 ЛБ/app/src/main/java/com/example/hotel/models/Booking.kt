package com.example.hotel.models

data class Booking(
    val id: Int = 0,
    val clientId: Int,
    val roomId: Int,
    val checkInDate: String,
    val checkOutDate: String,
    val totalPrice: Double
)
