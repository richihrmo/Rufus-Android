package com.developers.team100k.rufus.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.developers.team100k.rufus.TabLayoutFragment

/**
 * Created by Richard Hrmo.
 */
class TabLayoutPagerAdapter(fm: FragmentManager, val data: List<String>) : FragmentStatePagerAdapter(fm){

    override fun getItem(position: Int): Fragment {
        val fragment = TabLayoutFragment()
        val bundle = Bundle()
        bundle.putString("category", data[position])
        fragment.arguments = bundle
        return fragment
    }

    override fun getCount(): Int = data.size

    override fun getPageTitle(position: Int): CharSequence? = data[position]

}