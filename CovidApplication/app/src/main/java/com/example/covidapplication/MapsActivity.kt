package com.example.covidapplication

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.covidapplication.Common.Common2
import com.example.covidapplication.Model.MyPlaces
import com.example.covidapplication.Remote.IGoogleAPIService
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_maps.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.jar.Manifest

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var latitude: Double=0.toDouble()
    private var longitude: Double=0.toDouble()
    private lateinit var mLastLocation: Location
    private var mMarker: Marker?=null

    //Location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    companion object{
        private const val MY_PERMISSION_CODE: Int = 1000
    }

    lateinit var mService: IGoogleAPIService
    internal lateinit var currentPlace: MyPlaces

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //Init Service
        mService= Common2.googleAPIService

        //Request Runtime Permission
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkLocationPermission()){
            buildLocationRequest()
            buildLocationCallBack()
            fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }}
    else{
            buildLocationRequest()
            buildLocationCallBack()
            fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        }

        bottom_navigation_view.setOnNavigationItemSelectedListener {item->
            when(item.itemId)
            {
                R.id.action_hospital-> nearByPlace("hospital")
                R.id.action_store-> nearByPlace("store")
                R.id.action_chemist->nearByPlace("chemist")
            }
            true

        }

    }

    private fun nearByPlace(typePlace: String) {

        //clear all marker on map
        mMap.clear()
        //build url req based on location
        val url = getUrl(latitude, longitude, typePlace)
        mService.getNearbyPlaces(url)
            .enqueue(object : Callback<MyPlaces>{
                override fun onFailure(call: Call<MyPlaces>, t: Throwable) {
                    Toast.makeText(baseContext, ""+t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<MyPlaces>, response: Response<MyPlaces>) {
                    currentPlace= response!!.body()!!

                    if(response.isSuccessful)
                    {
                        for (i in 0 until response!!.body()!!.results!!.size)
                        {
                            val markerOptions= MarkerOptions()
                            val googlePlace= response.body()!!.results!![i]
                            val lat= googlePlace.geometry!!.location!!.lat
                            val lng= googlePlace.geometry!!.location!!.lng
                            val placeName= googlePlace.name
                            val latLng= LatLng(lng, lat)

                            markerOptions.position(latLng)
                            markerOptions.title(placeName)
                            if(typePlace.equals("hospital"))
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            else if(typePlace.equals("store"))
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            else if(typePlace.equals("chemist"))
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            else
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

                            markerOptions.snippet(i.toString())//assign index for marker

                            //add marker to map
                            mMap!!.addMarker(markerOptions)
                            //move camera
                            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                            mMap!!.animateCamera(CameraUpdateFactory.zoomTo(11f))



                        }

                    }
                }
            })

    }

    private fun getUrl(latitude: Double, longitude: Double, typePlace: String): String {
        val googlePlaceUrl= StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
        googlePlaceUrl.append("?location=$latitude, $longitude")
        googlePlaceUrl.append("&radius=10000")//10 km
        googlePlaceUrl.append("&type=$typePlace")
        googlePlaceUrl.append("&key=AIzaSyDzBUU4485mZpAQSYtHvSwjFssqb-Dmzgs")

        Log.d("URL_DEBUG", googlePlaceUrl.toString())
        return googlePlaceUrl.toString()

    }

    private fun buildLocationCallBack() {
        locationCallback = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                mLastLocation= p0!!.locations.get(p0!!.locations.size-1)// get last location

                if(mMarker!=null){
                    mMarker!!.remove()
                }
                latitude= mLastLocation.latitude
                longitude= mLastLocation.longitude

                val latLng= LatLng(latitude,longitude)
                val markerOptions= MarkerOptions()
                    .position(latLng)
                    .title("YOUR LOCATION")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                mMarker= mMap!!.addMarker(markerOptions)

                //move Camera
                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap!!.animateCamera(CameraUpdateFactory.zoomTo(11f))
            }
        }
    }

    private fun buildLocationRequest() {
        locationRequest= LocationRequest()
        locationRequest.priority= LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval= 5000
        locationRequest.fastestInterval= 3000
        locationRequest.smallestDisplacement= 10f
    }

    private fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION))
                ActivityCompat.requestPermissions(this, arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),MY_PERMISSION_CODE)
            else
                ActivityCompat.requestPermissions(this, arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),MY_PERMISSION_CODE)
            return false
        }
        else
            return true
    }

    //override onRequestPermissionResult

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray ){
    when(requestCode)
    {
        MY_PERMISSION_CODE ->{
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED )
            {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                    if (checkLocationPermission()){
                        buildLocationRequest()
                        buildLocationCallBack()
                        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
                    }
                        mMap!!.isMyLocationEnabled= true
            }
        }
            else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
         }
    }
}

    override fun onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onStop()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // init google play services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                mMap!!.isMyLocationEnabled = true
            }
        } else
            mMap!!.isMyLocationEnabled = true

        //event click on marker
        mMap.setOnMarkerClickListener { marker ->
            Common2.currentResult= currentPlace!!.results!![Integer.parseInt(marker.snippet)]
            startActivity(Intent(this, ViewPlace::class.java))
            true
        }

        // enable zoom control
        mMap.uiSettings.isZoomControlsEnabled = true
    }
}
