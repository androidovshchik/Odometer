package defpackage.odometer.screen

import android.app.Activity
import android.os.Bundle
import defpackage.odometer.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vp_main
    }
}
