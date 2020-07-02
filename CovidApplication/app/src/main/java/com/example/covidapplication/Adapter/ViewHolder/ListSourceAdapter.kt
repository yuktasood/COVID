package com.example.covidapplication.Adapter.ViewHolder

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.covidapplication.Interface.ItemClickListener
import com.example.covidapplication.ListNews
import com.example.covidapplication.Model.WebSite
import com.example.covidapplication.R

class ListSourceAdapter (private val context: Context, private val webSite: WebSite): RecyclerView.Adapter<ListSourceViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSourceViewHolder {
        val inflater= LayoutInflater.from(parent!!.context)
        val itemView = inflater.inflate(R.layout.source_news_layout, parent,false)
        return ListSourceViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return webSite.sources!!.size
    }

    override fun onBindViewHolder(holder: ListSourceViewHolder, position: Int) {
    holder!!.source_titile.text= webSite.sources!![position].name
        holder.setItemClickListener(object : ItemClickListener
        {
            override fun onClick(view: View, position: Int) {
                        val intent = Intent(context, ListNews::class.java)
                            intent.putExtra("source", webSite.sources!![position].id)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(intent)
                    }
    })

}}