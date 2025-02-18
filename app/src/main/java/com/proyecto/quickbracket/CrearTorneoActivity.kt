package com.proyecto.quickbracket

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class CrearTorneoActivity: AppCompatActivity(){

    private lateinit var etNombreTorneo: EditText
    private lateinit var spinnerJuegos: Spinner
    private lateinit var spinnerJugadores: Spinner
    private lateinit var btnSiguiente: Button


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_torneo)

        etNombreTorneo = findViewById(R.id.etNombreTorneo)
        spinnerJuegos = findViewById(R.id.spinnerJuegos)
        spinnerJugadores = findViewById(R.id.spinnerJugadores)
        btnSiguiente = findViewById(R.id.btnSiguiente)

        val juegosAdapter = ArrayAdapter.createFromResource(
            this, R.array.lista_juegos, android.R.layout.simple_spinner_item
        )
        juegosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerJuegos.adapter = juegosAdapter

        val jugadoresAdapter = ArrayAdapter.createFromResource(
            this, R.array.cantidad_jugadores, android.R.layout.simple_spinner_item
        )
        jugadoresAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerJugadores.adapter = jugadoresAdapter

        btnSiguiente.setOnClickListener {

            val nombreTorneo = etNombreTorneo.text.toString()
            val juegoSeleccionado = spinnerJuegos.selectedItem.toString()
            val cantidadJugadores = spinnerJugadores.selectedItem.toString().toIntOrNull() ?: 0

            if (nombreTorneo.isEmpty()) {
                etNombreTorneo.error = "Ingrese un nombre"
                return@setOnClickListener
            }

            if (cantidadJugadores <= 0) {
                Toast.makeText(this, "Ingrese una cantidad válida de jugadores", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, RegistrarEquiposActivity::class.java).apply {
                putExtra("nombreTorneo", nombreTorneo)
                putExtra("juegoSeleccionado", juegoSeleccionado)
                putExtra("cantidadJugadores", cantidadJugadores)
            }
            startActivity(intent)

        }

    }
}