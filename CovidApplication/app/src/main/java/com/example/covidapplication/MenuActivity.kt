package com.example.covidapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_menu.*


class MenuActivity : AppCompatActivity() {
    val menuOptions= arrayOf<String>("VIEW CASES","PRECAUTION LIST", "CHECK LATEST NEWS", "FIND NEARBY ESSENTIALS ","SELF-ASSESSMENT TEST", "E-COMMERCE", "PAY TO PMCARES FUND","CONTACT CORONA HELPLINE")
    val imageId= arrayOf<Int>(
        R.drawable.globe, R.drawable.list, R.drawable.newss, R.drawable.loc,R.drawable.quiz, R.drawable.cart, R.drawable.payment, R.drawable.help
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val menuAdapter= MenuAdapter(this, menuOptions, imageId)
        list_view.adapter= menuAdapter
        list_view.setOnItemClickListener(){
            adapterView, view, position , id ->
            if(position==0){
                startActivity(Intent(this, GlobalActivity::class.java))
            }
            if (position==1)
            {
                startActivity(Intent(this, Precautions::class.java))
                finish()

            }
            if (position==2)
            {
                startActivity(Intent(this, NewsActivity::class.java))

            }
            if (position==3)
            {
                startActivity(Intent(this, MapsActivity::class.java))
            }
            if (position==4)
            {
                startActivity(Intent(this, QuizActivity::class.java))

            }
            if (position==5)
            {
                startActivity(Intent(this, HomeActivity::class.java))

            }
            if (position==6)
            {
                startActivity(Intent(this, DonateActivity::class.java))

            }
            else if(position==7)
            {
                startActivity(Intent(this, HelplineActivity::class.java))
            }
            if(position==8)
            {
                startActivity(Intent(this, GlobalActivity::class.java))
            }
        }
    }

    //logout option
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id: Int= item.itemId

        if (id== R.id.logout)
        {
            FirebaseAuth.getInstance().signOut()
            finish()
            startActivity(Intent(this, LoginActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }


}

