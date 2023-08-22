package com.example.test_android2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment){
    private val fragments = mutableListOf<Fragment>()

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
        notifyItemInserted(fragments.size -1)
    }
    fun removeFragment(){
        fragments.removeLast()
        notifyItemRemoved(fragments.size)
    }
}