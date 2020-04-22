package defpackage.odometer.screen.main

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import defpackage.odometer.R
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

    inner class ViewHolder(itemView: View) : BaseHolder<LimitEntity>(itemView) {

        private val speed = itemView.et_speed

        private val distance = itemView.et_distance

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                reference?.get()?.onItemClicked(position, items[position])
            }
        }

        @SuppressLint("SetTextI18n")
        override fun onBindItem(position: Int, item: LimitEntity) {
            speed.setTextSelection(item.speed.toString())
            distance.setTextSelection(item.distance.toString())
        }
    }
}

interface ListListener : IAdapter<LimitEntity>