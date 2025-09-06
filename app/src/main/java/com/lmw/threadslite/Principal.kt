package com.lmw.threadslite

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.lmw.threadslite.databinding.PrincipalBinding
import com.lmw.threadslite.fragments.FragmentListaHilos
import com.lmw.threadslite.fragments.FragmentPublicarHilo
import com.lmw.threadslite.viewmodel.ThreadsViewModel

class Principal : AppCompatActivity() {

    private lateinit var binding: PrincipalBinding
    private val viewModel: ThreadsViewModel by viewModels()
    private var nombreUsuario: String = "Anónimo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inicializar()
        configurarEventos()
        CerrarSesion()
    }

    private fun inicializar() {
        // Obtener el nombre de usuario del Intent. Si no existe, se usa "Anónimo".
        nombreUsuario = intent.getStringExtra("usuario") ?: "Anónimo"
        binding.tvUsuario.text = "Bienvenido $nombreUsuario"

        // Mostrar el fragmento principal de la lista de hilos al iniciar la actividad
        mostrarFragmentoListaHilos()
        viewModel.listarPublicaciones()
    }

    private fun mostrarFragmento(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            // Usamos el ID correcto de tu layout para el contenedor de fragmentos
            .replace(R.id.fragmentContainerView, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun mostrarFragmentoListaHilos() {
        val fragmentoLista = FragmentListaHilos()
        mostrarFragmento(fragmentoLista)
    }

    private fun configurarEventos() {
        binding.anadirHilo.setOnClickListener {
            // Pasamos el nombre de usuario al nuevo fragmento
            val fragmentoPublicar = FragmentPublicarHilo.newInstance(nombreUsuario)
            mostrarFragmento(fragmentoPublicar)
        }
    }

    private fun CerrarSesion() {
        binding.btnCerrarsesion.setOnClickListener {
            mostrarDialogoConfirmacionCierreSesion()
        }
    }

    private fun mostrarDialogoConfirmacionCierreSesion() {
        AlertDialog.Builder(this)
            .setTitle("Cerrar Sesión")
            .setMessage("¿Estás seguro de que quieres cerrar sesión?")
            .setPositiveButton("Sí") { dialog, which ->
                cerrarSesion()
            }
            .setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }
    private fun cerrarSesion() {
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}

