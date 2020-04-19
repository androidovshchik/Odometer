package defpackage.odometer.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import defpackage.odometer.R

class FirstFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_first, root, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

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