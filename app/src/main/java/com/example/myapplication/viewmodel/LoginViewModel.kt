package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.model.UserRequest
import com.example.myapplication.model.UserResponse
import com.example.myapplication.repository.LoginRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    private val repository = LoginRepository()
    private val _isRegister = MutableLiveData<UserResponse>()
    val isRegister: LiveData<UserResponse> = _isRegister

    fun registerUser(userRequest: UserRequest) {
        repository.registerUser(userRequest) { userResponse ->
            _isRegister.value = userResponse
        }
    }

    fun loginUser(email: String, pass: String, isLogin: (Boolean) -> Unit) {
        // Simulación de autenticación (reemplazar con lógica real)
        if (email == "test@example.com" && pass == "1234") {
            isLogin(true) // Login exitoso
        } else {
            isLogin(false) // Login fallido
        }
    }
}
