package com.example.myapplication.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.content.Intent




class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed(Runnable () {
            runOnUiThread() {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        },5000)
    }
}