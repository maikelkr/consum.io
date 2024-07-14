package com.example.consumapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.consumapp.databinding.ActivityAddVehicleScreenBinding

class AddVehicleScreen : AppCompatActivity() {
    private lateinit var binding: ActivityAddVehicleScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVehicleScreenBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.backButton.setOnClickListener(){
            val startScreen = Intent(this, StartScreen::class.java)
            startActivity(startScreen)
            finish()
        }
        initListeners()
    }
    fun progressSignup(inProgress : Boolean){
        if (inProgress){
            binding.progressAddVehicle.visibility = View.VISIBLE
            binding.addVehicleButton.visibility = View.GONE
        }else{
            binding.progressAddVehicle.visibility = View.GONE
            binding.addVehicleButton.visibility = View.VISIBLE
        }
    }
    private fun initListeners(){
        binding.addVehicleButton.setOnClickListener{ validateData() }
    }

    private fun validateData(){
        val vehModel = binding.vehicleModelInput.text.toString().trim()

        if (vehModel.isNotEmpty()){
            progressSignup(true)
        }else{
            Toast.makeText(applicationContext,"Digite o modelo do seu veículo.", Toast.LENGTH_SHORT).show()
            progressSignup(false)
        }
    }
}


