package com.example.consumapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.consumapp.databinding.ActivityProfileScreenBinding
import com.example.consumapp.helper.FirebaseHelper
import com.example.consumapp.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class ProfileScreen : AppCompatActivity() {
    private lateinit var binding: ActivityProfileScreenBinding
    private var user = UserModel()
    private var toggled : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileScreenBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
    /*FirebaseAuth.getInstance().currentUser?.let {
        startActivity(Intent(this,StartScreen::class.java))
        finish()
    }*/

    onClickListeners()
    editProfile()
}
private fun toggleMenu(toggled : Boolean){
    if (toggled){
        binding.frameDeleteProfile.visibility = View.VISIBLE
        binding.frameDeleteProfileBg.visibility = View.VISIBLE
    }else{
        binding.frameDeleteProfile.visibility = View.GONE
        binding.frameDeleteProfileBg.visibility = View.GONE
    }
}
private fun onClickListeners(){
    binding.backButton.setOnClickListener{
        goStartScreen()
    }
    /*binding.deleteProfileButton.setOnClickListener {
        val dialogConfirmDelete = Intent(this, DialogConfirmDelete::class.java)
        dialogConfirmDelete.putExtra("funToDo", 1)
        startActivity(dialogConfirmDelete)
        finish()
    }*/
    binding.deleteProfileButton.setOnClickListener {
        if (!toggled){
            toggleMenu(true)
            toggled
        }else{
            toggleMenu(false)
            !toggled
        }
    }
    binding.frameDeleteProfileBg.setOnClickListener {
        toggleMenu(false)
        toggled = false
    }
    binding.buttonCancel.setOnClickListener {
        toggleMenu(false)
        toggled = false
    }
    binding.buttonConfirm.setOnClickListener {
        Firebase.firestore.collection("users")
            .document(FirebaseHelper.getIdUser() ?: "")
            .delete()
        val signupScreen = Intent(this, SignupScreen::class.java)
        startActivity(signupScreen)
        finish()
    }
}
private fun goStartScreen(){
    val startScreen = Intent(this, StartScreen::class.java)
    startActivity(startScreen)
    finish()
}
private fun deleteProfile(){
    Firebase.firestore.collection("users")
        .document(FirebaseHelper.getIdUser() ?: "")
        .delete()
}
private fun initListeners(){
    binding.updateButton.setOnClickListener{ validateData() }
}

private fun validateData(){
    val userName = binding.userName.text.toString().trim()
    val userMail = binding.userMail.text.toString().trim()
    //val userPassword = binding.userPassword.text.toString().trim()

    user.username = userName
    user.email = userMail
    //user.password = userPassword
    saveProfile()

}
private fun saveProfile(){
    Firebase.firestore.collection("users")
        .document(FirebaseHelper.getIdUser() ?: "")
        .set(user)
        .addOnCompleteListener{ user ->
            if(user.isSuccessful){
                Toast.makeText(applicationContext, "@string/profile_update_success",Toast.LENGTH_SHORT).show()
                goStartScreen()
            }else{
                Toast.makeText(applicationContext, "@string/profile_update_error",Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(applicationContext, "@string/profile_update_error",Toast.LENGTH_SHORT).show()
        }
}
private fun editProfile() {
    Firebase.firestore
        .collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid)
        .get().addOnSuccessListener { document ->
            val userName = document.data?.get("username")?.toString()
            val userMail = document.data?.get("mail")?.toString()

            binding.userName.setText(userName.toString())
            binding.userMail.setText(userMail.toString())
            initListeners()
        }
    }
}