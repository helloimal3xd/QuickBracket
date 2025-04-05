package com.proyecto.quickbracket.ui.brackets

data class BracketNode(
    val equipo1: String,
    val equipo2: String,
    var ganador: String? = null,
    var siguienteNodo: BracketNode? = null
)