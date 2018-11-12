package com.developers.team100k.rufus.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.developers.team100k.rufus.R
import com.developers.team100k.rufus.entity.Headline

/**
 * Created by Richard Hrmo.
 */
class RecyclerAdapter(private var dataSet: List<Headline>):
        RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        // create a new view
        val cardView = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_item, parent, false) as LinearLayout
        // set the view's size, margins, paddings and layout parameters
        return ViewHolder(cardView)
    }

//    override fun getItemCount() = dataSet.size
    override fun getItemCount() = dataSet.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.setText(dataSet[position].title)
        holder.subtitle.setText(dataSet[position].subtitle)
        holder.titleImage.setImageResource(R.drawable.ic_launcher_foreground)
    }

    class ViewHolder(val ll: LinearLayout) : RecyclerView.ViewHolder(ll){
        var title = ll.findViewById(R.id.title) as TextView
        var subtitle = ll.findViewById(R.id.subtitle) as TextView
        var titleImage = ll.findViewById(R.id.title_image) as ImageView
    }
}