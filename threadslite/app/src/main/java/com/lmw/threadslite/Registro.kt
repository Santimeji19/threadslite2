package com.lmw.threadslite

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lmw.threadslite.databinding.RegistroBinding

class Registro: AppCompatActivity() {

    private lateinit var binding: RegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = RegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        configurarBotones()
    }

    // ✅ La clase Usuario ya está definida en la actividad Login, no es necesario duplicarla aquí.

    private fun configurarBotones() {
        binding.btnCancelar.setOnClickListener { cancelarRegistro() }
        binding.btnRegistro.setOnClickListener { registrarUsuario() }
    }

    private fun registrarUsuario() {
        val nombre = binding.etNombre.text.toString().trim()
        val usuario = binding.etNombreUsuario.text.toString().trim()
        val contrasena = binding.etContrasenaRegistro.text.toString().trim()
        val confirmarContrasena = binding.etConfirmarContrasena.text.toString().trim()

        if (nombre.isEmpty() || usuario.isEmpty() || contrasena.isEmpty() || confirmarContrasena.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (contrasena != confirmarContrasena) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        // ✅ Se usa el DataHolder para verificar si el usuario ya está registrado
        if (DataHolder.usuarioYaRegistrado(usuario)) {
            Toast.makeText(this, "Este usuario ya está registrado", Toast.LENGTH_SHORT).show()
            return
        }

        val nuevoUsuario = Login.Usuario(nombre, usuario, contrasena) // ✅ Se usa la clase Usuario de la actividad Login
        DataHolder.listaUsuariosRegistrados.add(nuevoUsuario) // ✅ Se añade el usuario a la lista del DataHolder

        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
        irALogin()
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