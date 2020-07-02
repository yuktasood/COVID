package com.example.covidapplication.Interface

import com.example.covidapplication.Model.News
import com.example.covidapplication.Model.WebSite
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface NewsService {
    @get: GET("v2/sources?apiKey=ccd29b0d4a6d469cb16e5bf096a5d942")
    val sources: Call<WebSite>

    @GET
    fun getNewsFromSource(@Url url: String ): Call<News>
}