package com.proyecto.quickbracket

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.quickbracket.databinding.ActivityCrearTorneoBinding

class CrearTorneoActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCrearTorneoBinding
    private lateinit var db: FirebaseFirestore
    private var torneoId: String? = null
    private var esEdicion: Boolean = false


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCrearTorneoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        torneoId = intent.getStringExtra("torneoId") ?: ""
        esEdicion = !torneoId.isNullOrEmpty()


        val juegosAdapter = ArrayAdapter.createFromResource(
            this, R.array.lista_juegos, android.R.layout.simple_spinner_item
        )
        juegosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerJuegos.adapter = juegosAdapter

        val jugadoresAdapter = ArrayAdapter.createFromResource(
            this, R.array.cantidad_jugadores, android.R.layout.simple_spinner_item
        )
        jugadoresAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerJugadores.adapter = jugadoresAdapter

        if (esEdicion) {
            db.collection("torneos").document(torneoId!!).get()
                .addOnSuccessListener { doc ->
                    if (doc != null && doc.exists()) {
                        binding.etNombreTorneo.setText(doc.getString("nombre"))

                        val juego = doc.getString("juego")
                        val juegosArray = resources.getStringArray(R.array.lista_juegos)
                        val juegoIndex = juegosArray.indexOf(juego)
                        if (juegoIndex != -1) binding.spinnerJuegos.setSelection(juegoIndex)

                        val cantidad = doc.getLong("cantidadJugadores")?.toInt() ?: 0
                        val jugadoresArray = resources.getStringArray(R.array.cantidad_jugadores)
                        val cantidadIndex = jugadoresArray.indexOf(cantidad.toString())
                        if (cantidadIndex != -1) binding.spinnerJugadores.setSelection(cantidadIndex)

                        binding.btnSiguiente.text = "Guardar Cambios"
                    }
                }

        }

        binding.btnSiguiente.setOnClickListener {

            val nombreTorneo = binding.etNombreTorneo.text.toString()
            val juegoSeleccionado = binding.spinnerJuegos.selectedItem.toString()
            val cantidadJugadores =
                binding.spinnerJugadores.selectedItem.toString().toIntOrNull() ?: 0

            if (nombreTorneo.isEmpty()) {
                binding.etNombreTorneo.error = "Ingrese un nombre"
                return@setOnClickListener
            }

            if (cantidadJugadores <= 0) {
                makeText(
                    this,
                    "Ingrese una cantidad válida de jugadores",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val torneoData = mapOf(
                "nombre" to nombreTorneo,
                "juego" to juegoSeleccionado,
                "cantidadJugadores" to cantidadJugadores,
                "fecha" to System.currentTimeMillis(),
                "estado" to "Activo",
                "creadoPor" to userId
            )

            if (esEdicion) {
                db.collection("torneos").document(torneoId!!).update(torneoData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Torneo actualizado correctamente", Toast.LENGTH_SHORT)
                            .show()
                        setResult(RESULT_OK)
                        finish()
                    }
                    .addOnFailureListener {
                        makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
            } else {

                db.collection("torneos")
                    .add(torneoData)
                    .addOnSuccessListener { documentReference ->
                        makeText(this, "Torneo creado con exito :)", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, RegistrarEquiposActivity::class.java).apply {
                            putExtra("torneoId", documentReference.id)
                            putExtra("nombreTorneo", nombreTorneo)
                            putExtra("juegoSeleccionado", juegoSeleccionado)
                            putExtra("cantidadJugadores", cantidadJugadores)
                        }
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        Log.e("CrearTorneo", "Error al crear torneo", e)
                        Toast.makeText(
                            this,
                            "Error al crear el torneo: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }

        }
    }
}
