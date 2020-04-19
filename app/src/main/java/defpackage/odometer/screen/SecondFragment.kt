package defpackage.odometer.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import defpackage.odometer.R

class SecondFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_second, root, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    companion object {

        @Suppress("DEPRECATION")
        fun newInstance(): SecondFragment {
            return SecondFragment().apply {
                arguments = Bundle().apply {
                }
            }
        }
    }
}