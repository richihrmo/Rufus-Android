package com.developers.team100k.rufus.processing

import android.util.Log
import com.developers.team100k.rufus.entity.Article
import com.developers.team100k.rufus.entity.Headline
import com.developers.team100k.rufus.entity.Page
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import io.reactivex.subjects.PublishSubject

/**
 * Created by Richard Hrmo.
 */

class ArticlesParser(val mDatabase: DatabaseReference){

    lateinit var map: Map<String, Any>
    lateinit var map2: Map<String, String>
    lateinit var map3: Map<String, Any>
    lateinit var list: List<Any>
    lateinit var listOfArticles: MutableList<Headline>
    var data = PublishSubject.create<Any>()
    val jsonParser = JsonParser()

    fun callDatabase(){
        mDatabase.child("posts").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                map = dataSnapshot.getValue(false) as Map<String, Any>
                secondCall()
//                data.onNext(map)
//                list = map.values.toMutableList()
//                for (i in 0 until map.size){
//                    data.onNext(list[i])
//                }
                Log.e("Firebase", map.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Fail to read.")
            }
        })
    }

    fun secondCall(){
        mDatabase.child("postContents").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                map2 = dataSnapshot.getValue(false) as Map<String, String>
                listOfArticles = ArrayList()
                categoryCall()
//                for (i in 0 until map2.size){
//                    val current = map.get(map.keys.elementAt(i)) as Map<String, String>
//                    jsonParser.jsonToCollection(map2[map.keys.elementAt(i)])
//                    val headline = Headline(current.get("author"),
//                            current.get("title"),
//                            current.get("subtitle"),
//                            current.get("category"),
//                            current.get("featured") as Boolean,
//                            jsonParser.articles)
//                    listOfArticles.add(headline)
//                }
//                data.onNext(listOfArticles)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Fail to read.")
            }
        })
    }

    fun categoryCall(){
        mDatabase.child("categories").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                map3 = dataSnapshot.getValue(false) as Map<String, Map<String, String>>

                for (i in 0 until map2.size){
                    var textCategory: String? = null
                    val current = map.get(map.keys.elementAt(i)) as Map<String, String>
                    jsonParser.jsonToCollection(map2[map.keys.elementAt(i)])
                    val v = map3[current.get("category")]
                    if (v != null){
                        textCategory = (v as Map<String, String>).get("name")
                    }
                    val headline = Headline(current.get("author"),
                            current.get("title"),
                            current.get("subtitle"),
                            textCategory,
                            current.get("featured") as Boolean,
                            jsonParser.articles)
                    listOfArticles.add(headline)
                }
                data.onNext(listOfArticles)

                Log.e("Firebase", map.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Fail to read.")
            }
        })
    }
}