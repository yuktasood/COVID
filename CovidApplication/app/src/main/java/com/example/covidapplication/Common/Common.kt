package com.example.covidapplication.Common

import com.example.covidapplication.Interface.NewsService
import com.example.covidapplication.Remote.RetrofitClient

object Common{
    val BASE_URL = "https://newsapi.org/"
    val API_KEY = "ccd29b0d4a6d469cb16e5bf096a5d942"

    val newsService: NewsService
      get() = RetrofitClient.getClient(BASE_URL).create(NewsService::class.java)

    fun getNewsAPI(source: String): String{
        val apiUrl = StringBuilder("https://newsapi.org/v2/top-headlines?sources=")
            .append(source)
            .append("&apiKey=")
            .append(API_KEY)
            .toString()
        return (apiUrl)
    }
}