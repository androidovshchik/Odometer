package defpackage.odometer.screen.main

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.core.util.forEach
import com.google.android.gms.location.LocationSettingsStates
import defpackage.odometer.LocationListener
import defpackage.odometer.LocationManager
import defpackage.odometer.R
import defpackage.odometer.REQUEST_LOCATION
import defpackage.odometer.screen.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.generic.instance

@Suppress("DEPRECATION")
class MainActivity : BaseActivity(), LocationListener {

    private val locationManager by instance<LocationManager>()

    private lateinit var adapter: TabsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        adapter = TabsAdapter(fragmentManager)
        setContentView(R.layout.activity_main)
        vp_main.adapter = adapter
        locationManager.setLocationListener(this)
    }

    override fun onStart() {
        super.onStart()
        locationManager.requestUpdates(this)
    }

    override fun onLocationStates(states: LocationSettingsStates?) {
        onLocationAvailability(states?.isLocationUsable == true)
    }

    override fun onLocationAvailability(available: Boolean) {
        adapter.fragments.forEach { _, fragment ->
            fragment.onLocationAvailability(available)
        }
    }

    override fun onSpeedChanged(speed: Int) {
        adapter.fragments.forEach { _, fragment ->
            fragment.onSpeedChanged(speed)
        }
    }

    override fun onStop() {
        locationManager.removeUpdates()
        super.onStop()
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
