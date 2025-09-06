package com.lmw.threadslite

// Crea un objeto para almacenar los datos de forma estática
object DataHolder {
    val listaUsuariosRegistrados = mutableListOf<Login.Usuario>()

    fun usuarioYaRegistrado(usuario: String): Boolean {
        return listaUsuariosRegistrados.any { it.usuario.equals(usuario, ignoreCase = true) }
    }
}