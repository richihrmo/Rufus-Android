package com.developers.team100k.rufus.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.developers.team100k.rufus.R
import com.developers.team100k.rufus.entity.Headline
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * Created by Richard Hrmo.
 */
class TabLayoutFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var data: List<Headline>
    val eventBus = EventBus.getDefault()

    override fun onStart() {
        super.onStart()
        eventBus.register(this)
    }

    override fun onStop() {
        super.onStop()
        eventBus.unregister(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView : ViewGroup = inflater.inflate(R.layout.fragment_recycler_view, container, false) as ViewGroup
        recyclerView = rootView.findViewById(R.id.vertical_recyclerview)
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = RecyclerAdapter(listOf())

        return rootView
    }

    @Subscribe(sticky = true)
    fun onEvent(headlines: List<Headline>){
        data = headlines
        update()
    }

    fun update(){
        val adapter = RecyclerAdapter(data)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}