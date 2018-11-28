package com.developers.team100k.rufus.adapter

import android.os.Bundle
import android.os.Handler
import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.developers.team100k.rufus.ActivityViewModel
import com.developers.team100k.rufus.R
import com.developers.team100k.rufus.entity.Headline
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.Exception

/**
 * Created by Richard Hrmo.
 */
class TabLayoutFragment : Fragment() {

    lateinit var category: String
    lateinit var recyclerView: RecyclerView
    lateinit var activityViewModel: ActivityViewModel
    lateinit var data: List<Headline>
    lateinit var adapter: RecyclerAdapter
//    val eventBus = EventBus.getDefault()

    override fun onStart() {
        super.onStart()
//        eventBus.register(this)
        Log.e("Fragment", "start")
    }

    override fun onStop() {
        super.onStop()
//        eventBus.unregister(this)
        fragmentManager?.saveFragmentInstanceState(this)
        Log.e("Fragment", "stop")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = this.arguments
        category = bundle?.getString("category")!!
        activityViewModel = activity?.run {
            ViewModelProviders.of(this).get(ActivityViewModel::class.java)
        }?: throw Exception("Invalid Activity")
        Log.e("Fragment", "create")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        activity?.let {
            activityViewModel.getHeadlines().observe(this, Observer { list ->
                Log.e("Viewmodel", "data")
                data = when (category) {
                    "All" -> list
                    else -> list.filter { it.category == category }
                }
                update()
            })
        }

        val rootView : ViewGroup = inflater.inflate(R.layout.fragment_recycler_view, container, false) as ViewGroup
        recyclerView = rootView.findViewById(R.id.vertical_recyclerview)
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = RecyclerAdapter(listOf())
        val refresh = rootView.findViewById<SwipeRefreshLayout>(R.id.swipe)
        refresh.setOnRefreshListener {
            activityViewModel.updateHeadlines()
            refresh.isRefreshing = true
            val runnable = Runnable {
                refresh.isRefreshing = false
            }
            Handler().postDelayed(runnable, 1500)
        }

        return rootView
    }

    override fun onPause() {
        super.onPause()
        Log.e("Fragment", "pause")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("Fragment", "destroyview")
    }

//    @Subscribe(sticky = true)
//    fun onEvent(category: String){}

    private fun update(){
        adapter = RecyclerAdapter(data)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}