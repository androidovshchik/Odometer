package defpackage.odometer.screen.main

import android.annotation.SuppressLint
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import defpackage.odometer.R
import defpackage.odometer.extensions.onTextChanged
import defpackage.odometer.extensions.setTextSelection
import defpackage.odometer.local.entity.LimitEntity
import defpackage.odometer.screen.base.BaseAdapter
import defpackage.odometer.screen.base.BaseHolder
import defpackage.odometer.screen.base.IAdapter
import kotlinx.android.synthetic.main.item_list.view.*

class ListAdapter(listener: ListListener) : BaseAdapter<ListListener, LimitEntity>(listener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_list))
    }

    override fun onViewRecycled(holder: BaseHolder<LimitEntity>) {
        (holder as ViewHolder).release()
    }

    inner class ViewHolder(itemView: View) : BaseHolder<LimitEntity>(itemView) {

        private val speed = itemView.et_speed

        private val distance = itemView.et_distance

        private val speedWatcher: TextWatcher

        private val distanceWatcher: TextWatcher

        init {
            speedWatcher = speed.onTextChanged {
                afterTextChanged {
                    val position = bindingAdapterPosition
                }
            }
            distanceWatcher = distance.onTextChanged {
                afterTextChanged {

                }
            }
        }

        @SuppressLint("SetTextI18n")
        override fun onBindItem(position: Int, item: LimitEntity) {
            speed.setTextSelection(item.speed.toString())
            distance.setTextSelection(item.distance.toString())
        }

        fun release() {
            speed.removeTextChangedListener(speedWatcher)
            distance.removeTextChangedListener(distanceWatcher)
        }
    }
}

interface ListListener : IAdapter<LimitEntity>