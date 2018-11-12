package com.developers.team100k.rufus.processing

import android.util.Log
import com.google.firebase.database.*
import kotlin.collections.ArrayList
import io.reactivex.subjects.PublishSubject

/**
 * Created by Richard Hrmo.
 */
class CategoriesParser(val mDatabase: DatabaseReference) {

    lateinit var map: Map<String, Any>
    lateinit var list: List<String>
    var data = PublishSubject.create<Any>()

    fun databaseCall(){
        mDatabase.child("categories").addListenerForSingleValueEvent(object : ValueEventListener {
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
        mDatabase.child("categoryKeys").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list = dataSnapshot.getValue(false) as List<String>
                Log.e("Firebase", list.toString())
                if (map.isNotEmpty() && list.isNotEmpty()){
                    for (i in 0 until map.size){
                        data.onNext(map[list[i]]!!)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Fail to read.")
            }
        })
    }
}