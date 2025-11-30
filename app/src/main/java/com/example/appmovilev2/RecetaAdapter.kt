package com.example.appmovilev2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView

class RecetaAdapter(
    private val listaRecetas: MutableList<Receta>,
    private val alHacerClick: (Receta) -> Unit
) : RecyclerView.Adapter<RecetaAdapter.RecetaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecetaViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_receta, parent, false)
        return RecetaViewHolder(vista)
    }

    override fun onBindViewHolder(holder: RecetaViewHolder, position: Int) {
        val receta = listaRecetas[position]
        holder.tvTitulo.text = receta.titulo
        holder.tvDescripcion.text = receta.descripcion

        // Configurar el click en la tarjeta
        holder.itemView.setOnClickListener {
            alHacerClick(receta)
        }

        // Asignar imagen usando la función
        val imagenId = obtenerImagenPorCategoria(receta.categoria)
        holder.imgIcono.setImageResource(imagenId)

        holder.itemView.setOnClickListener {
            alHacerClick(receta)
        }
    }

    override fun getItemCount(): Int = listaRecetas.size

    class RecetaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitulo: TextView = itemView.findViewById(R.id.txtTituloReceta)
        val tvDescripcion: TextView = itemView.findViewById(R.id.txtDescripcionReceta)
        val imgIcono: ImageView = itemView.findViewById(R.id.imgRecetaItem)
    }
}