package com.example.interviewapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit

class loginOTP : AppCompatActivity() {
    private lateinit var auth  : FirebaseAuth
    private lateinit var storedVerificationId : String
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var phoneNumber : String
    private lateinit var otp : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_otp)
        otp = findViewById(R.id.c1)
        auth = FirebaseAuth.getInstance()
         phoneNumber = intent.getStringExtra("phone").toString()
        sendVerificationCode(phoneNumber)
        val hello = findViewById<TextView>(R.id.textView6)
        hello.text = "We have sent you an SMS on +91${phoneNumber} \n with 6 digit verification Code"
        Toast.makeText(this, "Hi $phoneNumber", Toast.LENGTH_SHORT).show()
        val otpBtn = findViewById<Button>(R.id.OTPbutton)
        otpBtn.setOnClickListener {
            val code = otp.text.toString()
            verifyCode(code)
        }
    }

    private fun sendVerificationCode(phoneNumber : String) {

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {






            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            val code = credential.smsCode
            if(code!=null){
                otp.setText(code)
                verifyCode(code)
            }

        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Toast.makeText(this@loginOTP,e.toString(), Toast.LENGTH_SHORT).show()
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Toast.makeText(this@loginOTP,e.toString(), Toast.LENGTH_SHORT).show()
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            super.onCodeSent(verificationId, token)
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            Toast.makeText(this@loginOTP, "code sent", Toast.LENGTH_SHORT).show()
        }
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    storedata()
                    Toast.makeText(this, "Register", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this,Welcome::class.java)
                    startActivity(intent)

                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }

    private fun storedata() {
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("Users")
        val name = intent.getStringExtra("user")
        val user = User(name,phoneNumber)
        reference.child(name.toString()).setValue(user)
    }

    fun register(view: View) {
        Toast.makeText(this, "Hello World !!", Toast.LENGTH_SHORT).show()
    }

    private fun verifyCode(code: String?) {
        val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(storedVerificationId, code.toString())
        signInWithPhoneAuthCredential(credential)
    }
}