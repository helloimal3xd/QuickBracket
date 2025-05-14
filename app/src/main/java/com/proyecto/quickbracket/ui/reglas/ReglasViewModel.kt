package com.proyecto.quickbracket.ui.reglas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReglasViewModel : ViewModel() {

    private val _juegos = listOf("Selecciona un juego","League of Legends", "Valorant", "FIFA24", "Rocket League", "Counter-Strike 2")
    val juegos: List<String> get() = _juegos

    private val _detalles = MutableLiveData<List<String>>()
    val detalles: LiveData<List<String>> = _detalles

    fun seleccionarJuego(juego: String) {
        _detalles.value = when (juego) {
            "League of Legends" -> listOf("ARAM","Custom")
            "Valorant" -> listOf("swiftplay", "Deathmatch", "1v1", "2v2", "5v5")
            "FIFA24" -> listOf("1v1",)
            "Rocket League" -> listOf("1v1", "2v2", "3v3")
            "Counter-Strike 2" -> listOf("Competitivo", "ARAM", "Reglas generales")
            else -> emptyList()
        }
    }
}
