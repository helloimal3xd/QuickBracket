    package com.proyecto.quickbracket.ui.torneo

    import android.content.Intent
    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.fragment.app.Fragment
    import androidx.lifecycle.ViewModelProvider
    import androidx.recyclerview.widget.LinearLayoutManager
    import com.proyecto.quickbracket.CrearTorneoActivity
    import com.proyecto.quickbracket.databinding.FragmentTorneoBinding
    import com.proyecto.quickbracket.ui.dao.Torneo
    import com.proyecto.quickbracket.ui.dao.TorneoAdapter

    class TorneoFragment : Fragment() {

        private lateinit var adapterActivos: TorneoAdapter
        private lateinit var adapterFinalizados: TorneoAdapter

        private lateinit var torneoViewModel: TorneoViewModel

        private var _binding: FragmentTorneoBinding? = null
        private val binding get() = _binding!!

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ):View {
            _binding = FragmentTorneoBinding.inflate(inflater, container, false)
            val root = binding.root

            torneoViewModel = ViewModelProvider(this).get(TorneoViewModel::class.java)

            adapterActivos = TorneoAdapter(emptyList())
            adapterFinalizados = TorneoAdapter(emptyList())

            binding.recyclerActivos.layoutManager = LinearLayoutManager(context)
            binding.recyclerActivos.adapter = adapterActivos

            binding.recyclerFinalizados.layoutManager = LinearLayoutManager(context)
            binding.recyclerFinalizados.adapter = adapterFinalizados

            binding.btnCrearTorneo.setOnClickListener {
                val intent = Intent(context, CrearTorneoActivity::class.java)
                startActivity(intent)
            }

            torneoViewModel.torneosActivos.observe(viewLifecycleOwner) { lista ->
                adapterActivos.actualizarLista(lista)
            }

            torneoViewModel.torneosFinalizados.observe(viewLifecycleOwner) { lista ->
                adapterFinalizados.actualizarLista(lista)
            }


            torneoViewModel.cargarTorneos()

            return root
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }