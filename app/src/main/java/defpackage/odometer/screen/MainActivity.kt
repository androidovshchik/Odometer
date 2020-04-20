package defpackage.odometer.screen

import android.Manifest
import android.app.Activity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import defpackage.odometer.R
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("DEPRECATION")
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vp_main.adapter = TabsAdapter(fragmentManager)
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            ), 0
        )
    }
}
