package com.example.consumapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.consumapp.databinding.ActivityVehicleDetailsBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class VehicleDetails : AppCompatActivity() {
    private lateinit var binding: ActivityVehicleDetailsBinding
    private var vehicleId: Int = -1
    private var vehicleKey: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVehicleDetailsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        /*FirebaseAuth.getInstance().currentUser?.let {
            startActivity(Intent(this,StartScreen::class.java))
            finish()
        }*/
        vehicleId = intent.getIntExtra("vehicleId", -1)
        vehicleKey = intent.getStringExtra("vehicleKey").toString()
        println(vehicleKey + " " + vehicleId + "=================================== 1")
        binding.editButton.setOnClickListener {
            val intent = Intent(this, AddVehicleScreen::class.java)
            intent.putExtra("funToDo", 0)
            intent.putExtra("vehicleId", vehicleId)
            intent.putExtra("vehicleKey", vehicleKey)
            startActivity(intent)
        }
        binding.backButton.setOnClickListener {
            val intent = Intent(this, StartScreen::class.java)
            startActivity(intent);
        }
        binding.removeButton.setOnClickListener {
            val intent = Intent(this, StartScreen::class.java)
            Firebase.firestore
                .collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid)
                .collection("vehicles").document(vehicleId.toString())
                .delete()
            startActivity(intent)
            finish()
        }
        showVehicle()
    }

    private fun showVehicle() {
        Firebase.firestore
            .collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid)
            .collection("vehicles").get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    if (document != null) {
                        if (document.data.get("id") as Long == vehicleId.toLong()) {
                            val model = document.data.get("model")?.toString()
                            val brand = document.data.get("brand")?.toString()
                            val age = document.data.get("age")?.toString()
                            val consum = document.data.get("consum")?.toString()
                            val gasSize = document.data.get("gasSize")?.toString()
                            val kmActual = document.data.get("kmActual")?.toString()
                            //val category = document.data.get("category")?.toString()
                            binding.vehicleName.text = model.toString()
                            binding.vehicleAge.text = age.toString()
                            binding.vehicleBrand.text = brand.toString()
                            binding.vehicleConsum.text = consum.toString()
                            binding.vehicleCapacity.text = gasSize.toString()
                            binding.vehicleKm.text = kmActual.toString()
                        }
                    }
                }
            }
    }

}
