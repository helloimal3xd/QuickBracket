package com.proyecto.quickbracket.ui.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TorneoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTorneo(torneo: TorneoEntity)

    @Query("SELECT * FROM torneos WHERE estado = 'activo'")
    fun obtenerTorneosActivos(): LiveData<List<TorneoEntity>>


}