package com.proyecto.quickbracket.ui.dao

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proyecto.quickbracket.databinding.ItemTorneoBinding
import com.proyecto.quickbracket.ui.brackets.BracketActivity

class TorneoAdapter(private var listaTorneos: List<Torneo>) :
    RecyclerView.Adapter<TorneoAdapter.TorneoViewHolder>() {

    class TorneoViewHolder(val binding: ItemTorneoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TorneoViewHolder {
        val binding = ItemTorneoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TorneoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TorneoViewHolder, position: Int) {
        val torneo = listaTorneos[position]
        holder.binding.tvNombreTorneo.text = torneo.nombre

        // Manejar el clic en el torneo para abrir BracketActivity
        holder.binding.root.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, BracketActivity::class.java)
            intent.putStringArrayListExtra("equipos", ArrayList(torneo.equipos))
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = listaTorneos.size

    fun actualizarLista(nuevaLista: List<Torneo>) {
        listaTorneos = nuevaLista
        notifyDataSetChanged()
    }
}
