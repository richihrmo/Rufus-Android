package com.developers.team100k.rufus.processing

import android.os.Build.VERSION_CODES.P
import android.util.Log
import com.google.firebase.database.*
import io.reactivex.subjects.PublishSubject

/**
 * Created by Richard Hrmo.
 */
class CategoriesParser(val mDatabase: DatabaseReference) {

    lateinit var mapCategories: Map<String, Map<String, String>>
    lateinit var listKeys: MutableList<String>
    var orderedKeys: MutableList<String> = mutableListOf()
    var doneCategoryKeys = false
    var doneCategory = false
    var data = PublishSubject.create<Any>()

    fun call(){
        databaseCall()
        secondCall()
    }

    fun databaseCall(){
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

    fun secondCall(){
        mDatabase.child("categoryKeys").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listKeys = dataSnapshot.getValue(false) as MutableList<String>
                doneCategoryKeys = true
                allDone()
                Log.e("Firebase", listKeys.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Fail to read.")
            }
        })
    }

    fun allDone(){
        if (doneCategory && doneCategoryKeys){
            random()
            doneCategoryKeys = false
            doneCategory = false
            Log.e("Firebase", "Category: All done!")
        }
    }

    fun random(){
        if (mapCategories.isNotEmpty() && listKeys.isNotEmpty()){
            orderedKeys.add("All")
            for (i in 0 until mapCategories.size){
                orderedKeys.add((mapCategories[listKeys[i]]!!)["name"] as String)
            }
            data.onNext(orderedKeys)
        }
        Log.e("Firebase", "Category: All sent!")
    }
}