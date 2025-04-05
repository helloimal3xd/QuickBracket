package com.proyecto.quickbracket.ui.dao

import androidx.lifecycle.LiveData

class TorneoRepository (private val torneoDao: TorneoDao){

    val torneosActivos : LiveData<List<Torneo>> = torneoDao.obtenerTorneosActivos()

    suspend fun insertarTorneo(torneo: Torneo){
        torneoDao.insertarTorneo(torneo)
    }
}