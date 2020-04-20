@file:Suppress("DEPRECATION")

package defpackage.odometer.screen

import android.app.FragmentManager
import androidx.legacy.app.FragmentPagerAdapter

class TabsAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

    override fun getItem(position: Int) = when (position) {
        0 -> FirstFragment.newInstance()
        else -> SecondFragment.newInstance()
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? = null
}