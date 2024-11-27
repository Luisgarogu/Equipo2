package com.example.myapplication.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityLoginRegisterBinding
import com.example.myapplication.viewmodel.LoginViewModel
import com.example.myapplication.model.UserRequest

class LoginRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginRegisterBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_register)

        sharedPreferences = getSharedPreferences("shared", Context.MODE_PRIVATE)
        setup()
        sesion()
        viewModelObservers()
    }

    private fun viewModelObservers() {
        observerIsRegister()
    }

    private fun observerIsRegister() {
        loginViewModel.isRegister.observe(this) { userResponse ->
            if (userResponse.isRegister) {
                Toast.makeText(this, userResponse.message, Toast.LENGTH_SHORT).show()
                sharedPreferences.edit().putString("email",userResponse.email).apply()
                goToHome()
            } else {
                Toast.makeText(this, userResponse.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setup() {
        // Listener para el botón de inicio de sesión
        binding.btnLogin.setOnClickListener {
            loginUser()
        }

        // Listener para el texto de registro (si planeas usarlo)
        binding.tvRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val email = binding.emailLog.text.toString()
        val pass = binding.passLog.text.toString()
        val userRequest = UserRequest(email, pass)

        if (email.isNotEmpty() && pass.isNotEmpty()) {
            loginViewModel.registerUser(userRequest)
        } else {
            Toast.makeText(this, "Campos Vacíos", Toast.LENGTH_SHORT).show()
        }

    }

    private fun loginUser() {
        val email = binding.emailLog.text.toString()
        val password = binding.passLog.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            loginViewModel.loginUser(email, password) { isLogin ->
                if (isLogin) {
                    // Guardar email en SharedPreferences
                    sharedPreferences.edit().putString("email", email).apply()

                    // Redirigir al Home (MainActivity)
                    goToHome()
                } else {
                    Toast.makeText(this, "Login incorrecto", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToHome() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun sesion(){
        val email = sharedPreferences.getString("email",null)
        loginViewModel.sesion(email){ isEnableView ->
            if (isEnableView){
                binding.formlogin.visibility = View.INVISIBLE
                goToHome()
            }
        }
    }
}
