package defpackage.odometer.screen.main

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import defpackage.odometer.R
import defpackage.odometer.extensions.onTextChanged
import defpackage.odometer.extensions.setTextSelection
import defpackage.odometer.local.Preferences
import defpackage.odometer.local.entity.LimitEntity
import kotlinx.android.synthetic.main.fragment_second.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.generic.instance

@Suppress("DEPRECATION")
class SecondFragment : LocationFragment() {

    private val preferences by instance<Preferences>()

    private lateinit var numberWatcher: TextWatcher

    private lateinit var speedWatcher: TextWatcher

    private lateinit var widthWatcher: TextWatcher

    override fun onCreateView(inflater: LayoutInflater, root: ViewGroup?, bundle: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_second, root, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        et_train_number.setTextSelection(preferences.trainNumber)
        numberWatcher = et_train_number.onTextChanged {
            afterTextChanged {
                preferences.trainNumber = it?.toString()
            }
        }
        et_main_speed.setTextSelection(preferences.mainSpeed)
        speedWatcher = et_main_speed.onTextChanged {
            afterTextChanged {
                preferences.mainSpeed = it?.toString()
            }
        }
        et_width.setTextSelection(preferences.conditionalWidth)
        widthWatcher = et_width.onTextChanged {
            afterTextChanged {
                preferences.conditionalWidth = it?.toString()
            }
        }
        rv_list.adapter = adapter
        rv_list.setHasFixedSize(true)
        rv_list.isNestedScrollingEnabled = false
        im_delete.setOnClickListener {
            it.isEnabled = false
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    db.limitDao().delete(item)
                }
                if (getView() != null) {
                    loadLimits()
                    it.isEnabled = true
                }
            }
        }
        im_add.setOnClickListener {
            it.isEnabled = false
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    db.limitDao().insert(LimitEntity())
                }
                if (getView() != null) {
                    loadLimits()
                    it.isEnabled = true
                }
            }
        }
    }

    override fun onItemClicked(position: Int, item: LimitEntity) {
    }

    @Suppress("DEPRECATION")
    override fun onDestroyView() {
        et_train_number.removeTextChangedListener(numberWatcher)
        et_main_speed.removeTextChangedListener(speedWatcher)
        et_width.removeTextChangedListener(widthWatcher)
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