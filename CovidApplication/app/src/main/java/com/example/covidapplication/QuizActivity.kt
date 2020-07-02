package com.example.covidapplication

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_quiz.*

class QuizActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var context: Context

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        submit.setOnClickListener {
            if (young.isChecked && q3_no.isChecked && q4_no.isChecked && q5_no.isChecked && q6_no.isChecked){
                val builder = AlertDialog.Builder(this)
                builder.setTitle("RESULT")
                builder.setMessage("YOUR INFECTION RISK IS LOW")
                builder.setPositiveButton("OK", { dialogInterface: DialogInterface, i: Int -> startActivity(Intent(this, MenuActivity::class.java))})
                builder.setNegativeButton("TRY TEST AGAIN", { dialogInterface: DialogInterface, i: Int -> startActivity(Intent(this, QuizActivity::class.java)) })
                builder.show()
        }
            else if (q5_yes.isChecked || q6_yes.isChecked){
                val builder = AlertDialog.Builder(this)
                builder.setTitle("RESULT")
                builder.setMessage("YOUR INFECTION RISK IS HIGH")
                builder.setPositiveButton("OK", { dialogInterface: DialogInterface, i: Int -> startActivity(Intent(this, MenuActivity::class.java))})
                builder.setNegativeButton("TRY TEST AGAIN", { dialogInterface: DialogInterface, i: Int -> startActivity(Intent(this, QuizActivity::class.java)) })
                builder.show()

            }
            else if (q3_yes.isChecked && q4_yes.isChecked){
                val builder = AlertDialog.Builder(this)
                builder.setTitle("RESULT")
                builder.setMessage("YOUR INFECTION RISK IS HIGH")
                builder.setPositiveButton("OK", { dialogInterface: DialogInterface, i: Int -> startActivity(Intent(this, MenuActivity::class.java))})
                builder.setNegativeButton("TRY TEST AGAIN", { dialogInterface: DialogInterface, i: Int -> startActivity(Intent(this, QuizActivity::class.java)) })
                builder.show()

            }
            else if (q3_yes.isChecked){
                val builder = AlertDialog.Builder(this)
                builder.setTitle("RESULT")
                builder.setMessage("YOU MAY BE INFECTED BY COVID-19. TAKE A TEST AND MAINTAIN SOCIAL DISTANICING")
                builder.setPositiveButton("OK", { dialogInterface: DialogInterface, i: Int -> startActivity(Intent(this, MenuActivity::class.java))})
                builder.setNegativeButton("TRY TEST AGAIN", { dialogInterface: DialogInterface, i: Int -> startActivity(Intent(this, QuizActivity::class.java)) })
                builder.show()

            }
            else if((middle.isChecked || old.isChecked|| veryold.isChecked) && q3_no.isChecked && q4_no.isChecked && q5_no.isChecked && q6_no.isChecked ){
                val builder = AlertDialog.Builder(this)
                builder.setTitle("RESULT")
                builder.setMessage("YOUR INFECTION RISK IS LOW")
                builder.setPositiveButton("OK", { dialogInterface: DialogInterface, i: Int -> startActivity(Intent(this, MenuActivity::class.java))})
                builder.setNegativeButton("TRY TEST AGAIN", { dialogInterface: DialogInterface, i: Int -> startActivity(Intent(this, QuizActivity::class.java)) })
                builder.show()
            }


    }}}

