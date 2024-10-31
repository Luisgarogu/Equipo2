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
            _retosList.value = retosRepository.getRetosList()
        }
    }

    fun getRandomReto() {
        viewModelScope.launch {
            retosRepository.getRandomReto().collect { reto ->
                _randomReto.value = reto
            }
        }
    }

    fun getPokemonlist() {
        viewModelScope.launch {
            _pokemonList.value = retosRepository.getPokemonList()
        }
    }

    fun getRetoById(retoId: Int): Reto? {
        return runBlocking {
            retosRepository.getRetoById(retoId)
        }
    }

    // Método para actualizar la descripción de un Challenge

    fun updateRetoDescription(retoId: Int, newDescription: String) {
        viewModelScope.launch {
            // Obtener el desafío por ID
            val retoToUpdate = retosRepository.getRetoById(retoId)

            // Actualizar la descripción si se encontró el desafío
            if (retoToUpdate != null) {
                retoToUpdate.description = newDescription
                retosRepository.updateReto(retoToUpdate)

                // Actualizar la lista de desafíos después de la actualización
                _retosList.value = retosRepository.getRetosList()
            }
        }
    }

    fun deleteReto(retoId: Int) {
        viewModelScope.launch {
            retosRepository.deleteReto(retoId)

            // Después de eliminar el desafío, actualiza la lista
            getRetosList()
        }
    }

}