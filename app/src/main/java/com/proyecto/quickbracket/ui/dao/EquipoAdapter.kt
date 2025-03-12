package com.proyecto.quickbracket.ui.dao

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proyecto.quickbracket.databinding.ItemEquipoBinding

class EquipoAdapter (private val equipos: List<Equipo>):
    RecyclerView.Adapter<EquipoAdapter.EquipoViewHolder>() {

        class EquipoViewHolder(private val binding: ItemEquipoBinding) :
                RecyclerView.ViewHolder(binding.root) {
            @SuppressLint("SetTextI18n")
            fun bind(equipo: Equipo) {
                binding.tvNombreEquipo.text = equipo.nombre
                binding.tvCantidadJugadores.text = "Jugadores: ${equipo.cantidadJugadores}"
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipoViewHolder {
        val binding = ItemEquipoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EquipoViewHolder(binding)

    }

    override fun onBindViewHolder(holder: EquipoViewHolder, position: Int) {
        holder.bind(equipos[position])
    }

    override fun getItemCount(): Int = equipos.size

    }


