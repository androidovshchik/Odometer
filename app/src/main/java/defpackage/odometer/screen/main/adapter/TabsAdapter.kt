@file:Suppress("DEPRECATION")

package defpackage.odometer.screen.main.adapter

import android.app.FragmentManager
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.legacy.app.FragmentPagerAdapter
import defpackage.odometer.screen.main.FirstFragment
import defpackage.odometer.screen.main.LocationFragment
import defpackage.odometer.screen.main.SecondFragment

class TabsAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

    val fragments = SparseArray<LocationFragment>()

    override fun getItem(position: Int): LocationFragment {
        var fragment = fragments.get(position)
        if (fragment == null) {
            fragment = when (position) {
                0 -> FirstFragment.newInstance()
                else -> SecondFragment.newInstance()
            }
            fragments.put(position, fragment)
            return fragment
        }
        return fragment
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