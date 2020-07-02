package com.example.covidapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        login_signup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
        login_submit.setOnClickListener {
            doLogin()
        }
    }
    private fun doLogin(){
        if(
            login_email.text.toString().isEmpty()){
            login_email.error= " Please enter valid email"
            login_email.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(login_email.text.toString()).matches()){
            login_email.error="Please enter valid E-mail"
            login_email.requestFocus()
            return
        }
        if(login_password.text.toString().isEmpty()){
            login_password.error="Please enter Password"
            login_password.requestFocus()
            return
        }
        auth.signInWithEmailAndPassword(login_email.text.toString(), login_password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val user = auth.currentUser
                    updateUI(user)
                } else {


                    updateUI(null)
                }

            }
    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser= auth.currentUser
        updateUI(currentUser)
    }
    private fun updateUI(currentUser: FirebaseUser?){
        if(currentUser!= null) {
            if (currentUser.isEmailVerified) {
                startActivity(Intent(this, StartingActivity::class.java))
            } else {
                Toast.makeText(baseContext, "Please verify your E-mail",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}


