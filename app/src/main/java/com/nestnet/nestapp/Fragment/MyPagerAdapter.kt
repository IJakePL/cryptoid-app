package com.nestnet.nestapp.Fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DeFiWalletFragment()
            1 -> NanoCoinFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}