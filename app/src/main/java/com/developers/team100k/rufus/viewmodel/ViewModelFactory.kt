package com.developers.team100k.rufus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.developers.team100k.rufus.ActivityViewModel

/**
 * Created by Richard Hrmo.
 */

class ViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivityViewModel::class.java)) {
            val key = "UserProfileViewModel"
            return if(hashMapViewModel.containsKey(key)){
                getViewModel(key) as T
            } else {
                addViewModel(key, ActivityViewModel())
                getViewModel(key) as T
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class")    }

    companion object {
        val hashMapViewModel = HashMap<String, ViewModel>()
        fun addViewModel(key: String, viewModel: ViewModel){
            hashMapViewModel.put(key, viewModel)
        }
        fun getViewModel(key: String): ViewModel? {
            return hashMapViewModel[key]
        }
    }
}