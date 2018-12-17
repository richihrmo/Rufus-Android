package com.developers.team100k.rufus.processing

import android.util.Log
import androidx.lifecycle.LiveData
import com.developers.team100k.rufus.entity.Article
import com.developers.team100k.rufus.entity.Headline
import com.developers.team100k.rufus.entity.Page
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import io.reactivex.subjects.PublishSubject

/**
 * Created by Richard Hrmo.
 */

class ArticlesParser(val mDatabase: DatabaseReference){

    lateinit var mapPosts: Map<String, Map<String, String>>
    lateinit var mapPostCont: Map<String, String>
    lateinit var mapCategories: Map<String, Map<String, String>>
    lateinit var mapTeam: Map<String, Map<String, String>>

    var donePost = false
    var doneCategory = false
    var doneContent = false
    var doneTeam = false


    lateinit var listOfArticles: MutableList<Headline>
    var data = PublishSubject.create<Any>()
    val jsonParser = JsonParser()

    fun call() {
        callDatabase()
        secondCall()
        categoryCall()
        authorCall()
    }

    fun callDatabase(){
        mDatabase.child("posts").orderByChild("status").equalTo("published").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mapPosts = dataSnapshot.getValue(false) as Map<String, Map<String, String>>
                donePost = true
                allDone()
                Log.e("Firebase", mapPosts.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Fail to read.")
            }
        })
    }

    fun secondCall(){
        mDatabase.child("postContents").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mapPostCont = dataSnapshot.getValue(false) as Map<String, String>
                listOfArticles = ArrayList()
                doneContent = true
                allDone()
                Log.e("Firebase", listOfArticles.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Fail to read.")
            }
        })
    }

    fun categoryCall(){
        mDatabase.child("categories").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mapCategories = dataSnapshot.getValue(false) as Map<String, Map<String, String>>
                doneCategory = true
                allDone()
                Log.e("Firebase", mapCategories.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Fail to read.")
            }
        })
    }

    fun authorCall(){
        mDatabase.child("team").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mapTeam = dataSnapshot.getValue(false) as Map<String, Map<String, String>>
                doneTeam = true
                allDone()
                Log.e("Firebase", mapTeam.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Fail to read.")
                mapTeam = mutableMapOf()
                doneTeam = true
                allDone()
            }
        })
    }

    fun allDone(){
        if (donePost && doneCategory && doneContent && doneTeam){
            random()
            Log.e("Firebase", "Article: All done!")
        }
    }

    fun random(){
        for (i in 0 until mapPosts.size){
            var textCategory: String? = null
            var textAuthor: String? = null
            val current = mapPosts.get(mapPosts.keys.elementAt(i))!!
            jsonParser.jsonToCollection(mapPostCont[mapPosts.keys.elementAt(i)])
            val v = mapCategories[current.get("category")]
            if (v != null){
                textCategory = v.get("name")
            }
            if (mapTeam.isNotEmpty()){
                val f = mapTeam[current.get("author")]
                if (f != null){
                    textAuthor = f.get("name")
                }
            }
            val headline = Headline(mapPosts.keys.elementAt(i),
                    textAuthor,
                    current.get("title"),
                    current.get("subtitle"),
                    textCategory,
                    current.get("featured") as Boolean,
                    current.get("paid") as Boolean,
                    current.get("image"),
                    jsonParser.articles)
            listOfArticles.add(headline)
        }
        data.onNext(listOfArticles)
        Log.e("Firebase", "Article: All sent!")
    }
}