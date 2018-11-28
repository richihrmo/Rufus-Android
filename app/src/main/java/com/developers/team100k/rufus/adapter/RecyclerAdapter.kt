package com.developers.team100k.rufus.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.developers.team100k.rufus.R
import com.developers.team100k.rufus.entity.Headline



/**
 * Created by Richard Hrmo.
 */
class RecyclerAdapter(private var dataSet: List<Headline>):
        RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){

    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {

        context = parent.context
        // create a new view
        val cardView = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_item, parent, false) as LinearLayout
        // set the view's size, margins, paddings and layout parameters
        return ViewHolder(cardView)
    }

    //    override fun getItemCount() = dataSet.size
    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = dataSet[position].title
        holder.subtitle.text = dataSet[position].subtitle
        Glide.with(context!!)
                .load("https://thenypost.files.wordpress.com/2018/11/trump-judges.jpg?quality=90&strip=all&w=618&h=410&crop=1")
                .into(holder.titleImage)
        holder.category.text = dataSet[position].category
        holder.id.text = dataSet[position].id
    }

    inner class ViewHolder(private val ll: LinearLayout) : RecyclerView.ViewHolder(ll){
        var title = ll.findViewById(R.id.title) as TextView
        var subtitle = ll.findViewById(R.id.subtitle) as TextView
        var titleImage = ll.findViewById(R.id.title_image) as ImageView
        var category = ll.findViewById(R.id.category_text) as TextView
        var id = ll.findViewById(R.id.id) as TextView
        var save = ll.findViewById(R.id.save) as ImageButton
    }
}