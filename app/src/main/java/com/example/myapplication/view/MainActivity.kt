package com.example.myapplication.view


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.view.LoginRegisterActivity


class MainActivity : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val authToken = sharedPreferences.getString("auth_token", null)
        if (authToken != null) {
            // Ya hay sesion guarada
            setContentView(R.layout.activity_main)
        } else {
            // Iniciar sesion
            setContentView(R.layout.activity_login_register)
        }
        val editor = sharedPreferences.edit()

        editor.putString("auth_token", authToken) // Guardar el token de autenticación
        editor.apply()


        //setContentView(R.layout.activity_main)
    }
}

