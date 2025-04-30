package com.proyecto.quickbracket.ui.dao

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.proyecto.quickbracket.R
import com.proyecto.quickbracket.ui.brackets.Bracket

class BracketAdapter(
    private val lista: List<Bracket>,
    private val onResultadoClick: (Bracket) -> Unit
) : RecyclerView.Adapter<BracketAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val equipo1Text: TextView = view.findViewById(R.id.equipo1Text)
        val equipo2Text: TextView = view.findViewById(R.id.equipo2Text)
        val puntosEquipo1: TextView = view.findViewById(R.id.puntos1Text)
        val puntosEquipo2: TextView = view.findViewById(R.id.puntos2Text)
        val botonResultado: Button = view.findViewById(R.id.btn_resultado)
        val rondaText: TextView = view.findViewById(R.id.tv_ronda)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bracket, parent, false)
        return ViewHolder(vista)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bracket = lista[position]

        holder.equipo1Text.text = bracket.equipo1
        holder.equipo2Text.text = bracket.equipo2

        holder.equipo1Text.setTextColor(Color.WHITE)
        holder.equipo2Text.setTextColor(Color.WHITE)

        holder.puntosEquipo1.text = bracket.puntosEquipo1.toString()
        holder.puntosEquipo2.text = bracket.puntosEquipo2.toString()

        holder.rondaText.text = "Ronda ${bracket.ronda}"

        if (bracket.ganador == bracket.equipo1) {
            holder.equipo1Text.setBackgroundColor(Color.GREEN)
        } else if (bracket.ganador == bracket.equipo2) {
            holder.equipo2Text.setBackgroundColor(Color.GREEN)
        }

        holder.botonResultado.setOnClickListener {
            onResultadoClick(bracket)
        }

    }

    override fun getItemCount(): Int = lista.size

}