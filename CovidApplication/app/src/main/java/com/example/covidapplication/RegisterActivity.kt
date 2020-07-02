package com.example.covidapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_regster.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regster)
        auth = FirebaseAuth.getInstance()

        register_createacc.setOnClickListener {
            signUp()
        }

    }
    fun signUp(){
        if(
            register_email.text.toString().isEmpty()){
            register_email.error= " Please enter email"
            register_email.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(register_email.text.toString()).matches()){
            register_email.error="Please enter valid E-mail"
            register_email.requestFocus()
            return
        }
        if(register_password.text.toString().isEmpty()){
            register_password.error="Please enter Password"
            register_password.requestFocus()
            return
        }
        auth.createUserWithEmailAndPassword(register_email.text.toString(), register_password.text.toString()).addOnCompleteListener(this){
                task ->
            if (task.isSuccessful){
                val user= auth.currentUser
                user?.sendEmailVerification()
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }
                    }
            }else{
                Toast.makeText(baseContext, "SignUp failed.",
                    Toast.LENGTH_SHORT).show()

            }
        }
    }
}

