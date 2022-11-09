package com.example.interviewapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val floatbtn = findViewById<FloatingActionButton>(R.id.floating1)
        floatbtn.setOnClickListener {
            val intent = Intent(this,Welcome::class.java)
            startActivity(intent)
        }

        val textView : TextView = findViewById(R.id.textView)
        textView.setOnClickListener{
            SignUp()
        }
    }

    fun SignUp() {
        val intent = Intent(this,SignUp::class.java)
        startActivity(intent)
    }
}