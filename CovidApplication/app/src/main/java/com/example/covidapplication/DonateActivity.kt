package com.example.covidapplication

import android.R.attr
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.collections.ArrayList


class DonateActivity : AppCompatActivity() {
        var amountEt: EditText? =null
        var noteEt:EditText? = null
        var nameEt:EditText? = null
        var upiIdEt:EditText? = null
        val  UPI_PAYMENT = 0
    lateinit var send: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donate)
        send= findViewById(R.id.send)

        initializeViews()


        send.setOnClickListener (object : View.OnClickListener{
            override fun onClick(view: View) {
                //Getting the values from the EditTexts
                val amount:String= amountEt?.text.toString()
                val note: String = noteEt?.text.toString()
                val name:String = nameEt?.text.toString()
                val upiId:String = upiIdEt?.text.toString()
                payUsingUpi(amount, upiId, name, note)
            }
        })
    }
    fun initializeViews(){
        send = findViewById(R.id.send);
        amountEt = findViewById(R.id.amount_et);
        noteEt = findViewById(R.id.note);
        nameEt = findViewById(R.id.name);
        upiIdEt = findViewById(R.id.upi_id);
    }
    fun payUsingUpi(amount: String, upiId: String, name: String, note: String) {
        val uri: Uri = Uri.parse("upi://pay").buildUpon()
            .appendQueryParameter("pa", upiId)
            .appendQueryParameter("pn", attr.name.toString())
            .appendQueryParameter("tn", note)
            .appendQueryParameter("am", amount)
            .appendQueryParameter("cu", "INR")
            .build()


        val upiPayIntent = Intent(Intent.ACTION_VIEW)
        upiPayIntent.data = uri

        // will always show a dialog to user to choose an app
        // will always show a dialog to user to choose an app
        val chooser = Intent.createChooser(upiPayIntent, "Pay with")

        // check if intent resolves
        // check if intent resolves
        if (null != chooser.resolveActivity(packageManager)) {
            startActivityForResult(chooser, UPI_PAYMENT)
        } else {
            Toast.makeText(
                this@DonateActivity,
                "No UPI app found, please install one to continue",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);

        when (requestCode) {
            UPI_PAYMENT -> if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                if (data != null) {
                    val trxt: String = data.getStringExtra("response")
                    Log.d("UPI", "onActivityResult: " + trxt)
                    val dataList: ArrayList<String> = ArrayList()
                    dataList.add(trxt)
                    upiPaymentDataOperation(dataList);
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null");
                    val dataList: ArrayList<String> = ArrayList()
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
            } else {
                Log.d(
                    "UPI",
                    "onActivityResult: " + "Return data is null"
                ); //when user simply back without payment
                val dataList: ArrayList<String> = ArrayList()
                dataList.add("nothing");
                upiPaymentDataOperation(dataList);
            }

        }
    }
    private fun upiPaymentDataOperation(data: ArrayList<String>) {
        if (isConnectionAvailable(this@DonateActivity)) {
            var str = data[0]
            Log.d("UPIPAY", "upiPaymentDataOperation: $str")
            var paymentCancel = ""
            if (str == null) str = "discard"
            var status = ""
            var approvalRefNo = ""
            val response = str.split("&").toTypedArray()
            for (i in response.indices) {
                val equalStr =
                    response[i].split("=").toTypedArray()
                if (equalStr.size >= 2) {
                    if (equalStr[0].toLowerCase(Locale.ROOT) == "Status".toLowerCase(Locale.ROOT)) {
                        status = equalStr[1].toLowerCase(Locale.ROOT)
                    } else if (equalStr[0].toLowerCase(Locale.ROOT) == "ApprovalRefNo".toLowerCase(Locale.ROOT)|| equalStr[0].toLowerCase(Locale.ROOT) == "txnRef".toLowerCase(Locale.ROOT)
                    ) {
                        approvalRefNo = equalStr[1]
                    }
                } else {
                    paymentCancel = "Payment cancelled by user."
                }
            }
            if (status == "success") { //Code to handle successful transaction here.
                Toast.makeText(this@DonateActivity, "Transaction successful.", Toast.LENGTH_SHORT)
                    .show()
                Log.d("UPI", "responseStr: $approvalRefNo")
            } else if ("Payment cancelled by user." == paymentCancel) {
                Toast.makeText(this@DonateActivity, "Payment cancelled by user.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(
                    this@DonateActivity,
                    "Transaction failed.Please try again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                this@DonateActivity,
                "Internet connection is not available. Please check and try again",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    fun  isConnectionAvailable(context: Context):Boolean {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo: NetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()
            && netInfo.isConnectedOrConnecting()
            && netInfo.isAvailable()) {
            return true
        }
        return false
    }
}

