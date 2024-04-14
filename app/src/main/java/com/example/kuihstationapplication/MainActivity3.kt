package com.example.kuihstationapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kuihstationapplication.databinding.ActivityMain3Binding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity3 : AppCompatActivity() {
    private lateinit var binding: ActivityMain3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        var NavController = findNavController(R.id.fragmentContainerView)
        var bottomnav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomnav.setupWithNavController(NavController)

        binding.faqButton.setOnClickListener {
            val intent = Intent(this, FaqActivity::class.java)
            startActivity(intent)
        }
    }
}