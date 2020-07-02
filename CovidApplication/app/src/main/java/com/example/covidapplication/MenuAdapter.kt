package com.example.covidapplication

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*

class MenuAdapter (private val context: Activity, private val menuOption: Array<String>, private val imageId: Array<Int>)
    : ArrayAdapter<String>(context, R.layout.row, menuOption){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater= context.layoutInflater
        val rowView= inflater.inflate(R.layout.row,null,true)
        val optionText= rowView.findViewById(R.id.menuOption)as TextView
        val imageView = rowView.findViewById(R.id.menu_image) as ImageView
        optionText.text = menuOption[position]
        imageView.setImageResource(imageId[position])

        return rowView

    }
}