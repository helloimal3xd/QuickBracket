package com.proyecto.quickbracket.ui.dao

data class Torneo(
    val id: String = "",
    val nombre: String = "",
    val fecha: Long = 0L,
    val estado: String = "",
    val equipos: List<String> = emptyList(),
    val juego: String = "",
    val cantidadJugadores: Int = 0,
    val creadoPor: String = ""
)
