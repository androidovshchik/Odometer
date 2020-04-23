@file:Suppress("DEPRECATION")

package defpackage.odometer.screen.main

import com.google.android.gms.location.LocationSettingsStates
import defpackage.odometer.LocationListener
import defpackage.odometer.local.Database
import defpackage.odometer.local.entity.LimitEntity
import defpackage.odometer.screen.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.generic.instance

abstract class LocationFragment : BaseFragment(), LocationListener, ListListener {

    protected val db by instance<Database>()

    @Suppress("LeakingThis")
    protected val adapter = ListAdapter(this)

    override fun onLocationStates(states: LocationSettingsStates?) {
    }

    override fun onLocationAvailability(available: Boolean) {
    }

    override fun onSpeedChanged(speed: Int) {
    }

    override fun onItemClicked(position: Int, item: LimitEntity) {
    }

    fun loadLimits() {
        launch {
            val items = withContext(Dispatchers.IO) {
                db.limitDao().getAll()
            }
            adapter.items.clear()
            adapter.items.addAll(items)
            adapter.notifyDataSetChanged()
        }
    }
}