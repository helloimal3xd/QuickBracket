package com.proyecto.quickbracket.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.proyecto.quickbracket.ui.dao.AppDataBase
import com.proyecto.quickbracket.ui.dao.Torneo
import com.proyecto.quickbracket.ui.dao.TorneoRepository

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : TorneoRepository
    val torneosActivos : LiveData<List<Torneo>>

    init {
        val torneoDao = AppDataBase.getDataBase(application).torneoDao()
        repository = TorneoRepository(torneoDao)
        torneosActivos = repository.torneosActivos
    }
}
