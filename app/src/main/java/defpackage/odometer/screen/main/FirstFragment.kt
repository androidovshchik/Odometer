package defpackage.odometer.screen.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import defpackage.odometer.R
import defpackage.odometer.screen.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_first.*

class FirstFragment : BaseFragment(), ListListener {

    private val adapter = ListAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_first, root, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter.items.add(Any())
        adapter.items.add(Any())
        adapter.items.add(Any())
        adapter.items.add(Any())
        adapter.items.add(Any())
        adapter.items.add(Any())
        adapter.items.add(Any())
        adapter.items.add(Any())
        adapter.items.add(Any())
        adapter.items.add(Any())
        adapter.items.add(Any())
        adapter.items.add(Any())
        rv_list.adapter = adapter
        rv_list.setHasFixedSize(true)
    }

    override fun onItemClicked(position: Int, item: Any) {

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