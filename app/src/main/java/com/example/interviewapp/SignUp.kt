package com.example.interviewapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class SignUp : AppCompatActivity() {
    private lateinit var phone : EditText
    private lateinit var userName : EditText
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        phone = findViewById(R.id.editTextTextPersonName3)
        userName = findViewById(R.id.editTextTextPersonName2)
        firebaseAuth= FirebaseAuth.getInstance()
        val floatingButton :FloatingActionButton = findViewById(R.id.floating2)
    }

    fun OTP(view: View) {
        val phone = phone.text!!.trim().toString()
        val user = userName.text.toString()
        if(phone.length!=10){
            Toast.makeText(this, "Enter a valid number", Toast.LENGTH_SHORT).show()
        }
        else{
            val phoneNumber = "+91$phone"
            val intent = Intent(this,loginOTP::class.java)
            intent.putExtra("phone",phoneNumber)
            intent.putExtra("user",user)
            startActivity(intent)
        }
    }
}