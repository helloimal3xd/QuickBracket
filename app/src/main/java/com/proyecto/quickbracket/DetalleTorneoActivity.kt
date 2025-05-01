package com.proyecto.quickbracket

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.quickbracket.databinding.ActivityDetalleTorneoBinding
import com.proyecto.quickbracket.ui.brackets.BracketActivity
import com.proyecto.quickbracket.ui.dao.Equipo
import com.proyecto.quickbracket.ui.dao.EquipoAdapter
import java.util.Date

class DetalleTorneoActivity : AppCompatActivity() {

    private lateinit var torneoId: String
    private lateinit var binding: ActivityDetalleTorneoBinding

    private val editarTorneoLauncher =
        registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                cargarDatosTorneo()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleTorneoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        torneoId = intent.getStringExtra("torneoId") ?: ""

        cargarDatosTorneo()

        binding.btnVerBrackets.setOnClickListener {
            val intent = Intent(this, BracketActivity::class.java)
            intent.putExtra("torneoId", torneoId)
            startActivity(intent)
        }

        binding.btnEditarTorneo.setOnClickListener {
            val intent = Intent(this, CrearTorneoActivity::class.java)
            intent.putExtra("torneoId", torneoId)
            editarTorneoLauncher.launch(intent)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun cargarDatosTorneo() {
        val db = FirebaseFirestore.getInstance()

        db.collection("torneos")
            .document(torneoId)
            .get()
            .addOnSuccessListener { doc ->
                if (doc != null) {
                    val nombre = doc.getString("nombre")
                    val juego = doc.getString("juego")
                    val cantidadJugadores = doc.getLong("cantidadJugadores")?.toInt() ?: 0
                    val fecha = doc.getLong("fecha") ?: 0L
                    val estado = doc.getString("estado")

                    val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                    val fechaFormateada = sdf.format(Date(fecha))

                    binding.tvTorneo2.text = nombre
                    binding.tvJuego2.text = juego
                    binding.tvEstado2.text = estado
                    binding.tvFecha2.text = fechaFormateada
                    binding.tvJpe2.text = cantidadJugadores.toString()

                    cargarEquipos()

                }
            }
    }

    private fun cargarEquipos() {
        val db = FirebaseFirestore.getInstance()

        db.collection("torneos")
            .document(torneoId)
            .collection("equipos")
            .get()
            .addOnSuccessListener { result ->
                val listaEquipos = result.map { doc ->
                    Equipo(
                        nombre = doc.getString("nombre") ?: "",
                        cantidadJugadores = doc.getLong("cantidadJugadores")?.toInt() ?: 0,
                        jugadores = doc.get("jugadores") as? List<String> ?: emptyList()
                    )

                }

                binding.recyclerEquipos.layoutManager = LinearLayoutManager(this)
                binding.recyclerEquipos.adapter = EquipoAdapter(listaEquipos)

            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al cargar los equipos", Toast.LENGTH_SHORT).show()
            }
    }

}