package com.example.consumapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.consumapp.databinding.ActivityDialogConfirmDeleteBinding

class DialogConfirmDelete : AppCompatActivity() {
    private lateinit var binding: ActivityDialogConfirmDeleteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialogConfirmDeleteBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        /*FirebaseAuth.getInstance().currentUser?.let {
            startActivity(Intent(this,StartScreen::class.java))
            finish()
        }*/
    }
}