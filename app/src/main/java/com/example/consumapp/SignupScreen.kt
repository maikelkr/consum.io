package com.example.consumapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.consumapp.databinding.ActivitySignupScreenBinding
import com.example.consumapp.helper.FirebaseHelper
import com.example.consumapp.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class SignupScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySignupScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupScreenBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        FirebaseAuth.getInstance().currentUser?.let {
            startActivity(Intent(this,StartScreen::class.java))
            finish()
        }
        binding.signupButton.setOnClickListener {
            signup()
        }
        binding.textLogin.setOnClickListener {
            val loginScreen = Intent(this, LoginScreen::class.java)
            startActivity(loginScreen)
            finish()
        }
    }
    fun progressSignup(inProgress : Boolean){
        if (inProgress){
            binding.progressSignup.visibility = View.VISIBLE
            binding.signupButton.visibility = View.GONE
        }else{
            binding.progressSignup.visibility = View.GONE
            binding.signupButton.visibility = View.VISIBLE
        }
    }
    fun signup(){
        val email = binding.userMail.text.toString()
        val password = binding.userPassword.text.toString()
        val confirmPassword = binding.userConfirmPassword.text.toString()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.userMail.setError("Digite um e-mail válido.")
            return
        }
        if (password.length<6){
            binding.userPassword.setError("A sua senha deve conter 6 ou mais caracteres.")
            return
        }
        if (confirmPassword!=password){
            binding.userConfirmPassword.setError("As senhas digitadas não correspondem.")
            return
        }
        signupWithFireBase(email, password)
    }
    fun signupWithFireBase(email : String, password : String){
        progressSignup(true)
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            it.user?.let { user->
                val userModel = UserModel( user.uid,email.substringBefore("@"),email)
                Firebase.firestore.collection("users").document(user.uid).set(userModel).addOnSuccessListener {
                    Toast.makeText(applicationContext,"Conta criada com sucesso.", Toast.LENGTH_SHORT).show()
                    progressSignup(false)
                    startActivity(Intent(this,StartScreen::class.java))
                    finish()
                }

            }
        }.addOnFailureListener {
            Toast.makeText(applicationContext, FirebaseHelper.validError(it.localizedMessage?: "Ocorreu um erro, tente novamente mais tarde."), Toast.LENGTH_SHORT).show()
            progressSignup(false)
        }
    }
}