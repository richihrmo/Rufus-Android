package com.developers.team100k.rufus.processing

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import io.reactivex.subjects.PublishSubject

/**
 * Created by Richard Hrmo.
 */

class AuthorParser(val mDatabase: DatabaseReference){

    lateinit var map: Map<String, Any>
    var data = PublishSubject.create<Any>()
    val jsonParser = JsonParser()

    fun callDatabase(){
        mDatabase.child("team").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                map = dataSnapshot.getValue(false) as Map<String, Any>
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
}