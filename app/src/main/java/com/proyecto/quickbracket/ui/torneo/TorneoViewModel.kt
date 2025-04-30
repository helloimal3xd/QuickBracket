package com.proyecto.quickbracket.ui.torneo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.quickbracket.ui.dao.Torneo

class TorneoViewModel : ViewModel() {

    private val _torneosActivos = MutableLiveData<List<Torneo>>()
    val torneosActivos: LiveData<List<Torneo>> get() = _torneosActivos


    private val _torneosFinalizados = MutableLiveData<List<Torneo>>()
    val torneosFinalizados: LiveData<List<Torneo>> get() = _torneosFinalizados

    private val db = FirebaseFirestore.getInstance()

    fun cargarTorneos() {
        db.collection("torneos")
            .get()
            .addOnSuccessListener { snapshot ->
                val torneosActivos = mutableListOf<Torneo>()
                val torneosFinalizados = mutableListOf<Torneo>()


                for (doc in snapshot.documents) {
                    val torneo = doc.toObject(Torneo::class.java)?.copy(id = doc.id)
                    if (torneo != null) {
                        when (torneo.estado) {
                            "Activo" -> torneo?.let { torneosActivos.add(it) }
                            "Finalizado", "Inconcluso" -> torneo?.let { torneosFinalizados.add(it) }
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