package com.example.covidapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.covidapplication.Common.Common2
import com.example.covidapplication.Model.PlaceDetail
import com.example.covidapplication.Remote.IGoogleAPIService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_view_place.*
import retrofit2.Call
import retrofit2.Response

class ViewPlace : AppCompatActivity() {

    internal lateinit var mService: IGoogleAPIService
    var mPlace: PlaceDetail?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_place)

        //Init Service
        mService= Common2.googleAPIService

        //set empty for all text views
        place_name.text=""
        place_address.text=""
        place_open_hour.text=""

        btn_show_map.setOnClickListener {
            //open map intent to view
            val mapIntent= Intent(Intent.ACTION_VIEW, Uri.parse(mPlace!!.result!!.url))
            startActivity(mapIntent)
        }

        //Load photo of place
        if(Common2.currentResult!!.photos!=null && Common2.currentResult!!.photos!!.size>0)
            Picasso.with(this)
                .load(getPhotoOfPlace(Common2.currentResult!!.photos!![0].photo_reference!!, 1000))
                .into(photo)
        //load rating
        if(Common2.currentResult!!.rating!=null)
            rating_bar.rating = Common2.currentResult!!.rating.toFloat()
        else
            rating_bar.visibility= View.GONE
        //Load open hours
        if(Common2.currentResult!!.opening_hours!=null)
            place_open_hour.text="Open Now: "+ Common2.currentResult!!.opening_hours!!.open_now
        else
            place_open_hour.visibility=View.GONE

        //Use Service to fetch add and name
        mService.getPlaceDetail(getPlaceDetailUrl(Common2.currentResult!!.place_id!!))
            .enqueue(object : retrofit2.Callback<PlaceDetail>{
                override fun onFailure(call: Call<PlaceDetail>, t: Throwable) {
                    Toast.makeText(baseContext, ""+t.message,Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<PlaceDetail>, response: Response<PlaceDetail>) {
                    mPlace=response!!.body()

                    place_address.text= mPlace!!.result!!.formatted_address
                    place_name.text= mPlace!!.result!!.name
                }

            })
    }

    private fun getPlaceDetailUrl(placeId: String): String {
        val url= StringBuilder("https://maps.googleapis.com/maps/api/place/details/json")
        url.append("?place_id=$placeId")
        url.append("&key=AIzaSyDzBUU4485mZpAQSYtHvSwjFssqb-Dmzgs")
        return url.toString()

    }

    private fun getPhotoOfPlace(photo_reference: String, maxWidth: Int): String {
        val url= StringBuilder("https://maps.googleapis.com/maps/api/place/details/photo")
        url.append("?maxwidth=$maxWidth")
        url.append("&phtoreference=$photo_reference")
        url.append("&key=AIzaSyDzBUU4485mZpAQSYtHvSwjFssqb-Dmzgs")
        return url.toString()
    }
}
