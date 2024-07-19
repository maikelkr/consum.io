package com.example.consumapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.consumapp.databinding.ActivityAddVehicleScreenBinding
import com.example.consumapp.databinding.ActivityStartScreenBinding
import com.example.consumapp.helper.FirebaseHelper
import com.example.consumapp.model.VehicleModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class StartScreen : AppCompatActivity() {
    private lateinit var binding: ActivityStartScreenBinding
    private lateinit var vehicle: VehicleModel
    private var newVehicle: Boolean = true
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
    private fun showVehicle(){
        Firebase.firestore
            .collection("users").document(FirebaseHelper.getIdUser() ?: "")
            .collection("vehicles").document(vehicle.id ?: "")
            .get(vehicle)
            .addOnCompleteListener{ vehicle ->
                if(vehicle.isSuccessful){
                    binding.vehicleName.text.toString() = vehicle.getResult()
                }else{

                }
            }.addOnFailureListener {
                Toast.makeText(applicationContext, "Veículo não foi adicionado.", Toast.LENGTH_SHORT).show()

            }
    }
}