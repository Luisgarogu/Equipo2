package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.Reto
import com.example.myapplication.model.Pokemon
import com.example.myapplication.repository.RetosRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RetosViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>()
    private val retosRepository = RetosRepository(context)
    private val _retosList = MutableLiveData<MutableList<Reto>>()
    val retosList: LiveData<MutableList<Reto>> get() = _retosList

    private val _randomReto = MutableLiveData<Reto?>()
    val randomReto: LiveData<Reto?> get() = _randomReto

    private val _pokemonList = MutableLiveData<List<Pokemon>>()
    val pokemonList: LiveData<List<Pokemon>> get() = _pokemonList

    private val _editRetoId = MutableLiveData<Int?>()
    val editRetoId: LiveData<Int?> get() = _editRetoId

    fun saveReto(reto: Reto) {
        viewModelScope.launch {
            retosRepository.saveReto(reto)
        }
    }

    fun getRetosList() {
        viewModelScope.launch {
            val retosList = retosRepository.getRetosList()
            // Convierte la lista de pares (documentId, Reto) a solo la lista de objetos Reto
            _retosList.value = retosList.map { it.second }.toMutableList()
        }
    }


    // RetosViewModel: Obtener y actualizar el reto aleatorio
    fun getRandomReto() {
        viewModelScope.launch {
            val randomReto = retosRepository.getRandomReto()
            _randomReto.value = randomReto  // Asigna el reto aleatorio a LiveData
        }
    }

    fun getPokemonlist() {
        viewModelScope.launch {
            _pokemonList.value = retosRepository.getPokemonList()
        }
    }

    fun getRetoById(documentId: String): Reto? {
        return runBlocking {
            retosRepository.getRetoById(documentId)
        }
    }

    fun updateRetoDescription(documentId: String, newDescription: String) {
        viewModelScope.launch {
            val retoToUpdate = retosRepository.getRetoById(documentId)

            // Aquí verificas que el reto existe, y luego actualizas su descripción
            if (retoToUpdate != null) {
                // Actualizamos solo la descripción del reto
                retosRepository.updateReto(documentId, newDescription)  // Llamada al repositorio con los parámetros correctos
                _retosList.value = retosRepository.getRetosList().map { it.second }.toMutableList()  // Actualizamos la lista después de la actualización
            }
        }
    }

    fun deleteReto(documentId: String) {
        viewModelScope.launch {
            retosRepository.deleteReto(documentId)

            // Después de eliminar el desafío, actualiza la lista
            getRetosList()
        }
    }

}