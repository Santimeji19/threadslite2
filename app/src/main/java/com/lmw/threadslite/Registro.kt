package com.lmw.threadslite

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lmw.threadslite.databinding.RegistroBinding
import com.lmw.threadslite.models.Usuario
import com.lmw.threadslite.services.ConexionServiceData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Registro : AppCompatActivity() {

    private lateinit var binding: RegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = RegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        configurarBotones()
    }

    private fun configurarBotones() {
        binding.btnCancelar.setOnClickListener { cancelarRegistro() }
        binding.btnRegistro.setOnClickListener { registrarUsuario() }
    }

    private fun registrarUsuario() {
        val nombre = binding.etNombre.text.toString().trim()
        val apellido = binding.etApellido.text.toString().trim()
        val usuario = binding.etNombreUsuario.text.toString().trim()
        val contrasena = binding.etContrasenaRegistro.text.toString().trim()
        val confirmarContrasena = binding.etConfirmarContrasena.text.toString().trim()

        if (nombre.isEmpty() || apellido.isEmpty() || usuario.isEmpty() || contrasena.isEmpty() || confirmarContrasena.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (contrasena != confirmarContrasena) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiService = ConexionServiceData.create()
                val usuariosExistentes = apiService.getUsuarios().body()

                if (usuariosExistentes?.any { it.nombre_usuario == usuario } == true) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@Registro, "Este nombre de usuario ya está registrado", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }
                val nuevoUsuario = Usuario(nombre_usuario = nombre, password = contrasena)
                val response = apiService.addUsuario(nuevoUsuario)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@Registro, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        irALogin()
                    } else {
                        Toast.makeText(this@Registro, "Error al registrar: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Registro, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun irALogin() {
        startActivity(Intent(this, Login::class.java))
        finish()
    }

    private fun cancelarRegistro() {
        Toast.makeText(this, "Registro cancelado", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, Login::class.java))
        finish()
    }
}