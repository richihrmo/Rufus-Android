package com.developers.team100k.rufus

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.developers.team100k.rufus.entity.Dialog
import com.developers.team100k.rufus.entity.Headline
import com.developers.team100k.rufus.processing.ArticlesParser
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.observers.DefaultObserver
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.greenrobot.eventbus.EventBus

/**
 * Created by Richard Hrmo.
 */
class ActivityViewModel : ViewModel() {

    lateinit var headlines: MutableLiveData<List<Headline>>
    private val databaseReference = FirebaseDatabase.getInstance().reference
    private val eventBus = EventBus.getDefault()

    fun getHeadlines(): LiveData<List<Headline>> {
        if (!::headlines.isInitialized) {
            headlines = MutableLiveData()
            loadHeadlines()
        }
        return headlines
    }

    fun updateHeadlines() {
        loadHeadlines()
    }

    private fun loadHeadlines() {
        eventBus.postSticky(Dialog.SHOW)
            val articlesParser = ArticlesParser(databaseReference)
                articlesParser.call()
                val articleObserver = object : DefaultObserver<Any>() {
                    override fun onNext(o: Any) {
                        val head = o as List<Headline>
                        headlines.value = head
                        headlines.postValue(head)
                        eventBus.postSticky(Dialog.DISMISS)
                        Log.e("headline", headlines.value.toString())
                    }

                    override fun onError(e: Throwable) {
                        Log.e("Observer", "onError")
                    }

                    override fun onComplete() {
                        Log.e("Observer", "onComplete")
                    }
                }
            articlesParser.data.subscribe(articleObserver)
    }



    override fun onCleared() {
        super.onCleared()
        Log.e("VM", "Cleared.")
    }
}