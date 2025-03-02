package com.mfatihceliik.chatapplication.ui.createChat

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class MembersViewPagerAdapter(
    private val fragmentManager: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager, lifecycle) {

    companion object {
        const val TAG = "MemberViewPagerAdapter"
    }

    private var fragmentList: ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int = fragmentList.size
    override fun createFragment(position: Int): Fragment = this.fragmentList[position]

    fun addAllFragment(fragments: ArrayList<Fragment>) {
        this.fragmentList = fragments
    }
}
