package com.example.consumapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.consumapp.adapter.VehAdapter
import com.example.consumapp.databinding.ActivityStartScreenBinding
import com.example.consumapp.helper.FirebaseHelper
import com.example.consumapp.model.VehicleModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class StartScreen : AppCompatActivity() {
    private lateinit var binding: ActivityStartScreenBinding
    private var vehicleItem: VehicleModel = VehicleModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartScreenBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        FirebaseHelper.isAutenticated()
        showVehicle()
        binding.addVehicleBtn.setOnClickListener {
            val addVehicleScreen = Intent(this, AddVehicleScreen::class.java)
            addVehicleScreen.putExtra("funToDo", 1)
            startActivity(addVehicleScreen)
            finish()
        }
        /*findViewById<TextView>(R.id.remove_button).setOnClickListener {
            val saveVehicleScreen = Intent(this, AddVehicleScreen::class.java)
            saveVehicleScreen.putExtra("funToDo", 0)
            saveVehicleScreen.putExtra("vehicleId", vehicle.id)
            saveVehicleScreen.putExtra("vehicleKey", vehicle.key)
            startActivity(saveVehicleScreen)
            finish()
        }*/
    }
    private fun showVehicle() {
        Firebase.firestore.collection("users")
        .document(FirebaseAuth.getInstance().currentUser!!.uid).collection("vehicles")
        .get()
        .addOnSuccessListener{
            query ->
            val recyclerView = findViewById<RecyclerView>(R.id.view_holder)
            val vehicleList = mutableListOf<VehicleModel>()
            val adapter = VehAdapter(this, vehicleList)
            recyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter
            vehicleList.clear()
            for (document in query) {
                if (document != null) {
                    vehicleItem = VehicleModel(
                        document.data["id"]!!.toString().toInt(),
                        document.data["key"]!!.toString(),
                        document.data["model"]!!.toString(),
                        document.data["brand"]!!.toString(),
                        document.data["category"]!!.toString(),
                        document.data["age"]!!.toString(),
                        document.data["kmActual"]!!.toString(),
                        document.data["consum"]!!.toString(),
                        document.data["gasSize"]!!.toString()
                    )
                }
                println(vehicleItem)
                vehicleList.add(vehicleItem)
            }
        }.addOnFailureListener {
                Toast.makeText(applicationContext,"Você precisa fazer o login.", Toast.LENGTH_SHORT).show()
                val loginScreen = Intent(this, LoginScreen::class.java)
                startActivity(loginScreen)
                finish()
        }
    }
}