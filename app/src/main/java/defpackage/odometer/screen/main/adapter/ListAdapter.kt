package defpackage.odometer.screen.main.adapter

import android.annotation.SuppressLint
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
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

    inner class ViewHolder(itemView: View) : BaseHolder<LimitEntity>(itemView),
        PopupMenu.OnMenuItemClickListener {

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
            val menu = itemView.im_menu
            val popup = PopupMenu(appContext, menu).also {
                it.menuInflater.inflate(R.menu.popup_menu, it.menu)
                it.setOnMenuItemClickListener(this)
            }
            menu.setOnClickListener {
                popup.show()
            }
        }

        @SuppressLint("SetTextI18n")
        override fun onBindItem(position: Int, item: LimitEntity) {
            speed.setTextSelection(item.speed.toString())
            distance.setTextSelection(item.distance.toString())
        }

        override fun onMenuItemClick(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.action_delete -> {
                    val position = bindingAdapterPosition
                    reference?.get()?.onItemRemoved(position, items[position])
                }
            }
            return true
        }

        fun release() {
            speed.removeTextChangedListener(speedWatcher)
            distance.removeTextChangedListener(distanceWatcher)
        }
    }
}

interface ListListener : IAdapter<LimitEntity> {

    fun onItemRemoved(position: Int, item: LimitEntity)
}