package com.example.covidapplication.Common

import com.example.covidapplication.Model.Results
import com.example.covidapplication.Remote.IGoogleAPIService
import com.example.covidapplication.Remote.RetrofitClient

object Common2 {
    private val GOOGLE_API_URL="https://maps.googleapis.com/"

    var currentResult: Results?=null
    val googleAPIService: IGoogleAPIService
        get()= RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService::class.java)
}