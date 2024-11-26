package com.example.myapplication.view

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activitysplash)

        // Configurar animación
        val imageView: ImageView = findViewById(R.id.imageView)
        val slideAnimation = ObjectAnimator.ofFloat(imageView, "translationY", -300f, 0f)
        slideAnimation.duration = 5000
        slideAnimation.start()

        // Iniciar temporizador para redirigir al LoginRegisterActivity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginRegisterActivity::class.java)
            startActivity(intent)
            finish()
        }, 5000) // Tiempo del splash (5 segundos)
    }
}
