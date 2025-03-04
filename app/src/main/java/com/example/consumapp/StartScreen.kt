package com.example.consumapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.consumapp.adapter.VehAdapter
import com.example.consumapp.databinding.ActivityStartScreenBinding
import com.example.consumapp.model.VehicleModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class StartScreen : AppCompatActivity() {
    private lateinit var binding: ActivityStartScreenBinding
    private var vehicleItem: VehicleModel = VehicleModel()
    private var toggled : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartScreenBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        /*FirebaseAuth.getInstance().currentUser?.let {
            startActivity(Intent(this,StartScreen::class.java))
            finish()
        }*/
        showVehicle()
        clickListeners()
    }
    private fun clickListeners(){

        binding.profilePicture.setOnClickListener {
            if (!toggled){
                toggleMenu(true)
                toggled
            }else{
                toggleMenu(false)
                !toggled
            }
        }
        binding.frameMenuExit.setOnClickListener {
            toggleMenu(false)
            toggled = false
        }
        binding.buttonProfile.setOnClickListener {
            val profileScreen = Intent(this, ProfileScreen::class.java)
            startActivity(profileScreen)
            finish()
        }
        binding.buttonLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val loginScreen = Intent(this, LoginScreen::class.java)
            startActivity(loginScreen)
            finish()
        }
        binding.addVehicleBtn.setOnClickListener {
            val addVehicleScreen = Intent(this, AddVehicleScreen::class.java)
            addVehicleScreen.putExtra("funToDo", 1)
            startActivity(addVehicleScreen)
            finish()
        }
    }
    private fun toggleMenu(toggled : Boolean){
        if (toggled){
            binding.frameMenu.visibility = View.VISIBLE
            binding.frameMenuExit.visibility = View.VISIBLE
        }else{
            binding.frameMenu.visibility = View.GONE
            binding.frameMenuExit.visibility = View.GONE
        }
    }
    private fun showVehicle(){
        Firebase.firestore.collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid).collection("vehicles").get().addOnSuccessListener { query ->
            val recyclerView = findViewById<RecyclerView>(R.id.view_holder)
            val vehicleList = mutableListOf<VehicleModel>()
            val adapter = VehAdapter(this, vehicleList)
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter
            vehicleList.clear()
            for (document in query) {
                if (document != null) {
                    vehicleItem = VehicleModel(
                        document.data["id"].toString().toInt(),
                        document.data["key"].toString(),
                        document.data["model"].toString(),
                        document.data["brand"].toString(),
                        document.data["category"].toString(),
                        document.data["age"].toString(),
                        document.data["kmActual"].toString(),
                        document.data["consum"].toString(),
                        document.data["gasSize"].toString()
                    )
                }
                vehicleList.add(vehicleItem)
                VehAdapter.onItemClick = {
                    val intent = Intent(this, VehicleDetails::class.java)
                    intent.putExtra("vehicleId", vehicleItem.id)
                    intent.putExtra("vehicleKey", vehicleItem.key)
                    println(vehicleItem.id)
                    println(vehicleItem.key)
                    println("=================================== 3")
                    intent.putExtra("funToDo", 0)
                    startActivity(intent)
                }
            }
        }
    }
}