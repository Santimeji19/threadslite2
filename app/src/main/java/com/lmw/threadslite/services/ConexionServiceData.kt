package com.lmw.threadslite.services

import com.lmw.threadslite.models.Usuario
import com.lmw.threadslite.models.Publicaciones
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface ConexionServiceData {

    @POST("login")
    @Headers("Content-Type: application/json")
    suspend fun login(@Body body: Usuario): Response<Usuario>

    @GET("consultaUsuarios")
    suspend fun getUsuarios(): retrofit2.Response<List<Usuario>>

    @POST("insertarUsuarios")
    @Headers("Content-Type: application/json")
    suspend fun addUsuario(@Body usuario: Usuario): Response<Usuario>

    @GET("consultaHiloPublicacion")
    suspend fun getPublicaciones(): retrofit2.Response<List<Publicaciones>>

    @POST("insertarHiloPublicacion")
    @Headers("Content-Type: application/json")
    suspend fun insertarPublicacion(@Body body: Publicaciones): Response<Publicaciones>

    @PUT("actualizarHiloPublicacion/{id}")
    @Headers("Content-Type: application/json")
    suspend fun actualizarPublicacion(@Path("id") id: Int, @Body body: Publicaciones): Response<Publicaciones>

    @DELETE("eliminarHiloPublicacion/{id}")
    suspend fun eliminarPublicacion(@Path("id") id: Int): Response<Void>

    companion object {
        private const val BASE_URL = "http://192.168.101.21:5050/"

        fun create(): ConexionServiceData {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ConexionServiceData::class.java)
        }
    }
}