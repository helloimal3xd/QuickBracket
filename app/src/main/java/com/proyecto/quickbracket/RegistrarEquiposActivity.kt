package com.proyecto.quickbracket

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.quickbracket.databinding.ActivityRegistrarEquiposBinding
import com.proyecto.quickbracket.ui.brackets.BracketActivity
import com.proyecto.quickbracket.ui.dao.Equipo
import com.proyecto.quickbracket.ui.dao.EquipoAdapter

class RegistrarEquiposActivity: AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarEquiposBinding
    private lateinit var equipoAdapter: EquipoAdapter
    private val listaEquipos = mutableListOf<Equipo>()

    private lateinit var torneoId: String
    private var cantidadJugadores = 1

    private val db = FirebaseFirestore.getInstance()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarEquiposBinding.inflate(layoutInflater)
        setContentView(binding.root)

        torneoId = intent.getStringExtra("torneoId") ?: ""
        cantidadJugadores = intent.getIntExtra("cantidadJugadores", 1)

        binding.tvInfoJugadores.text = "Cada equipo debe tener $cantidadJugadores jugadores"

        equipoAdapter = EquipoAdapter(listaEquipos)
        binding.rvEquipos.layoutManager = LinearLayoutManager(this)
        binding.rvEquipos.adapter = equipoAdapter

        // Generar campos de texto para nombres de jugadores
        generarCamposJugadores()

        binding.btnAgregarEquipo.setOnClickListener {
            agregarEquipo()
        }

        binding.btnSiguiente.setOnClickListener {
            confirmarContinuar()
        }
    }

    private fun confirmarContinuar() {
        AlertDialog.Builder(this)
            .setTitle("¿Desea continuar?")
            .setMessage("Se han registrado ${listaEquipos.size} equipos. ¿Desea continuar?")
            .setPositiveButton("Sí") { _, _ ->

                val db = FirebaseFirestore.getInstance()
                val equiposCollection = db.collection("torneos")
                    .document(torneoId)
                    .collection("equipos")

                // Subir todos los equipos
                for (equipo in listaEquipos) {
                    val equipoMap = mapOf(
                        "nombre" to equipo.nombre,
                        "cantidadJugadores" to equipo.cantidadJugadores,
                        "jugadores" to equipo.jugadores
                    )

                    equiposCollection.add(equipoMap)
                }

                // Ir a la siguiente pantalla luego de guardar
                val intent = Intent(this, BracketActivity::class.java)
                intent.putExtra("torneoId", torneoId)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun agregarEquipo() {
        val nombreEquipo = binding.etNombreEquipo.text.toString().trim()

        if (nombreEquipo.isEmpty()) {
            Toast.makeText(this, "Ingrese un nombre de equipo", Toast.LENGTH_SHORT).show()
            return
        }

        if (listaEquipos.any { it.nombre == nombreEquipo }) {
            Toast.makeText(this, "Ya existe un equipo con ese nombre", Toast.LENGTH_SHORT).show()
            return
        }

        val nombresJugadores = mutableListOf<String>()
        for (i in 0 until binding.layoutJugadores.childCount) {
            val editText = binding.layoutJugadores.getChildAt(i) as EditText
            val nombreJugador = editText.text.toString().trim()
            if (nombreJugador.isEmpty()) {
                Toast.makeText(this, "Todos los campos de jugadores deben estar llenos", Toast.LENGTH_SHORT).show()
                return
            }
            nombresJugadores.add(nombreJugador)
        }

        val equipo = Equipo(
            nombre = nombreEquipo,
            cantidadJugadores = cantidadJugadores,
            jugadores = nombresJugadores
        )
        listaEquipos.add(equipo)
        equipoAdapter.notifyDataSetChanged()

        binding.etNombreEquipo.text.clear()
        generarCamposJugadores() // Limpiar campos
    }

    private fun generarCamposJugadores() {
        binding.layoutJugadores.removeAllViews()

        repeat(cantidadJugadores) { index ->
            val editText = EditText(this)
            editText.hint = "Jugador ${index + 1}"
            editText.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            binding.layoutJugadores.addView(editText)
        }
    }


}
