package com.developers.team100k.rufus.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.developers.team100k.rufus.entity.Dialog
import com.developers.team100k.rufus.entity.Headline
import com.developers.team100k.rufus.processing.ArticlesParser
import com.developers.team100k.rufus.processing.HTMLParser
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.observers.DefaultObserver
import org.greenrobot.eventbus.EventBus

/**
 * Created by Richard Hrmo.
 * ViewModel tied to ShowActivity, used for processing of HTML data from Firebase
 */
class ShowViewModel : ViewModel() {

    lateinit var text: MutableLiveData<String>
    private val databaseReference = FirebaseDatabase.getInstance().reference
    private val eventBus = EventBus.getDefault()

    fun getHtml(string: String): LiveData<String> {
        if (!::text.isInitialized) {
            text = MutableLiveData()
            loadText(string)
        }
        return text
    }

    private fun loadText(string: String) {
        val htmlParser = HTMLParser(databaseReference, string)
        htmlParser.callDatabase()
        val htmlObserver = object : DefaultObserver<Any>() {
            override fun onNext(o: Any) {
                val head = o as String
                text.value = head
                text.postValue(head)
//                eventBus.postSticky(Dialog.DISMISS)
                Log.e("text", head)
            }

            override fun onError(e: Throwable) {
                Log.e("Observer", "onError")
            }

            override fun onComplete() {
                Log.e("Observer", "onComplete")
            }
        }
        htmlParser.data.subscribe(htmlObserver)
    }

    override fun onCleared() {
        super.onCleared()
        Log.e("VM", "Cleared.")
    }
}