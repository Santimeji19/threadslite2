package com.lmw.threadslite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lmw.threadslite.databinding.ItemHiloBinding
import com.lmw.threadslite.models.Publicaciones

class ThreadsAdapter(
    private var publicaciones: List<Publicaciones>, // ✅ nombre más claro
    private val onEditarClick: (Publicaciones, Int) -> Unit,
    private val onEliminarClick: (Publicaciones, Int) -> Unit,
    private val onVerDetalleClick: (Publicaciones) -> Unit
) : RecyclerView.Adapter<ThreadsAdapter.ThreadsViewHolder>() {

    inner class ThreadsViewHolder(private val binding: ItemHiloBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(publicacion: Publicaciones) {
            binding.tvAutor.text = publicacion.autor
            binding.tvTitulo.text = publicacion.titulo
            binding.tvContenido.text = publicacion.contenido

            // ✅ siempre usa bindingAdapterPosition (es más seguro que adapterPosition)
            binding.btnEditar.setOnClickListener {
                val hilo= bindingAdapterPosition
                if (hilo != RecyclerView.NO_POSITION) {
                    onEditarClick(publicacion, hilo)
                }
            }
            binding.btnEliminar.setOnClickListener {
                val hilo = bindingAdapterPosition
                if (hilo != RecyclerView.NO_POSITION) {
                    onEliminarClick(publicacion, hilo)
                }
            }
            binding.root.setOnClickListener {
                onVerDetalleClick(publicacion)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThreadsViewHolder {
        val binding = ItemHiloBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ThreadsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ThreadsViewHolder, position: Int) {
        val publicacion = publicaciones[position]
        holder.bind(publicacion)
    }

    override fun getItemCount(): Int = publicaciones.size

    fun actualizarLista(nuevaLista: List<Publicaciones>) {
        publicaciones = nuevaLista
        notifyDataSetChanged()
    }
}
