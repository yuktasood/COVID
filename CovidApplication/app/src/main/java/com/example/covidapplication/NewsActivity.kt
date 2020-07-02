package com.example.covidapplication

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.covidapplication.Adapter.ViewHolder.ListSourceAdapter
import com.example.covidapplication.Common.Common
import com.example.covidapplication.Interface.NewsService
import com.example.covidapplication.Model.WebSite
import com.google.gson.Gson
import dmax.dialog.SpotsDialog
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_news.*
import retrofit2.Call
import retrofit2.Response

class NewsActivity : AppCompatActivity() {
    lateinit var layoutManager: LinearLayoutManager
    lateinit var mService: NewsService
    lateinit var adapter: ListSourceAdapter
    lateinit var dialog: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        //Init cache Db
        Paper.init(this)

        // Init Service
        mService= Common.newsService

        //Init View
        swipe_to_refresh.setOnRefreshListener {
            loadWebSiteSource(true)
        }

        recycler_view_source_news.setHasFixedSize(true)
        layoutManager= LinearLayoutManager(this)
        recycler_view_source_news.layoutManager = layoutManager

        dialog= SpotsDialog(this)
        loadWebSiteSource(false)


    }

    private fun loadWebSiteSource(isRefresh: Boolean) {
        if (!isRefresh)
        {
            val cache= Paper.book().read<String>("cache")
            if (cache!= null && !cache.isBlank() && cache != "null")
            {
                //Read Cache
                val webSite = Gson().fromJson<WebSite>(cache, WebSite::class.java)
                adapter= ListSourceAdapter(baseContext, webSite)
                recycler_view_source_news.adapter= adapter

            }
            else{
                // Load website and write cache
                dialog.show()
                //fetch new data
                mService.sources.enqueue(object : retrofit2.Callback<WebSite>{
                    override fun onResponse(call: Call<WebSite>, response: Response<WebSite>) {
                        adapter= ListSourceAdapter(baseContext, response!!.body()!!)
                        adapter.notifyDataSetChanged()
                        recycler_view_source_news.adapter= adapter

                        //Save to cache
                        Paper.book().write("cache", Gson().toJson(response!!.body()!!))

                        dialog.dismiss()
                    }

                    override fun onFailure(call: Call<WebSite>, t: Throwable) {
                        Toast.makeText(baseContext, "FAILED", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
        else
        {
            swipe_to_refresh.isRefreshing= true
            // fetch new data
            mService.sources.enqueue(object : retrofit2.Callback<WebSite>{
                override fun onResponse(call: Call<WebSite>, response: Response<WebSite>) {
                    adapter= ListSourceAdapter(baseContext, response!!.body()!!)
                    adapter.notifyDataSetChanged()
                    recycler_view_source_news.adapter= adapter

                    //Save to cache
                    Paper.book().write("cache", Gson().toJson(response!!.body()!!))

                    swipe_to_refresh.isRefreshing= false
                }

                override fun onFailure(call: Call<WebSite>, t: Throwable) {
                    Toast.makeText(baseContext, "FAILED", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
