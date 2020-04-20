package defpackage.odometer.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import defpackage.odometer.R
import kotlinx.android.synthetic.main.fragment_second.*

class SecondFragment : BaseFragment(), ListListener {

    private val adapter = ListAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_second, root, false)
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
        fun newInstance(): SecondFragment {
            return SecondFragment().apply {
                arguments = Bundle().apply {
                }
            }
        }
    }
}