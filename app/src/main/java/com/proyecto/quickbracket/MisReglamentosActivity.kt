package com.proyecto.quickbracket

import com.proyecto.quickbracket.databinding.ActivityMisReglasBinding
import com.proyecto.quickbracket.ui.dao.Reglamento
import com.proyecto.quickbracket.ui.dao.ReglamentosAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

class MisReglamentosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMisReglasBinding
    private lateinit var adapter: ReglamentosAdapter
    private val listaReglamentos = mutableListOf<Reglamento>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMisReglasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ReglamentosAdapter(listaReglamentos) { reglamento ->
            abrirPDF(reglamento.url)
        }

        binding.recyclerReglas.layoutManager = LinearLayoutManager(this)
        binding.recyclerReglas.adapter = adapter

        obtenerReglamentosUsuario()
    }

    private fun obtenerReglamentosUsuario() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val storageRef = FirebaseStorage.getInstance().reference.child("reglas/$userId")

        storageRef.listAll()
            .addOnSuccessListener { listResult ->
                listaReglamentos.clear()

                val tareas = listResult.items.map { item ->
                    item.downloadUrl.addOnSuccessListener { uri ->
                        val nombreArchivo = item.name
                        listaReglamentos.add(Reglamento(nombreArchivo, uri.toString()))
                        adapter.notifyDataSetChanged()
                    }
                }

                if (tareas.isEmpty()) {
                    Toast.makeText(this, "No has subido reglamentos aún", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al obtener reglamentos", Toast.LENGTH_SHORT).show()
            }
    }

    private fun abrirPDF(url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(Uri.parse(url), "application/pdf")
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "No se pudo abrir el archivo", Toast.LENGTH_SHORT).show()
        }
    }
}
