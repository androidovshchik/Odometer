@file:Suppress("DEPRECATION")

package defpackage.odometer.screen.main

import android.app.Fragment
import android.app.FragmentManager
import android.view.View
import android.view.ViewGroup
import androidx.collection.SimpleArrayMap
import androidx.legacy.app.FragmentStatePagerAdapter

class TabsAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {

    val fragments = SimpleArrayMap<Int, Fragment>()

    override fun getItem(position: Int): Fragment? {
        if (!fragments.containsKey(position)) {
            val fragment = when (position) {
                0 -> FirstFragment.newInstance()
                else -> SecondFragment.newInstance()
            }
            fragments.put(position, fragment)
            return fragment
        }
        return fragments.get(position)
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? = null

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        fragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    override fun destroyItem(container: View, position: Int, `object`: Any) {
        fragments.remove(position)
        super.destroyItem(container, position, `object`)
    }
}