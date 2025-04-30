package com.proyecto.quickbracket.ui.brackets

data class Bracket(
    val equipo1: String,
    val equipo2: String,
    var puntosEquipo1: Int = 0,
    var puntosEquipo2: Int = 0,
    val ronda: String,
    val id: String,
    var ganador: String = ""
)