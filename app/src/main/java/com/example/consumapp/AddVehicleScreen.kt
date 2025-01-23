package com.example.consumapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.consumapp.databinding.ActivityAddVehicleScreenBinding
import com.example.consumapp.helper.FirebaseHelper
import com.example.consumapp.model.VehicleModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class AddVehicleScreen : AppCompatActivity() {
    private lateinit var binding: ActivityAddVehicleScreenBinding
    private var vehicle = VehicleModel()
    private var newVehicle: Boolean = true
    private var funToDo: Int = -1
    private var vehicleId: Int = -1
    private var vehicleKey: String = ""
    private var toggled : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVehicleScreenBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.backButton.setOnClickListener(){
            goStartScreen()
        }
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
        /*FirebaseAuth.getInstance().currentUser?.let {
            startActivity(Intent(this,StartScreen::class.java))
            finish()
        }*/
        funToDo = intent.getIntExtra("funToDo", 1)
        if (funToDo == 1) {
            newVehicle = true
            binding.vehicleAddText.setText("Adicionar veículo")
            vehicleKey = vehicle.key
            initListeners()
        } else if (funToDo == 0){
            newVehicle = false
            binding.vehicleAddText.setText("Atualizar veículo")
            binding.addVehicleButton.setText("Atualizar veículo")
            vehicleId = intent.getIntExtra("vehicleId", -1)
            vehicleKey = intent.getStringExtra("vehicleKey").toString()
            println(vehicleKey+" "+vehicleId+"=================================== 2")
            editVehicle()
        } else {
            binding.vehicleAddText.setText("Erro")
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
    private fun goStartScreen(){
        val startScreen = Intent(this, StartScreen::class.java)
        startActivity(startScreen)
        finish()
    }
    private fun progressSignup(inProgress : Boolean){
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
        val vehBrand = binding.vehicleBrandInput.text.toString().trim()
        val vehAge = binding.vehicleAgeInput.text.toString().trim()
        val vehConsum = binding.vehicleConsumInput.text.toString().trim()
        val vehCapacity = binding.vehicleCapacityInput.text.toString().trim()
        val vehKm = binding.vehicleKmInput.text.toString().trim()

        if (vehModel.isNotEmpty() || vehBrand.isNotEmpty() || vehAge.isNotEmpty() || vehConsum.isNotEmpty()){
            progressSignup(true)
            if (newVehicle == true) {
                vehicle = VehicleModel()
                Firebase.firestore
                .collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid)
                .collection("vehicles").get().addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot) {
                        if (document != null) {
                            val id = document.data.get("id")?.toString()
                            vehicle.id = (id?.toInt() ?: 0) + 1
                        }
                    }
                    vehicle.model = vehModel
                    vehicle.brand = vehBrand
                    vehicle.age = vehAge
                    vehicle.consum = vehConsum
                    vehicle.gasSize = vehCapacity
                    vehicle.kmActual = vehKm
                    saveVehicle()
                }
            } else {
                println(vehicleKey+" "+vehicleId+"=================================== 4")
                vehicle.id = vehicleId
                vehicle.key = vehicleKey
                vehicle.model = vehModel
                vehicle.brand = vehBrand
                vehicle.age = vehAge
                vehicle.consum = vehConsum
                vehicle.gasSize = vehCapacity
                vehicle.kmActual = vehKm
                saveVehicle()
            }
        }else{
            Toast.makeText(applicationContext,"Preencha todos os campos corretamente.", Toast.LENGTH_SHORT).show()
            progressSignup(false)
        }
    }
    private fun saveVehicle(){
        Firebase.firestore.collection("users")
            .document(FirebaseHelper.getIdUser() ?: "")
            .collection("vehicles")
            .document(vehicle.key)
            .set(vehicle)
            .addOnCompleteListener{ vehicle ->
            if(vehicle.isSuccessful){
                if (newVehicle){
                    Toast.makeText(applicationContext, "Veículo adicionado com sucesso.",Toast.LENGTH_SHORT).show()
                    goStartScreen()
                }else{
                    Toast.makeText(applicationContext, "Veículo atualizado com sucesso.",Toast.LENGTH_SHORT).show()
                    goStartScreen()
                }
            }else{
                Toast.makeText(applicationContext, "Veículo não foi adicionado.",Toast.LENGTH_SHORT).show()
                progressSignup(false)
            }
        }.addOnFailureListener {
            Toast.makeText(applicationContext, "Veículo não foi adicionado.",Toast.LENGTH_SHORT).show()
            progressSignup(false)
        }
        println(vehicle.key)
        println(vehicle.id)
        println("=================================== 5")
    }
    private fun editVehicle(){
        Firebase.firestore
            .collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid)
            .collection("vehicles").get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot) {
                if (document != null) {
                    if (document.data.get("id") as Long == vehicleId.toLong()){
                        val model = document.data.get("model")?.toString()
                        val brand = document.data.get("brand")?.toString()
                        val age = document.data.get("age")?.toString()
                        val consum = document.data.get("consum")?.toString()
                        val gasSize = document.data.get("gasSize")?.toString()
                        val kmActual = document.data.get("kmActual")?.toString()
                        //val category = document.data.get("category")?.toString()

                        binding.vehicleConsumInput.setText(consum)
                        binding.vehicleModelInput.setText(model)
                        binding.vehicleAgeInput.setText(age)
                        binding.vehicleBrandInput.setText(brand)
                        binding.vehicleCapacityInput.setText(gasSize)
                        binding.vehicleKmInput.setText(kmActual)
                        initListeners()
                    }
                }
            }
        }
    }
}


