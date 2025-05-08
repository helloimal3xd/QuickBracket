package com.proyecto.quickbracket.ui.reglas

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

class ReglasFragment : Fragment() {

    private var _binding: FragmentReglasBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ReglasViewModel

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

            Toast.makeText(requireContext(), "Mostrando reglas de $juego - $detalle", Toast.LENGTH_SHORT).show()
            // Aquí puedes navegar o abrir contenido según el juego
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

