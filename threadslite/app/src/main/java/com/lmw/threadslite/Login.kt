package com.lmw.threadslite

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lmw.threadslite.databinding.LoginBinding

class Login : AppCompatActivity() {

    private lateinit var binding: LoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        configurarBotones()
    }

    // ✅ La clase de datos Usuario ahora debe ser accesible por las dos clases
    data class Usuario(val nombre: String, val usuario: String, val contrasena: String)

    private fun configurarBotones() {
        binding.btnEntrar1.setOnClickListener { iniciarsesion() }
        binding.btnRegistro.setOnClickListener { registrarUsuario() }
    }

    private fun iniciarsesion() {
        val usuario = binding.etUsuario1.text.toString().trim()
        val contrasena = binding.etContrasena1.text.toString().trim()

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // ✅ Se usa el DataHolder para buscar el usuario
        val usuarioEncontrado = DataHolder.listaUsuariosRegistrados.find { it.usuario == usuario }

        if (usuarioEncontrado != null && usuarioEncontrado.contrasena == contrasena) {
            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
            // ✅ Se llama a la función corregida y se le pasa el nombre de usuario
            iraPrincipalActivity(usuarioEncontrado.nombre)
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }
    }


    private fun iraPrincipalActivity(nombreUsuario: String) {
        val intent = Intent(this, Principal::class.java)
        // ✅ Se usa putExtra para adjuntar el nombre del usuario al Intent
        intent.putExtra("usuario", nombreUsuario)
        startActivity(intent)
        finish()
    }

    private fun registrarUsuario() {
        val intent = Intent(this, Registro::class.java)
        startActivity(intent)
    }
}