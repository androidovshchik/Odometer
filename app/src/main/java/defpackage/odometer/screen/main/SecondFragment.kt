package defpackage.odometer.screen.main

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import defpackage.odometer.R
import defpackage.odometer.extensions.onTextChanged
import defpackage.odometer.local.Database
import defpackage.odometer.local.entity.LimitEntity
import defpackage.odometer.screen.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_second.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.generic.instance

class SecondFragment : BaseFragment(), ListListener {

    private val db by instance<Database>()

    private val adapter = ListAdapter(this)

    private lateinit var numberWatcher: TextWatcher

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_second, root, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        numberWatcher = et_train_number.onTextChanged {
            afterTextChanged {

            }
        }
        rv_list.adapter = adapter
        rv_list.setHasFixedSize(true)
        rv_list.isNestedScrollingEnabled = false
        im_add.setOnClickListener {
            it.isEnabled = false
            GlobalScope.launch(Dispatchers.Main) {
                val item = LimitEntity()
                item.id = withContext(Dispatchers.IO) {
                    db.limitDao().insert(item)
                }
                adapter.items.add(item)
                adapter.notifyDataSetChanged()
                it.isEnabled = true
            }
        }
        launch {
            val items = withContext(Dispatchers.IO) {
                db.limitDao().getAll()
            }
            adapter.items.clear()
            adapter.items.addAll(items)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onItemClicked(position: Int, item: LimitEntity) {
    }

    @Suppress("DEPRECATION")
    override fun onDestroyView() {
        et_train_number.removeTextChangedListener(numberWatcher)
        super.onDestroyView()
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