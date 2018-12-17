package com.developers.team100k.rufus

import android.content.Intent
import android.os.Bundle
import android.os.Handler
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
import com.developers.team100k.rufus.viewmodel.ActivityViewModel
import com.developers.team100k.rufus.adapter.RecyclerAdapter
import com.developers.team100k.rufus.entity.Headline
import io.reactivex.observers.DefaultObserver
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
    lateinit var itemClickObserver: DefaultObserver<Any>
    lateinit var itemSaveObserver: DefaultObserver<Any>
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
        adapter = RecyclerAdapter(listOf())
        recyclerView.adapter = adapter
        val refresh = rootView.findViewById<SwipeRefreshLayout>(R.id.swipe)
        refresh.setOnRefreshListener {
            activityViewModel.updateHeadlines()
            refresh.isRefreshing = true
            val runnable = Runnable {
                refresh.isRefreshing = false
            }
            Handler().postDelayed(runnable, 1500)
        }
        itemClickObserver = object : DefaultObserver<Any>() {
            override fun onNext(o: Any) {
                val clicked = o as Headline
                val intent = Intent(context, ShowActivity::class.java)
                intent.putExtra("article_id", clicked.id)
                intent.putExtra("title", clicked.title)
                intent.putExtra("subtitle", clicked.subtitle)
                intent.putExtra("image", clicked.image)
                startActivity(intent)
                Log.e("ClickObserver", "ha")
            }

            override fun onError(e: Throwable) {
                Log.e("Observer", "onError")
            }

            override fun onComplete() {
                Log.e("Observer", "onComplete")
            }
        }
        adapter.onClickItem.subscribe(itemClickObserver)

        itemSaveObserver = object : DefaultObserver<Any>() {
            override fun onNext(o: Any) {
//                val clicked = o as Headline
//                val intent = Intent(context, ShowActivity::class.java)
//                intent.putExtra("article_id", clicked.id)
//                intent.putExtra("title", clicked.title)
//                intent.putExtra("subtitle", clicked.subtitle)
//                intent.putExtra("image", clicked.image)
//                startActivity(intent)
                Log.e("ClickObserver", "ha")
            }

            override fun onError(e: Throwable) {
                Log.e("Observer", "onError")
            }

            override fun onComplete() {
                Log.e("Observer", "onComplete")
            }
        }
        adapter.onClickSave.subscribe(itemSaveObserver)

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
        adapter.dataSet = data
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}