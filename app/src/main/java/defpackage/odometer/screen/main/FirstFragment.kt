package defpackage.odometer.screen.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import defpackage.odometer.R
import defpackage.odometer.local.entity.LimitEntity
import kotlinx.android.synthetic.main.fragment_first.*

class FirstFragment : LocationFragment() {

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_first, root, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter.items.add(LimitEntity())
        adapter.items.add(LimitEntity())
        adapter.items.add(LimitEntity())
        adapter.items.add(LimitEntity())
        adapter.items.add(LimitEntity())
        adapter.items.add(LimitEntity())
        adapter.items.add(LimitEntity())
        adapter.items.add(LimitEntity())
        adapter.items.add(LimitEntity())
        adapter.items.add(LimitEntity())
        adapter.items.add(LimitEntity())
        adapter.items.add(LimitEntity())
        rv_list.adapter = adapter
        rv_list.setHasFixedSize(true)
    }

    override fun onSpeedChanged(speed: Int) {
        with(speed.toString().padStart(3, '0')) {
            tv_1.text = get(0).toString()
            tv_2.text = get(1).toString()
            tv_3.text = get(2).toString()
        }
    }

    override fun onItemClicked(position: Int, item: LimitEntity) {
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