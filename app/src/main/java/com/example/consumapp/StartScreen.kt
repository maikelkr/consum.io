package com.example.consumapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.consumapp.databinding.ActivityStartScreenBinding
import com.example.consumapp.model.VehicleModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class StartScreen : AppCompatActivity() {
    private lateinit var binding: ActivityStartScreenBinding
    var vehicle = VehicleModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartScreenBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        showVehicle()
        binding.addVehicleBtn.setOnClickListener {
            val addVehicleScreen = Intent(this, AddVehicleScreen::class.java)
            addVehicleScreen.putExtra("funToDo", 1)
            startActivity(addVehicleScreen)
            finish()
        }
        binding.removeButton.setOnClickListener {
            val saveVehicleScreen = Intent(this, AddVehicleScreen::class.java)
            saveVehicleScreen.putExtra("funToDo", 0)
            saveVehicleScreen.putExtra("vehicleId", vehicle.id)
            startActivity(saveVehicleScreen)
            finish()
        }
    }
    private fun showVehicle() {

        val userAuth = FirebaseAuth.getInstance().currentUser!!.uid
        val db = Firebase.firestore
        val refDb = db.collection("users").document(userAuth).collection("vehicles")
        refDb.get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot) {
                if (document != null) {
                    val id = document.data?.get("id")!!.toString()
                    vehicle.id = id.toInt()
                    val model = document.data?.get("model")?.toString()
                    val brand = document.data?.get("brand")?.toString()
                    val age = document.data?.get("age")?.toString()
                    binding.vehicleName.text = "$brand $model"
                    binding.vehicleAge.text = "Ano $age"

                    // val age = document.getString("age")

                }
            }
        }
    }
}