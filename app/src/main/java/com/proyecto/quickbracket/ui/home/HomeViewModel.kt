package com.proyecto.quickbracket.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.quickbracket.ui.dao.Torneo

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _torneosActivos = MutableLiveData<List<Torneo>>()
    val torneosActivos: LiveData<List<Torneo>> = _torneosActivos

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    init {
        cargarTorneos()
    }

    fun cargarTorneos() {
        val usuarioActual = auth.currentUser

        db.collection("torneos")
            .whereEqualTo("creadoPor", usuarioActual?.uid)
            .get()
            .addOnSuccessListener { documentos ->
                val listaTorneos = documentos.map { doc ->
                    Torneo(
                        id = doc.id,
                        nombre = doc.getString("nombre") ?: "",
                        juego = doc.getString("juego") ?: "",
                        cantidadJugadores = doc.getLong("cantidadJugadores")?.toInt() ?: 0,
                        fecha = (doc.getLong("fecha") ?: 0) ?: 0L,
                        estado = doc.getString("estado") ?: "",
                        creadoPor = doc.getString("creadoPor") ?: "",
                        equipos = emptyList()
                    )
                }

                _torneosActivos.value = listaTorneos
            }
            .addOnFailureListener { e ->
                _torneosActivos.value = emptyList()
            }
    }

}
