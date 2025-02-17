package com.proyecto.quickbracket.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.proyecto.quickbracket.ui.dao.AppDataBase
import com.proyecto.quickbracket.ui.dao.TorneoEntity
import com.proyecto.quickbracket.ui.dao.TorneoRepository
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : TorneoRepository
    val torneosActivos : LiveData<List<TorneoEntity>>

    init {
        val torneoDao = AppDataBase.getDataBase(application).torneoDao()
        repository = TorneoRepository(torneoDao)
        torneosActivos = repository.torneosActivos
    }
}
