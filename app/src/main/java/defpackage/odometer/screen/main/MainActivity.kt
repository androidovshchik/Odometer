package defpackage.odometer.screen.main

import android.Manifest
import android.os.Bundle
import androidx.core.app.ActivityCompat
import defpackage.odometer.LocationListener
import defpackage.odometer.LocationManager
import defpackage.odometer.R
import defpackage.odometer.screen.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.generic.instance

@Suppress("DEPRECATION")
class MainActivity : BaseActivity(), LocationListener {

    private val locationManager by instance<LocationManager>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vp_main.adapter = TabsAdapter(fragmentManager)
        locationManager.setLocationListener(this)
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            ), 0
        )
    }

    override fun onStart() {
        super.onStart()
        locationManager.requestUpdates()
    }

    override fun onLocationAvailability(available: Boolean) {

    }

    override fun onSpeedChanged(speed: Int) {

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
    }
}
