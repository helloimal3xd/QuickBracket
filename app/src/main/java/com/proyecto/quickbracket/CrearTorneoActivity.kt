package com.proyecto.quickbracket

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.quickbracket.databinding.ActivityCrearTorneoBinding

class CrearTorneoActivity: AppCompatActivity(){

    private lateinit var binding: ActivityCrearTorneoBinding
    private lateinit var db: FirebaseFirestore


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCrearTorneoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid


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

        binding.btnSiguiente.setOnClickListener {

            val nombreTorneo = binding.etNombreTorneo.text.toString()
            val juegoSeleccionado = binding.spinnerJuegos.selectedItem.toString()
            val cantidadJugadores = binding.spinnerJugadores.selectedItem.toString().toIntOrNull() ?: 0

            if (nombreTorneo.isEmpty()) {
                binding.etNombreTorneo.error = "Ingrese un nombre"
                return@setOnClickListener
            }

            if (cantidadJugadores <= 0) {
                makeText(this, "Ingrese una cantidad válida de jugadores", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val torneoData = hashMapOf(
                "nombre" to nombreTorneo,
                "juego" to juegoSeleccionado,
                "cantidadJugadores" to cantidadJugadores,
                "fecha" to System.currentTimeMillis(),
                "estado" to "Activo",
                "creadoPor" to userId
            )

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
                    Toast.makeText(this, "Error al crear el torneo: ${e.message}", Toast.LENGTH_SHORT).show()
                }

        }

    }
}
/* dejo 2 cosas pendientes:
*
* 1. antes de seguir con el diseño de la visualizacion de detalles del torneo, tengo que decirle que no se estan
*    viendo los enfrentamientos en el bracketActivity (el ultimo torneo que creé)
* 2. ahora si hacer lo de la visualizacion de detalles del torneo (entregarle esta mamada de activity que estoy)
* */