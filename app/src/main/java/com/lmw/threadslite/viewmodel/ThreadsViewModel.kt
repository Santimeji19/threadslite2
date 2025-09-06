package com.lmw.threadslite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lmw.threadslite.models.Publicaciones
import com.lmw.threadslite.services.ConexionServiceData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ThreadsViewModel : ViewModel() {

    private val _datalistPublicaciones = MutableLiveData<MutableList<Publicaciones>>(mutableListOf())
    val datalistPublicaciones: LiveData<MutableList<Publicaciones>> = _datalistPublicaciones

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val apiService = ConexionServiceData.create()

    // Función para obtener las publicaciones de la API
    fun listarPublicaciones() {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.getPublicaciones()
                }
                if (response.isSuccessful) {
                    val lista = response.body()?.toMutableList() ?: mutableListOf()
                    _datalistPublicaciones.postValue(lista)
                } else {
                    _errorMessage.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error de red: ${e.message}")
            }
        }
    }

    // Asegúrate de que esta función se llama después de cada operación
    fun agregarHilo(mPublicacion: Publicaciones) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.insertarPublicacion(mPublicacion)
                }
                if (response.isSuccessful) {
                    // Causa del error: Falta recargar la lista de publicaciones
                    listarPublicaciones()
                } else {
                    _errorMessage.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error de red: ${e.message}")
            }
        }
    }

    fun editPublicacion(hiloEditado: Publicaciones) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.actualizarPublicacion(hiloEditado.publiId, hiloEditado)
                }
                if (response.isSuccessful) {
                    listarPublicaciones()
                } else {
                    _errorMessage.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error de red: ${e.message}")
            }
        }
    }

    fun deleteHilo(id: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.eliminarPublicacion(id)
                }
                if (response.isSuccessful) {
                    listarPublicaciones()
                } else {
                    _errorMessage.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error de red: ${e.message}")
            }
        }
    }
}

