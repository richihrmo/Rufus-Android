package com.developers.team100k.rufus.adapter

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.developers.team100k.rufus.entity.Headline

/**
 * Created by Richard Hrmo.
 */
class TabLayoutPagerAdapter(val fm: FragmentManager, val data: List<String>) : FragmentStatePagerAdapter(fm){

    override fun getItem(position: Int): Fragment {
//        val fragment = TabLayoutFragment()
//        val bundle: Bundle = Bundle()
//        bundle.putParcelableArrayList(articles)
//        fragment.arguments = bundle
        return TabLayoutFragment()
    }

    override fun getCount(): Int = data.size

    override fun getPageTitle(position: Int): CharSequence? = data[position]
}