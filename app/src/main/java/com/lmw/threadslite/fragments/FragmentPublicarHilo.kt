package com.lmw.threadslite.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.lmw.threadslite.databinding.FragmentPublicarHiloBinding
import com.lmw.threadslite.models.Publicaciones
import com.lmw.threadslite.viewmodel.ThreadsViewModel
import java.util.UUID // Importar la clase UUID para generar un ID único

class FragmentPublicarHilo : Fragment() {

    private var _binding: FragmentPublicarHiloBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ThreadsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPublicarHiloBinding.inflate(inflater, container, false)
        binding.btnCrearHilo.setOnClickListener { publicarHilo() }
        return binding.root
    }

    private fun publicarHilo() {
        val autor = arguments?.getString(ARG_NOMBRE_USUARIO) ?: "Anónimo"
        val titulo = binding.etTituloPublicacion.text.toString().trim()
        val contenido = binding.etContenidoPublicacion.text.toString().trim()
        val url = binding.etUrlImagen.text.toString().trim()

        if (titulo.isBlank() || contenido.isBlank()) {
            Toast.makeText(requireContext(), "El título y el contenido no pueden estar vacíos", Toast.LENGTH_SHORT).show()
            return
        }

        // Generar un ID de publicación único para el nuevo hilo.
        val publiId = UUID.randomUUID().hashCode()

        val hilo = Publicaciones(
            publiId = publiId,
            titulo = titulo,
            contenido = contenido,
            nombreUsuario = autor,
            url = url
        )

        viewModel.agregarHilo(hilo)

        Toast.makeText(requireContext(), "Hilo publicado", Toast.LENGTH_SHORT).show()
        parentFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_NOMBRE_USUARIO = "nombre_usuario"

        fun newInstance(nombreUsuario: String): FragmentPublicarHilo {
            val fragment = FragmentPublicarHilo()
            fragment.arguments = Bundle().apply {
                putString(ARG_NOMBRE_USUARIO, nombreUsuario)
            }
            return fragment
        }
    }
}