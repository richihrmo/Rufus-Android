package com.developers.team100k.rufus.processing

import android.util.Log
import com.developers.team100k.rufus.entity.Page
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import io.reactivex.subjects.PublishSubject

/**
 * Created by Richard Hrmo.
 */

class PagesParser(val mDatabase: DatabaseReference){

    lateinit var map: Map<String, Any>
    lateinit var map2: Map<String, String>
    lateinit var list: HashMap<String, String>
    var data = PublishSubject.create<Any>()

    fun callDatabase(){
        mDatabase.child("pages").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                map = dataSnapshot.getValue(false) as Map<String, Any>
                secondCall()
                Log.e("Firebase", map.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Fail to read.")
            }
        })
    }

    fun secondCall(){
        mDatabase.child("pageContentsHTML").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                map2 = dataSnapshot.getValue(false) as Map<String, String>
                val random = map.get(map.keys.first()) as Map<String, String>
                Log.e("random", random.toString())
                val article = map2.get(map2.keys.first()) as String
                val page = Page(random.get("title"), random.get("subtitle"), article)
                data.onNext(page)
                Log.e("Firebase", map2.get(map.keys.first()))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Fail to read.")
            }
        })
    }
}