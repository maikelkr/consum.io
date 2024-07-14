package com.example.consumapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.consumapp.databinding.ActivityMainBinding
import com.example.consumapp.helper.FirebaseHelper
import com.google.firebase.auth.FirebaseAuth

class LoginScreen : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseAuth.getInstance().currentUser?.let {
            startActivity(Intent(this,StartScreen::class.java))
            finish()
        }

        binding.textSignup.setOnClickListener {
            val signupScreen = Intent(this, SignupScreen::class.java)
            startActivity(signupScreen)
            finish()
        }
        binding.loginButton.setOnClickListener {
            login()
        }
    }
    fun progressLogin(inProgress : Boolean){
        if (inProgress){
            binding.progressLogin.visibility = View.VISIBLE
            binding.loginButton.visibility = View.GONE
        }else{
            binding.progressLogin.visibility = View.GONE
            binding.loginButton.visibility = View.VISIBLE
        }
    }
    fun login(){
        val email = binding.userMail.text.toString()
        val password = binding.userPassword.text.toString()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.userMail.setError("Digite um e-mail válido.")
            return
        }
        if (password.length<6){
            binding.userPassword.setError("A sua senha deve conter 6 ou mais caracteres.")
            return
        }
        loginWithFireBase(email, password)
    }
    fun loginWithFireBase(email : String, password : String){
        progressLogin(true)
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            email,
            password
        ).addOnSuccessListener {
            Toast.makeText(applicationContext,"Login efetuado", Toast.LENGTH_SHORT).show()
            progressLogin(false)
            startActivity(Intent(this,StartScreen::class.java))
            finish()

        }.addOnFailureListener {
            Toast.makeText(applicationContext, FirebaseHelper.validError(it.localizedMessage?: "Ocorreu um erro, tente novamente mais tarde."), Toast.LENGTH_SHORT).show()
            progressLogin(false)
        }
    }
}