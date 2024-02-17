package com.manish.hotelbookingapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.manish.hotelbookingapp.R
import com.manish.hotelbookingapp.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Loading Images
        loadImage(R.drawable.welcome_ic_1, binding.imgWelcome1)
        loadImage(R.drawable.welcome_ic_2, binding.imgWelcome2)
        loadImage(R.drawable.welcome_ic_3, binding.imgWelcome3)

        binding.btnNext.setOnClickListener {
            Log.d("TAGU", "onCreate: On Click next")

            val signUpIntent = Intent(this, SignUpActivity::class.java)
            startActivity(signUpIntent)
        }

        binding.btnLogin.setOnClickListener {
            Log.d("TAGU", "onCreate: On Click Login")

            val signUpIntent = Intent(this, LoginActivity::class.java)
            startActivity(signUpIntent)
        }
    }

    private fun loadImage(id: Int, view: ImageView) {
        Glide.with(this)
            .load(id)
            .placeholder(R.color.white)
            .into(view)
    }
}