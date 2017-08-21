package ru.vassuv.fl.odordivice.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import ru.vassuv.fl.odordivice.ui.fragment.control.AboutFragment
import ru.vassuv.fl.odordivice.ui.fragment.control.SetFragment

class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    val mNumOfTabs: Int = 2


    override fun getItem(position: Int): Fragment? = when (position) {
        0 -> SetFragment()
        1 -> AboutFragment()
        else -> null
    }


    override fun getCount(): Int {
        return mNumOfTabs
    }
}