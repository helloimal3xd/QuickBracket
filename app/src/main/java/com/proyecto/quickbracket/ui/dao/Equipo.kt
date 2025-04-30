package com.proyecto.quickbracket.ui.dao

data class Equipo(
    val nombre: String,
    val cantidadJugadores: Int,
    val jugadores: List<String>
)
