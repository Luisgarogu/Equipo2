package com.example.myapplication.viewmodel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class SoundViewModel(application: Application): AndroidViewModel(application)  {
    private val _musicEnabled = MutableLiveData<Boolean>().apply { value = true }
    val musicEnabled: LiveData<Boolean> get() = _musicEnabled

    fun setMusicEnabled(enabled: Boolean) {
        _musicEnabled.value = enabled
    }
}