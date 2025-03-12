package com.proyecto.quickbracket.ui.dao

import androidx.room.PrimaryKey

data class Equipo (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val cantidadJugadores: Int

)