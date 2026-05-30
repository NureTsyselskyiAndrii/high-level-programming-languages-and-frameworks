package com.example.numguess

data class Player(
    val username: String,
    var wins: Int = 0,
    var bestScore: Int? = null,
    var currentSecret: Int = (1..100).random(),
    var attempts: Int = 0
)
