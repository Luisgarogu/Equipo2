package com.example.myapplication.view

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.content.Intent
import android.widget.ImageView
import com.example.myapplication.R


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activitysplash)
        val imageView: ImageView = findViewById(R.id.imageView)
        val slideAnimation = ObjectAnimator.ofFloat(imageView, "translationY", -300f, 0f)
        slideAnimation.duration = 5000
        slideAnimation.start()
        Handler().postDelayed(Runnable () {
            runOnUiThread() {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        },5000)
    }
}