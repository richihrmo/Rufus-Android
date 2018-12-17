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

class HTMLParser(val mDatabase: DatabaseReference, val id: String){

    lateinit var map: Map<String, Any>
    lateinit var list: HashMap<String, String>
    var data = PublishSubject.create<Any>()

    fun callDatabase(){
        mDatabase.child("postContentsHTML").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                map = dataSnapshot.getValue(false) as Map<String, String>
                data.onNext(map[id]!!)
//                Log.e("POST_HTML", map[id].toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Fail to read.")
            }
        })
    }
}