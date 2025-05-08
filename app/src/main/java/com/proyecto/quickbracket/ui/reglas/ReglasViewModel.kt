package com.proyecto.quickbracket.ui.reglas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReglasViewModel : ViewModel() {

    private val _juegos = listOf("Selecciona un juego", "League of Legends", "Valorant", "FIFA")
    val juegos: List<String> get() = _juegos

    private val _detalles = MutableLiveData<List<String>>()
    val detalles: LiveData<List<String>> = _detalles

    fun seleccionarJuego(juego: String) {
        _detalles.value = when (juego) {
            "League of Legends" -> listOf("Competitivo", "ARAM", "Reglas generales")
            "Valorant" -> listOf("Ranked", "Deathmatch", "Custom")
            "FIFA" -> listOf("1v1", "Clásico", "Tiempo extra")
            else -> emptyList()
        }
    }
}
