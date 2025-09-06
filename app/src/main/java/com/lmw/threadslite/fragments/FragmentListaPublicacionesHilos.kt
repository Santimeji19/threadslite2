package com.lmw.threadslite.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lmw.threadslite.R
import com.lmw.threadslite.adapters.HiloThreadsAdapter
import com.lmw.threadslite.databinding.FragmentEditHiloBinding
import com.lmw.threadslite.databinding.FragmentListaPublicacionesHilosBinding
import com.lmw.threadslite.models.Publicaciones
import com.lmw.threadslite.viewmodel.ThreadsViewModel

class FragmentListaHilos : Fragment() {

    private val viewModel: ThreadsViewModel by activityViewModels()
    private var _binding: FragmentListaPublicacionesHilosBinding? = null
    private val binding get() = _binding!!
    private lateinit var threadsAdapter: HiloThreadsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaPublicacionesHilosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        threadsAdapter = HiloThreadsAdapter(
            mutableListOf(),
            onEditClick = { hilo, position -> mostrarDialogoEditarHilo(hilo, position) },
            onDeleteClick = { hilo, position -> confirmarEliminacion(hilo, position) }
        )
        configurarRecyclerView()
        observarDatos()
    }

    private fun configurarRecyclerView() {
        binding.recyclerViewPublicaciones.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = threadsAdapter
        }
    }

    private fun observarDatos() {
        viewModel.datalistPublicaciones.observe(viewLifecycleOwner) { lista ->
            threadsAdapter.actualizarLista(lista)
        }
    }

    private fun mostrarDialogoEditarHilo(hilo: Publicaciones, position: Int) {
        val dialogBinding = FragmentEditHiloBinding.inflate(layoutInflater)

        dialogBinding.ettitulo.setText(hilo.titulo)
        dialogBinding.etdescripcion.setText(hilo.contenido)
        // Puedes agregar aquí la carga de la imagen si tu diálogo de edición tiene un ImageView
        // Glide.with(requireContext()).load(hilo.urlImagen).into(dialogBinding.imageViewEdicion)

        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()

        dialogBinding.btnDialogAlertGuardar.setOnClickListener {
            val nuevoTitulo = dialogBinding.ettitulo.text.toString().trim()
            val nuevoContenido = dialogBinding.etdescripcion.text.toString().trim()

            if (nuevoTitulo.isEmpty() || nuevoContenido.isEmpty()) {
                Toast.makeText(requireContext(), "Completa todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val hiloEditado = hilo.copy(titulo = nuevoTitulo, contenido = nuevoContenido)
            viewModel.editPublicacion(hiloEditado) // Se corrigió la llamada a la función
            Toast.makeText(requireContext(), "Publicación editada", Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
        }

        dialogBinding.btnDialogAlertCancelar.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun confirmarEliminacion(hilo: Publicaciones, position: Int) {
        AlertDialog.Builder(requireContext(), R.style.AlertDialogButtonsStyle)
            .setTitle("¿Eliminar Hilo?")
            .setMessage("¿Estás seguro de que vas a eliminar ésta publicación?")
            .setPositiveButton("Eliminar") { dialog, _ ->
                viewModel.deleteHilo(hilo.publiId)
                Toast.makeText(requireContext(), "Hilo eliminado", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}