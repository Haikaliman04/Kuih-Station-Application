package com.example.kuihstationapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class StartActivity : AppCompatActivity() {

    //initialize all component
    private lateinit var buttonNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        //declare all component
        buttonNext = findViewById(R.id.btnNext)

        buttonNext.setOnClickListener{
            val i = Intent (this, LoginActivity::class.java)
            startActivity(i)
        }
    }
}