package com.lmw.threadslite

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lmw.threadslite.databinding.LoginBinding
import com.lmw.threadslite.models.Usuario
import com.lmw.threadslite.services.ConexionServiceData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Login : AppCompatActivity() {

    private lateinit var binding: LoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.loginLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        configurarBotones()
    }

    private fun configurarBotones() {
        binding.btnEntrar1.setOnClickListener { iniciarSesion() }
        binding.btnregistro.setOnClickListener { registrarUsuario() }
    }

    private fun iniciarSesion() {
        val usuario = binding.etUsuario1.text.toString().trim()
        val contrasena = binding.etContrasena1.text.toString().trim()

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiService = ConexionServiceData.create()
                val nuevoUsuario = Usuario(nombre_usuario = usuario, password = contrasena)
                val response = apiService.login(nuevoUsuario)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        Toast.makeText(this@Login, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                        irAPrincipalActivity(response.body()!!.nombre_usuario)
                    } else {
                        Toast.makeText(this@Login, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Login, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun irAPrincipalActivity(nombreUsuario: String) {
        val intent = Intent(this, Principal::class.java)
        intent.putExtra("usuario", nombreUsuario)
        startActivity(intent)
        finish()
    }

    private fun registrarUsuario() {
        val intent = Intent(this, Registro::class.java)
        startActivity(intent)
    }
}