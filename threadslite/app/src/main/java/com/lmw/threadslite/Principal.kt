package com.lmw.threadslite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.lmw.threadslite.adapter.ThreadsAdapter
import com.lmw.threadslite.databinding.PrincipalBinding
import com.lmw.threadslite.models.Publicaciones
import com.lmw.threadslite.models.ThreadsViewModel
import com.lmw.threadslite.services.ConexionService_data
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Principal : AppCompatActivity() {

    private lateinit var binding: PrincipalBinding
    private lateinit var threadsAdapter: ThreadsAdapter
    private val viewModel: ThreadsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = PrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // ✅ Corrección: era systemBars.bars → ahora es systemBars.right
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.anadirHilo.setOnClickListener { mostrarPublicarHiloFragment() }
        binding.btnCerrarsesion.setOnClickListener { cerrarSesion() }
        inicializar()
        configurarRecyclerView()
        observarPublicaciones()
    }

    private fun inicializar() {
        val nombreUsuario = intent.getStringExtra("usuario") ?: "Anónimo"
        binding.tvUsuario.text = "Bienvenido $nombreUsuario"
    }

    private fun cerrarSesion() {
        Toast.makeText(this, "Sesión cerrada exitosamente", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, Login::class.java))
        finish()
        AlertDialog.Builder(this)
            .setTitle("Cerrar sesión")
            .setMessage("¿Estás seguro de que deseas cerrar sesión?")
            .setPositiveButton("cerrar sesión") { dialog, _ ->
                Toast.makeText(this, "sesión cerrada", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                }
                .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }


    private fun configurarRecyclerView() {
        threadsAdapter = ThreadsAdapter(
            hilo = emptyList(),
            onEditarClick = { hilo, position -> mostrarDialogoEditarHilo(hilo, position) },
            onEliminarClick = { hilo, position -> confirmarEliminacion(hilo, position) },
            onVerDetalleClick = { hilo ->
                Toast.makeText(this, "Autor: ${hilo.autor}\nContenido: ${hilo.contenido}", Toast.LENGTH_LONG).show()
            }
        )
        binding.recyclerViewPublicaciones.apply {
            layoutManager = LinearLayoutManager(this@Principal)
            adapter = threadsAdapter
        }
    }

    private fun observarPublicaciones() {
        viewModel.datalistPublicaciones.observe(this) { lista ->
            threadsAdapter.actualizarLista(lista)
        }
    }

    private fun mostrarPublicarHiloFragment() {
        binding.anadirHilo.visibility = View.GONE

        val fragment = FragmentPublicarHilo().apply {
            arguments = Bundle().apply {
                putString("nombre_usuario", binding.tvUsuario.text.toString().replace("Bienvenido ", ""))
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentPublicarHilo.id, fragment)
            .commit()
    }

    private fun mostrarDialogoEditarHilo(hilo: Publicaciones, position: Int) {
        val dialogBinding = com.lmw.threadslite.databinding.DialogAlertEditHiloBinding.inflate(layoutInflater)

        dialogBinding.ettitulo.setText(hilo.autor)
        dialogBinding.etdescripcion.setText(hilo.contenido)

        AlertDialog.Builder(this)
            .setTitle("Editar publicación")
            .setView(dialogBinding.root)
            .setPositiveButton("Guardar") { dialog, _ ->
                val nuevoAutor = dialogBinding.ettitulo.text.toString().trim()
                val nuevoContenido = dialogBinding.etdescripcion.text.toString().trim()

                if (nuevoAutor.isEmpty() || nuevoContenido.isEmpty()) {
                    Toast.makeText(this, "No puede haber campos vacíos", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val hiloEditado = hilo.copy(autor = nuevoAutor, contenido = nuevoContenido)
                viewModel.editPublicacione(hiloEditado, position)
                Toast.makeText(this, "Hilo editado", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun confirmarEliminacion(hilo: Publicaciones, position: Int) {
        AlertDialog.Builder(this)
            .setTitle("¿Eliminar publicación?")
            .setMessage("¿Estás seguro de que deseas eliminar este hilo?")
            .setPositiveButton("Eliminar") { dialog, _ ->
                viewModel.deleteHilo(position)
                Toast.makeText(this, "Hilo eliminado", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
        }
    fun observerViewModel() {
        viewModel.datalistPublicaciones.observe(viewLifecycleOwner) { Publicaciones ->
            ThreadsAdapter.Publicaciones = Publicaciones
            ThreadsAdapter.notifyDataSetChanged()
        }
    }


    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ConexionService_data.url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private fun listarNombreFuncion() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val call =
                    getRetrofit().create(ConexionService_data::class.java).consultaUsuario()
                if (call.isSuccessful && call.body() != null) {
                    withContext(Dispatchers.Main) {
                        ThreadsViewModel.agregarHilo(call.body()!!.toMutableList())
                    }
                } else {
                    Log.e("dap", "Error No se encontró información")
                }
            } catch (e: Exception) {
                Log.e("dap", "No se pudo conectar al backend", e)
            }
        }
    }
}
