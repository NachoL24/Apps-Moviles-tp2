package com.example.juego_adivinar_numero_aleatorio

data class Attempt(
    var id: Int,
    var points: Int,
    var fails: Int,
    var date: String
)
