package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    fun loginUser(email: String, pass: String, isLogin: (Boolean) -> Unit) {
        // Simulación de autenticación (reemplazar con lógica real)
        if (email == "test@example.com" && pass == "1234") {
            isLogin(true) // Login exitoso
        } else {
            isLogin(false) // Login fallido
        }
    }
}
