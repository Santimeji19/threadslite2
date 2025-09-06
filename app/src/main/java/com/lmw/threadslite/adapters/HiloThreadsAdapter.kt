package com.lmw.threadslite.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lmw.threadslite.databinding.ItemHiloBinding
import com.lmw.threadslite.models.Publicaciones

/**
 * Adaptador para mostrar una lista de publicaciones en un RecyclerView.
 *
 * @param listaHilos La lista de objetos Publicaciones a mostrar.
 * @param onEditClick Un lambda que se ejecuta cuando se hace clic en el botón de editar de un hilo.
 * @param onDeleteClick Un lambda que se ejecuta cuando se hace clic en el botón de eliminar de un hilo.
 */
class HiloThreadsAdapter(
    private val listaHilos: MutableList<Publicaciones>,
    private val onEditClick: (Publicaciones, Int) -> Unit,
    private val onDeleteClick: (Publicaciones, Int) -> Unit
) : RecyclerView.Adapter<HiloThreadsAdapter.HiloViewHolder>() {

    // ViewHolder para contener las vistas de cada elemento de la lista.
    inner class HiloViewHolder(private val binding: ItemHiloBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(hilo: Publicaciones) {
            // Asigna los datos del hilo a las vistas correspondientes en el diseño.
            binding.tvTitulo.text = hilo.titulo
            binding.tvContenido.text = hilo.contenido
            binding.tvAutor.text = hilo.nombreUsuario
            // Aquí puedes cargar la imagen si tu modelo de datos y layout lo permiten.
            // Glide.with(binding.ivImagenHilo.context).load(hilo.url).into(binding.ivImagenHilo)

            // Configura los listeners de clic para los botones de editar y eliminar.
            binding.btnEditar.setOnClickListener {
                onEditClick(hilo, adapterPosition)
            }
            binding.btnEliminar.setOnClickListener {
                onDeleteClick(hilo, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HiloViewHolder {
        // Infla el layout de un solo elemento (item_hilo.xml) usando View Binding.
        val binding = ItemHiloBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HiloViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HiloViewHolder, position: Int) {
        // Vincula los datos de la posición actual al ViewHolder.
        val hilo = listaHilos[position]
        holder.bind(hilo)
    }

    override fun getItemCount(): Int = listaHilos.size

    /**
     * Actualiza la lista de publicaciones y notifica al RecyclerView para que se redibuje.
     * Esta función es la que tu fragmento estaba buscando.
     *
     * @param nuevaLista La nueva lista de publicaciones.
     */
    fun actualizarLista(nuevaLista: List<Publicaciones>) {
        listaHilos.clear()
        listaHilos.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}

