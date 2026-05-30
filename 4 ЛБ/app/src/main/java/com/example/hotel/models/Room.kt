package com.example.hotel.models

data class Room(
    val id: Int = 0,
    val hotelId: Int,
    val roomNumber: String,
    val type: String,
    val pricePerNight: Double,
    val isAvailable: Boolean = true
)
