package com.proyecto.quickbracket.ui.dao

import androidx.lifecycle.LiveData

class TorneoRepository (private val torneoDao: TorneoDao){

    val torneosActivos : LiveData<List<TorneoEntity>> = torneoDao.obtenerTorneosActivos()

    suspend fun insertarTorneo(torneo: TorneoEntity){
        torneoDao.insertarTorneo(torneo)
    }
}