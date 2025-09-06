package com.lmw.threadslite.services

import com.lmw.threadslite.models.Publicaciones
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ConexionService_data {
        companion object{
            val url: String = "http://http://192.168.0.192:5050"
        }
        @GET("/consultaUsuario")
        suspend fun consultaUsuario (): retrofit2.Response<List<Publicaciones>>
        @POST("/insertarUsuario")
        suspend fun insertarUsuario (@Body data: Publicaciones):
                retrofit2.Response<Publicaciones>
        @PUT("/modificarUsuario")
        suspend fun modificarUsuario (@Body data: Publicaciones):
                retrofit2.Response<Publicaciones>
        @DELETE("/eliminarUsuario/{cajCc}")
        suspend fun eliminarUsuario (@Path("NombreCampoBdd ") NombreCampoBdd:
                                    String): retrofit2.Response<Any>
}