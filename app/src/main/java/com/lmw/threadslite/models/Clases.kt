package com.lmw.threadslite.models

import com.google.gson.annotations.SerializedName

data class Publicaciones(
    @SerializedName("publi_id")
    val publiId: Int = 0,
    @SerializedName("titulo")
    val titulo: String,
    @SerializedName("contenido")
    val contenido: String,
    @SerializedName("nombre_usuario")
    val nombreUsuario: String,
    @SerializedName("url_imagen")
    val url: String? = null // ¡Corrección aquí!
)

data class Usuario(
    @SerializedName("nombre_usuario")
    val nombre_usuario: String,
    @SerializedName("contraseña")
    val password: String
)
