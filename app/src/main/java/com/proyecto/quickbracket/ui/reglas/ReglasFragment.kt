package com.proyecto.quickbracket.ui.reglas

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.proyecto.quickbracket.databinding.FragmentReglasBinding
import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.proyecto.quickbracket.MisReglamentosActivity
import java.util.*

class ReglasFragment : Fragment() {

    private var _binding: FragmentReglasBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ReglasViewModel

    private var archivoUri: Uri? = null

    private val seleccionarArchivoLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            archivoUri = it
            subirPDF(it)
        }

    }

    private fun subirPDF(uri: Uri) {
        val storageRef = FirebaseStorage.getInstance().reference
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val nombreArchivoOriginal = obtenerNombreArchivo(uri) ?: UUID.randomUUID().toString()
        val rutaArchivo = "reglas/$userId/$nombreArchivoOriginal"
        val archivoRef = storageRef.child(rutaArchivo)

        val uploadTask = archivoRef.putFile(uri)

        uploadTask.addOnSuccessListener {
            archivoRef.downloadUrl.addOnSuccessListener { downloadUri ->
                Toast.makeText(requireContext(), "Archivo subido con éxito", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Error al subir el archivo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtenerNombreArchivo(uri: Uri): String? {
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            val nameIndex = it.getColumnIndexOpenableColumnsDisplayName()
            it.moveToFirst()
            it.getString(nameIndex)
        }
    }

    private fun android.database.Cursor.getColumnIndexOpenableColumnsDisplayName(): Int {
        return getColumnIndexOrThrow(android.provider.OpenableColumns.DISPLAY_NAME)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReglasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(ReglasViewModel::class.java)

        val juegosAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, viewModel.juegos)
        juegosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerJuegosReglas.adapter = juegosAdapter

        binding.spinnerJuegosReglas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val juegoSeleccionado = viewModel.juegos[position]
                viewModel.seleccionarJuego(juegoSeleccionado)

                if (juegoSeleccionado != "Selecciona un juego") {
                    binding.SpinnerDetalles.visibility = View.VISIBLE
                } else {
                    binding.SpinnerDetalles.visibility = View.GONE
                    binding.btnReglas.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        viewModel.detalles.observe(viewLifecycleOwner) { lista ->
            val detallesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, lista)
            detallesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.SpinnerDetalles.adapter = detallesAdapter
            binding.btnReglas.visibility = View.GONE
        }

        binding.SpinnerDetalles.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position >= 0) {
                    binding.btnReglas.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.btnReglas.setOnClickListener {
            val juego = binding.spinnerJuegosReglas.selectedItem.toString()
            val detalle = binding.SpinnerDetalles.selectedItem.toString()

            val infoJuego = mapOf(
                "League of Legends" to Pair("League_of_Legends", "LoL"),
                "Valorant" to Pair("Valorant", "Valorant"),
                "FIFA24" to Pair("FIFA24", "FIFA"),
                "Rocket League" to Pair("Rocket_League", "RL"),
                "Counter-Strike 2" to Pair("Counter_Strike_2", "CS2")
            )

            val (carpetaJuego, prefijoArchivo) = infoJuego[juego] ?: return@setOnClickListener

            val nombreArchivo = "${prefijoArchivo}_${detalle.replace(" ", "")}.pdf"

            val storageRef = FirebaseStorage.getInstance()
                .getReference("reglas/formatos_estandar/$carpetaJuego/$nombreArchivo")

            Log.d("PDF_DEBUG", "Ruta completa: reglas/formatos_estandar/$carpetaJuego/$nombreArchivo")

            storageRef.downloadUrl.addOnSuccessListener { uri ->
                Log.d("PDF_DEBUG", "URL de descarga: $uri")
                abrirPDF(uri)
            }.addOnFailureListener {
                Log.e("PDF_DEBUG", "Error al obtener el archivo PDF", it)
                Toast.makeText(requireContext(), "No se encontró el archivo", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnReglasSubidas.setOnClickListener {
            val intent = Intent(requireContext(), MisReglamentosActivity::class.java)
            startActivity(intent)
        }


        binding.btnSubir.setOnClickListener {
            seleccionarArchivoLauncher.launch("application/pdf")
        }
    }

    private fun abrirPDF(uri: Uri?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/pdf")
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION

        try {
            startActivity(intent)
        } catch (e: Exception){
            Toast.makeText(requireContext(), "No se encontro el archivo", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

