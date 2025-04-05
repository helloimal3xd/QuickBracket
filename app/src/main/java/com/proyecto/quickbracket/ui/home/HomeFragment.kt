package com.proyecto.quickbracket.ui.home

import HomeViewModelFactory
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.proyecto.quickbracket.CrearTorneoActivity
import com.proyecto.quickbracket.databinding.FragmentHomeBinding
import com.proyecto.quickbracket.ui.dao.TorneoAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var torneoAdapter: TorneoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        homeViewModel = ViewModelProvider(this, HomeViewModelFactory(requireActivity().application))[HomeViewModel::class.java]

        // Configurar el RecyclerView
        torneoAdapter = TorneoAdapter(emptyList())
        binding.recyclerTorneos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = torneoAdapter
        }

        // Observar los cambios en la lista de torneos
        homeViewModel.torneosActivos.observe(viewLifecycleOwner) { torneos ->
            if (torneos.isEmpty()) {
                binding.tvNoTorneos.visibility = View.VISIBLE
                binding.btnCrearTorneo.visibility = View.VISIBLE
                binding.recyclerTorneos.visibility = View.GONE
            } else {
                binding.tvNoTorneos.visibility = View.GONE
                binding.btnCrearTorneo.visibility = View.GONE
                binding.recyclerTorneos.visibility = View.VISIBLE
                torneoAdapter.actualizarLista(torneos)
            }

        }

        homeViewModel.torneosActivos.observe(viewLifecycleOwner) { torneos ->
            for (torneo in torneos) {
                Log.d("HomeFragment", "Torneo: ${torneo.nombre}, Equipos: ${torneo.equipos}")
            }
        }

        binding.btnCrearTorneo.setOnClickListener {
            val intent = Intent(this.context, CrearTorneoActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

