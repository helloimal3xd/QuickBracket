package com.proyecto.quickbracket.ui.brackets

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.quickbracket.R
import com.proyecto.quickbracket.databinding.ActivityBracketBinding
import com.proyecto.quickbracket.ui.dao.BracketAdapter

class BracketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBracketBinding
    private lateinit var adapter: BracketAdapter
    private val listaBrackets = mutableListOf<Bracket>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBracketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicia el RecyclerView
        adapter = BracketAdapter(listaBrackets) { bracket ->
            mostrarDialogoResultado(bracket)
        }
        binding.recyclerEnfrentamientos.layoutManager = LinearLayoutManager(this)
        binding.recyclerEnfrentamientos.adapter = adapter

        // Obtiene ID del torneo y carga brackets
        val torneoId = intent.getStringExtra("torneoId") ?: ""
        if (torneoId.isNotEmpty()) {
            cargarEquiposYGenerarBrackets(torneoId)
        } else {
            Toast.makeText(this, "ID del torneo no encontrado", Toast.LENGTH_SHORT).show()
        }

        binding.btnFinalizarTorneo.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Finalizar Torneo")
                .setMessage("¿Estás seguro de que deseas finalizar el torneo?")
                .setPositiveButton("Sí") { _, _ ->
                    val torneoId = intent.getStringExtra("torneoId") ?: return@setPositiveButton
                    val db = FirebaseFirestore.getInstance()

                    db.collection("torneos")
                        .document(torneoId)
                        .update("estado", "Inconcluso")
                        .addOnSuccessListener {
                            Toast.makeText(this, "Torneo finalizado exitosamente", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error al finalizar el torneo", Toast.LENGTH_SHORT).show()
                        }

                }
                .setNegativeButton("cancelar", null)
                .show()
        }

    }

    private fun mostrarDialogoResultado(bracket: Bracket) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_resultado, null)

        val tvEquipo1 = dialogView.findViewById<TextView>(R.id.tvEquipo1)
        val tvEquipo2 = dialogView.findViewById<TextView>(R.id.tvEquipo2)
        val etPuntosEquipo1 = dialogView.findViewById<TextView>(R.id.etPuntosEquipo1)
        val etPuntosEquipo2 = dialogView.findViewById<TextView>(R.id.etPuntosEquipo2)
        val btnGanador1 = dialogView.findViewById<TextView>(R.id.btnGanador1)
        val btnGanador2 = dialogView.findViewById<TextView>(R.id.btnGanador2)

        tvEquipo1.text = bracket.equipo1
        tvEquipo2.text = bracket.equipo2

        val alertDialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        alertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

        alertDialog.show()

        btnGanador1.setOnClickListener {
            val puntosEquipo1 = etPuntosEquipo1.text.toString().toIntOrNull() ?: 0
            val puntosEquipo2 = etPuntosEquipo2.text.toString().toIntOrNull() ?: 0

            bracket.ganador = bracket.equipo1

            guardarResultado(bracket, puntosEquipo1, puntosEquipo2)

            alertDialog.dismiss()
        }

        btnGanador2.setOnClickListener {
            val puntosEquipo1 = etPuntosEquipo1.text.toString().toIntOrNull() ?: 0
            val puntosEquipo2 = etPuntosEquipo2.text.toString().toIntOrNull() ?: 0

            bracket.puntosEquipo1 = puntosEquipo1
            bracket.puntosEquipo2 = puntosEquipo2
            bracket.ganador = bracket.equipo2

            guardarResultado(bracket, puntosEquipo1, puntosEquipo2)

            alertDialog.dismiss()
        }
    }

    private fun guardarResultado(bracket: Bracket, puntosEquipo1: Int, puntosEquipo2: Int) {
        val db = FirebaseFirestore.getInstance()
        val torneoId = intent.getStringExtra("torneoId") ?: ""

        bracket.puntosEquipo1 = puntosEquipo1
        bracket.puntosEquipo2 = puntosEquipo2

        db.collection("torneos")
            .document(torneoId)
            .collection("enfrentamientos")
            .document(bracket.id)
            .update(
                mapOf(
                    "puntosEquipo1" to puntosEquipo1,
                    "puntosEquipo2" to puntosEquipo2,
                    "ganador" to bracket.ganador
                )
            )
            .addOnSuccessListener {
                Toast.makeText(this, "Resultado guardado exitosamente", Toast.LENGTH_SHORT).show()
                adapter.notifyDataSetChanged()

                verificarYSiguienteRonda(bracket.ronda)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al guardar el resultado", Toast.LENGTH_SHORT).show()
            }
    }

    private fun verificarYSiguienteRonda(rondaActual: String) {
        val db = FirebaseFirestore.getInstance()
        val torneoId = intent.getStringExtra("torneoId") ?: ""

        db.collection("torneos")
            .document(torneoId)
            .collection("enfrentamientos")
            .whereEqualTo("ronda", rondaActual)
            .get()
            .addOnSuccessListener { snapshot ->
                val enfrentamientos = snapshot.documents
                val todosConGanador =  enfrentamientos.all { it.getString("ganador") != null }

                if (todosConGanador) {

                    val ganadores = enfrentamientos.mapNotNull { it.getString("ganador") }

                    if (ganadores.size == 1) {
                        db.collection("torneos")
                            .document(torneoId)
                            .update("estado", "Finalizado")
                            .addOnSuccessListener {
                                Toast.makeText(this, "Eso es todo, el ganador es ${ganadores.first()} ", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Ganador: ${ganadores.first()}, pero no se logró actualizar el estado del torneo", Toast.LENGTH_SHORT).show()
                            }
                    }
                    else {
                        val nRondaActual = rondaActual.filter { it.isDigit() }.toIntOrNull() ?: 1
                        val rondaActual = "Ronda ${nRondaActual + 1}"
                        val nuevoIdInicial = enfrentamientos.mapNotNull { it.getString("id")?.toIntOrNull() }.maxOrNull()?.plus(1) ?: 1

                        val nuevosBrackets = generarBrackets(ganadores, rondaActual, nuevoIdInicial)

                        for (bracket in nuevosBrackets) {
                            val data = mapOf(
                                "equipo1" to bracket.equipo1,
                                "equipo2" to bracket.equipo2,
                                "puntosEquipo1" to bracket.puntosEquipo1,
                                "puntosEquipo2" to bracket.puntosEquipo2,
                                "ganador" to null,
                                "ronda" to bracket.ronda
                            )

                            db.collection("torneos")
                                .document(torneoId)
                                .collection("enfrentamientos")
                                .document(bracket.id)
                                .set(data)
                        }

                        Toast.makeText(this, "avanzamos a la $rondaActual!!!", Toast.LENGTH_SHORT).show()

                    }

                }

            }
    }

    private fun cargarEquiposYGenerarBrackets(torneoId: String) {
        val db = FirebaseFirestore.getInstance()

        val enfrentamientosRef = db.collection("torneos")
            .document(torneoId)
            .collection("enfrentamientos")

        enfrentamientosRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Toast.makeText(this, "Error al escuchar cambios", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                listaBrackets.clear()
                for (document in snapshot.documents) {
                    val bracket = document.getString("ganador")?.let {
                        Bracket(
                            equipo1 = document.getString("equipo1") ?: "",
                            equipo2 = document.getString("equipo2") ?: "",
                            puntosEquipo1 = document.getLong("puntosEquipo1")?.toInt() ?: 0,
                            puntosEquipo2 = document.getLong("puntosEquipo2")?.toInt() ?: 0,
                            ronda = document.getString("ronda") ?: "",
                            id = document.getString("id") ?: document.id,
                            ganador = it
                        )
                    }
                    if (bracket != null) {
                        listaBrackets.add(bracket)
                    }
                }
                listaBrackets.sortBy { it.id.toIntOrNull() ?: 0 }
                adapter.notifyDataSetChanged()
            } else {
                generarYGuardarBrackets(torneoId)
            }
        }
    }

    private fun generarYGuardarBrackets(torneoId: String) {
        val db = FirebaseFirestore.getInstance()


        db.collection("torneos")
            .document(torneoId)
            .collection("equipos")
            .get()
            .addOnSuccessListener { equiposSnapshot ->
                val nombresEquipos = equiposSnapshot.documents.mapNotNull { it.getString("nombre") }.shuffled()

                db.collection("torneos")
                    .document(torneoId)
                    .collection("enfrentamientos")
                    .get()
                    .addOnSuccessListener { enfrentamientosSnapshot ->
                        val enfrentamientos = enfrentamientosSnapshot.documents

                        val rondasExistentes = enfrentamientos.mapNotNull { it.getString("ronda") }.toSet()
                        val siguienteNumeroRonda = rondasExistentes.size + 1
                        val nuevaRonda = "Ronda $siguienteNumeroRonda"

                        val idInicial = enfrentamientos.size + 1

                        val bracketsGenerados = generarBrackets(nombresEquipos, nuevaRonda, idInicial)

                        val enfrentamientosRef = db.collection("torneos")
                            .document(torneoId)
                            .collection("enfrentamientos")

                        for (bracket in bracketsGenerados) {
                            val data = mapOf(
                                "equipo1" to bracket.equipo1,
                                "equipo2" to bracket.equipo2,
                                "puntosEquipo1" to bracket.puntosEquipo1,
                                "puntosEquipo2" to bracket.puntosEquipo2,
                                "ronda" to bracket.ronda,
                                "id" to bracket.id
                            )
                            enfrentamientosRef.add(data)
                        }

                        listaBrackets.clear()
                        listaBrackets.addAll(bracketsGenerados)
                        adapter.notifyDataSetChanged()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al cargar los datos", Toast.LENGTH_SHORT).show()
            }

    }

    private fun generarBrackets(equipos: List<String>, ronda: String, idInicial: Int): MutableList<Bracket> {
        val enfrentamientos = mutableListOf<Bracket>()
        var idCounter = idInicial

        for (i in equipos.indices step 2) {
            if (i + 1 < equipos.size) {
                enfrentamientos.add(
                    Bracket(
                        equipo1 = equipos[i],
                        equipo2 = equipos[i + 1],
                        puntosEquipo1 = 0,
                        puntosEquipo2 = 0,
                        ronda = ronda,
                        id = idCounter++.toString()
                    )
                )
            } else {
                enfrentamientos.add(
                    Bracket(
                        equipo1 = equipos[i],
                        equipo2 = "bye",
                        puntosEquipo1 = 0,
                        puntosEquipo2 = 0,
                        ronda = ronda,
                        id = idCounter++.toString(),
                        ganador = equipos [i]
                    )
                )
            }
        }
        return enfrentamientos
    }
}
