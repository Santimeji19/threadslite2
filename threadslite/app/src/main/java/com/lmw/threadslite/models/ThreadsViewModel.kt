package com.lmw.threadslite.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class ThreadsViewModel : ViewModel() {

    private val _datalistHilo = MutableLiveData<MutableList<Publicaciones>>(mutableListOf())
    val datalistPublicaciones: LiveData<MutableList<Publicaciones>> get() = _datalistHilo

    // 🔹 Agregar un hilo individual
    fun agregarHilo(hilo: Publicaciones) {
        val currentList = _datalistHilo.value ?: mutableListOf()
        currentList.add(hilo)
        _datalistHilo.value = currentList
    }

    // 🔹 Reemplazar toda la lista (ej: carga desde API)
    fun agregarHilos(lista: List<Publicaciones>) {
        _datalistHilo.value = lista.toMutableList()
    }

    fun editPublicacione(hiloEditado: Publicaciones, position: Int) {
        val currentList = _datalistHilo.value ?: mutableListOf()
        if (position in currentList.indices) {
            currentList[position] = hiloEditado
            _datalistHilo.value = currentList
        }
    }

    fun deleteHilo(position: Int) {
        val currentList = _datalistHilo.value ?: mutableListOf()
        if (position in currentList.indices) {
            currentList.removeAt(position)
            _datalistHilo.value = currentList
        }
    }
}
