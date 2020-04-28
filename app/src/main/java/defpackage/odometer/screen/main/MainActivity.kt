package defpackage.odometer.screen.main

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.WindowManager
import androidx.core.util.forEach
import com.google.android.gms.location.LocationSettingsStates
import defpackage.odometer.LocationListener
import defpackage.odometer.LocationManager
import defpackage.odometer.R
import defpackage.odometer.REQUEST_LOCATION
import defpackage.odometer.local.Database
import defpackage.odometer.local.entity.LimitEntity
import defpackage.odometer.screen.base.BaseActivity
import defpackage.odometer.screen.main.adapter.ListAdapter
import defpackage.odometer.screen.main.adapter.ListListener
import defpackage.odometer.screen.main.adapter.TabsAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.generic.instance
import timber.log.Timber

@Suppress("DEPRECATION")
class MainActivity : BaseActivity(), LocationListener, ListListener {

    private val locationManager by instance<LocationManager>()

    private val db by instance<Database>()

    private lateinit var tabsAdapter: TabsAdapter

    private val listAdapter = ListAdapter(this)

    private var signalPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        tabsAdapter = TabsAdapter(fragmentManager)
        setContentView(R.layout.activity_main)
        vp_main.adapter = tabsAdapter
        rv_list.apply {
            adapter = listAdapter
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
        }
        locationManager.setLocationListener(this)
        launch {
            val items = withContext(Dispatchers.IO) {
                db.limitDao().getAll()
            }
            listAdapter.items.clear()
            listAdapter.items.addAll(items)
            listAdapter.notifyDataSetChanged()
            while (true) {
                playSignal()
                delay(3000)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        locationManager.requestUpdates(this)
    }

    override fun onLocationStates(states: LocationSettingsStates?) {
        onLocationAvailability(states?.isLocationUsable == true)
    }

    override fun onLocationAvailability(available: Boolean) {
        tabsAdapter.fragments.forEach { _, fragment ->
            fragment.onLocationAvailability(available)
        }
    }

    override fun onSpeedChanged(speed: Int) {
        tabsAdapter.fragments.forEach { _, fragment ->
            fragment.onSpeedChanged(speed)
        }
    }

    override fun onItemClicked(position: Int, item: LimitEntity) {
    }

    override fun onItemRemoved(position: Int, item: LimitEntity) {

    }

    fun playSignal() {
        releasePlayer()
        try {
            signalPlayer = MediaPlayer.create(applicationContext, R.raw.hey).also {
                it.start()
            }
        } catch (e: Throwable) {
            Timber.e(e)
        }
    }

    override fun onStop() {
        locationManager.removeUpdates()
        releasePlayer()
        super.onStop()
    }

    private fun releasePlayer() {
        try {
            signalPlayer?.apply {
                stop()
                reset()
                release()
            }
        } catch (e: Throwable) {
            Timber.e(e)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION -> {
                locationManager.requestUpdates(this)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_LOCATION -> {
                if (resultCode == RESULT_OK) {
                    onLocationStates(LocationSettingsStates.fromIntent(data))
                } else {
                    locationManager.requestUpdates(this)
                }
            }
        }
    }

    override fun onDestroy() {
        locationManager.release()
        super.onDestroy()
    }
}
