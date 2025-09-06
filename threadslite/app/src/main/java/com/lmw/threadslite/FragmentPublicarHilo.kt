package com.lmw.threadslite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lmw.threadslite.adapter.ThreadsAdapter
import com.lmw.threadslite.databinding.FragmentPublicarHiloBinding
import com.lmw.threadslite.models.ThreadsViewModel
import com.lmw.threadslite.models.Publicaciones
import com.lmw.threadslite.services.ConexionService_data
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FragmentPublicarHilo : Fragment() {

    private var _binding: FragmentPublicarHiloBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ThreadsViewModel by activityViewModels()
    private lateinit var adapter: ThreadsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPublicarHiloBinding.inflate(inflater, container, false)

        // Inicializamos el RecyclerView
        setupRecyclerView()

        // Observamos el ViewModel
        observerViewModel()

        // Botón publicar
        binding.anadirpublicacion.setOnClickListener { publicarHilo() }

        // Llamamos a la API para cargar publicaciones
        listarNombreFuncion()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = ThreadsAdapter(
            emptyList(),
            onEditarClick = { publicacion, pos ->
                Toast.makeText(requireContext(), "Editar: ${publicacion.titulo}", Toast.LENGTH_SHORT).show()
            },
            onEliminarClick = { _, pos ->
                viewModel.deleteHilo(pos)
                Toast.makeText(requireContext(), "Hilo eliminado", Toast.LENGTH_SHORT).show()
            },
            onVerDetalleClick = { publicacion ->
                Toast.makeText(requireContext(), "Detalle de: ${publicacion.titulo}", Toast.LENGTH_SHORT).show()
            }
        )
        binding.recyclerViewPublicaciones.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewPublicaciones.adapter = adapter
    }

    private fun publicarHilo() {
        val autor = arguments?.getString("nombre_usuario") ?: "Anónimo"
        val titulo = binding.ettitulo.text.toString().trim()
        val contenido = binding.etdescripcion.text.toString().trim()

        if (titulo.isEmpty() || contenido.isEmpty()) {
            Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val hilo = Publicaciones(autor, titulo, contenido)
        viewModel.agregarHilo(hilo)
        Toast.makeText(requireContext(), "Hilo publicado", Toast.LENGTH_SHORT).show()
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun observerViewModel() {
        viewModel.datalistPublicaciones.observe(viewLifecycleOwner) { publicaciones ->
            adapter.actualizarLista(publicaciones)
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ConexionService_data.url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun listarNombreFuncion() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val call = getRetrofit().create(ConexionService_data::class.java).consultaUsuario()
                if (call.isSuccessful && call.body() != null) {
                    withContext(Dispatchers.Main) {
                        // Aquí usamos el nuevo método agregarHilos
                        viewModel.agregarHilos(call.body()!!)
                    }
                } else {
                    Log.e("dap", "Error: No se encontró información")
                }
            } catch (e: Exception) {
                Log.e("dap", "No se pudo conectar al backend", e)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
