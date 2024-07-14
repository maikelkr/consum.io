package com.example.consumapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.consumapp.databinding.ActivityStartScreenBinding

class StartScreen : AppCompatActivity() {
    private lateinit var binding: ActivityStartScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartScreenBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.addVehicleBtn.setOnClickListener {
            val addVehicleScreen = Intent(this, AddVehicleScreen::class.java)
            startActivity(addVehicleScreen)
            finish()
        }
    }
}