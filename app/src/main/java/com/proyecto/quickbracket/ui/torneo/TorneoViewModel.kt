package com.proyecto.quickbracket.ui.torneo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.quickbracket.ui.dao.Torneo

class TorneoViewModel : ViewModel() {

    private val _torneosActivos = MutableLiveData<List<Torneo>>()
    val torneosActivos: LiveData<List<Torneo>> get() = _torneosActivos


    private val _torneosFinalizados = MutableLiveData<List<Torneo>>()
    val torneosFinalizados: LiveData<List<Torneo>> get() = _torneosFinalizados

    private val db = FirebaseFirestore.getInstance()

    fun cargarTorneos() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("torneos")
            .whereEqualTo("creadoPor", userId) // 👈 Filtro clave
            .get()
            .addOnSuccessListener { snapshot ->
                val torneosActivos = mutableListOf<Torneo>()
                val torneosFinalizados = mutableListOf<Torneo>()

                for (doc in snapshot.documents) {
                    val torneo = doc.toObject(Torneo::class.java)?.copy(id = doc.id)
                    if (torneo != null) {
                        when (torneo.estado) {
                            "Activo" -> torneosActivos.add(torneo)
                            "Finalizado", "Inconcluso" -> torneosFinalizados.add(torneo)
                        }
                    }
                }

                _torneosActivos.value = torneosActivos
                _torneosFinalizados.value = torneosFinalizados
            }
            .addOnFailureListener {
                _torneosActivos.value = emptyList()
                _torneosFinalizados.value = emptyList()
            }
    }
}