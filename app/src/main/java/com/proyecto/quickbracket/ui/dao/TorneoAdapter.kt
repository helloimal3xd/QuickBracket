package com.proyecto.quickbracket.ui.dao

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proyecto.quickbracket.databinding.ItemTorneoBinding

class TorneoAdapter(private var torneos: List<TorneoEntity>) :
    RecyclerView.Adapter<TorneoAdapter.TorneoViewHolder>() {

    inner class TorneoViewHolder(private val binding: ItemTorneoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(torneo: TorneoEntity) {

            binding.tvNombreTorneo.text = torneo.nombre
            binding.tvFechaTorneo.text = torneo.fecha

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TorneoViewHolder {
        val binding = ItemTorneoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TorneoViewHolder(binding)
    }

    override fun getItemCount() = torneos.size

    override fun onBindViewHolder(holder: TorneoViewHolder, position: Int) {
        holder.bind(torneos[position])
    }

    fun actualizarLista(nuevaLista: List<TorneoEntity>) {
        torneos = nuevaLista
        notifyDataSetChanged()

    }

}