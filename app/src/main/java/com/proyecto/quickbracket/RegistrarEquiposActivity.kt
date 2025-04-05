package com.proyecto.quickbracket

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.proyecto.quickbracket.databinding.ActivityRegistrarEquiposBinding
import com.proyecto.quickbracket.ui.brackets.BracketActivity
import com.proyecto.quickbracket.ui.dao.Equipo
import com.proyecto.quickbracket.ui.dao.EquipoAdapter

class RegistrarEquiposActivity: AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarEquiposBinding
    private lateinit var equipoAdapter: EquipoAdapter
    private val listaEquipos = mutableListOf<Equipo>()
    private var cantidadJugadores = 1


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarEquiposBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cantidadJugadores = intent.getIntExtra("cantidadJugadores", 1)

        binding.tvInfoJugadores.text = "Cada equipo debe tener $cantidadJugadores jugadores"

        equipoAdapter = EquipoAdapter(listaEquipos)
        binding.rvEquipos.layoutManager = LinearLayoutManager(this)
        binding.rvEquipos.adapter = equipoAdapter

        binding.btnAgregarEquipo.setOnClickListener {
            agregarEquipo()
        }

        binding.btnSiguiente.setOnClickListener {
            confirmarConinuar()
        }

    }

    private fun confirmarConinuar() {
        AlertDialog.Builder(this)
            .setTitle("Seguro que desea continuar?")
            .setMessage("Se han registrado ${listaEquipos.size} equipos. ¿Desea continuar?")
            .setPositiveButton("Sí") { _, _ ->
                Toast.makeText(this, "Guardando...", Toast.LENGTH_SHORT).show()
                val intent = android.content.Intent(this, BracketActivity::class.java)
                val equipos = listaEquipos.map { it.nombre }
                intent.putStringArrayListExtra("equipos", ArrayList(equipos))
                startActivity(intent)
            }
            .setNegativeButton("No", null)
            .show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun agregarEquipo() {

        val nombreEquipo = binding.etNombreEquipo.text.toString().trim()

        if (nombreEquipo.isEmpty()){
            Toast.makeText(this, "Ingrese un nombre de equipo", Toast.LENGTH_SHORT).show()
            return
        }

        if (listaEquipos.any { it.nombre == nombreEquipo }) {
            Toast.makeText(this, "Ya existe un equipo con ese nombre", Toast.LENGTH_SHORT).show()
            return

        }

        val equipo = Equipo(nombre = nombreEquipo, cantidadJugadores = cantidadJugadores)
        listaEquipos.add(equipo)
        equipoAdapter.notifyDataSetChanged()
        binding.etNombreEquipo.text.clear()
    }

}
