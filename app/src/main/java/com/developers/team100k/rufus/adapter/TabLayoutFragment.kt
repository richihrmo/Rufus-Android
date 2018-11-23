package com.developers.team100k.rufus.adapter

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.annotation.XmlRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.developers.team100k.rufus.MainActivity
import com.developers.team100k.rufus.R
import com.developers.team100k.rufus.ShowActivity
import com.developers.team100k.rufus.entity.Headline
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by Richard Hrmo.
 */
class TabLayoutFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var data: List<Headline>
    lateinit var adapter: RecyclerAdapter
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
        val refresh = rootView.findViewById<SwipeRefreshLayout>(R.id.swipe)
        refresh.setOnRefreshListener {
            //TODO call data update
            refresh.isRefreshing = true
            val runnable = Runnable {
                refresh.isRefreshing = false
            }
            Handler().postDelayed(runnable, 10000)
            update()
        }

        return rootView
    }

    @Subscribe(sticky = true)
    fun onEvent(headlines: List<Headline>){
        data = headlines
        update()
    }

    fun update(){
        adapter = RecyclerAdapter(data)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}