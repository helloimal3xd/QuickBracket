package com.proyecto.quickbracket.ui.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "torneos")
data class TorneoEntity (

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val fecha: String,
    val estado: String

)