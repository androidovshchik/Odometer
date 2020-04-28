package defpackage.odometer.screen.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import defpackage.odometer.R
import kotlinx.android.synthetic.main.fragment_first.*

class FirstFragment : LocationFragment() {

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_first, root, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    @Suppress("DEPRECATION")
    override fun onLocationAvailability(available: Boolean) {
        tv_gps.background = ContextCompat.getDrawable(activity ?: return, if (available) {
            R.drawable.defbackground_accent
        } else {
            R.drawable.defbackground_primary
        })
    }

    override fun onSpeedChanged(speed: Int) {
        with(speed.toString().padStart(3, '0')) {
            tv_1.text = get(length - 3).toString()
            tv_2.text = get(length - 2).toString()
            tv_3.text = get(length - 1).toString()
        }
    }

    companion object {

        @Suppress("DEPRECATION")
        fun newInstance(): FirstFragment {
            return FirstFragment().apply {
                arguments = Bundle().apply {
                }
            }
        }
    }
}