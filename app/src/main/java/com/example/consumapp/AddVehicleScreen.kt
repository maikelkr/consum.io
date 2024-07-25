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
import com.google.firebase.firestore.ktx.firestore

class AddVehicleScreen : AppCompatActivity() {
    private lateinit var binding: ActivityAddVehicleScreenBinding
    var vehicle = VehicleModel()
    private var newVehicle: Boolean = true
    private var funToDo: Int = -1
    private var vehicleId: Int = -1
    private val refDbVehicles = com.google.firebase.ktx.Firebase.firestore.collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid).collection("vehicles")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVehicleScreenBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.backButton.setOnClickListener(){
            goStartScreen()
        }
        funToDo = intent.getIntExtra("funToDo", 1)
        vehicleId = intent.getIntExtra("vehicleId", -1)
        if (funToDo == 1) {
            newVehicle = true
            binding.vehicleAddText.setText("Adicionar veículo")
            initListeners()
        } else if (funToDo == 0){
            newVehicle = false
            binding.vehicleAddText.setText("Atualizar veículo")
            editVehicle()
        } else {
            binding.vehicleAddText.setText("Erro")
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


        if (vehModel.isNotEmpty() || vehBrand.isNotEmpty() || vehAge.isNotEmpty() || vehConsum.isNotEmpty()){
            progressSignup(true)
            if (newVehicle == true) {
                vehicle = VehicleModel()
                refDbVehicles.get().addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot) {
                        if (document != null) {
                            val id = document.data?.get("id")!!.toString()
                            vehicle.id = id.toInt() + 1
                            println("ID novo" + vehicle.id.toString())
                        }
                    }
                }
            }
            println("ID depois do for " + vehicle.id.toString())
            vehicle.model = vehModel
            vehicle.brand = vehBrand
            vehicle.age = vehAge
            vehicle.consum = vehConsum

            saveVehicle()
        }else{
            Toast.makeText(applicationContext,"Preencha todos os campos corretamente.", Toast.LENGTH_SHORT).show()
            progressSignup(false)
        }
    }
    private fun saveVehicle(){
        Firebase.firestore
            .collection("users").document(FirebaseHelper.getIdUser() ?: "")
            .collection("vehicles").document(vehicle.key)
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
    }
    private fun editVehicle(){
        refDbVehicles.get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot) {
                if (document != null) {
                    val id = document.data?.get("id")!!.toString()
                    val model = document.data?.get("model")?.toString()
                    val brand = document.data?.get("brand")?.toString()
                    val age = document.data?.get("age")?.toString()
                    binding.vehicleModelInput.setText(model)
                    binding.vehicleAgeInput.setText(age)
                    binding.vehicleBrandInput.setText(brand)
                    vehicleId = id.toInt()
                    initListeners()
                    // val age = document.getString("age")

                }
            }
        }
    }
}


