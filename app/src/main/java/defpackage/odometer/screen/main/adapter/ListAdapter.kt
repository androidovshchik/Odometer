package defpackage.odometer.screen.main.adapter

import android.annotation.SuppressLint
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import com.redmadrobot.inputmask.MaskedTextChangedListener
import defpackage.odometer.R
import defpackage.odometer.extension.onTextChanged
import defpackage.odometer.extension.setTextSelection
import defpackage.odometer.local.entity.LimitEntity
import defpackage.odometer.screen.base.BaseAdapter
import defpackage.odometer.screen.base.BaseHolder
import defpackage.odometer.screen.base.IAdapter
import kotlinx.android.synthetic.main.item_list.view.*
import timber.log.Timber
import java.util.concurrent.CopyOnWriteArrayList

class ListAdapter(listener: ListListener) : BaseAdapter<ListListener, LimitEntity>(listener) {

    override val items = CopyOnWriteArrayList<LimitEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_list))
    }

    inner class ViewHolder(itemView: View) : BaseHolder<LimitEntity>(itemView),
        PopupMenu.OnMenuItemClickListener {

        private val speed = itemView.et_speed

        private val position = itemView.et_position

        private val speedWatcher: TextWatcher

        private val positionWatcher: TextWatcher

        init {
            speedWatcher = speed.onTextChanged {
                afterTextChanged {
                    try {
                        items[bindingAdapterPosition].speed = it.toString().toInt()
                    } catch (ignored: Throwable) {
                    }
                }
            }
            MaskedTextChangedListener.installOn(position, "[000] [000]{.}[0]").apply {
                rightToLeft = true
            }
            positionWatcher = position.onTextChanged {
                afterTextChanged {
                    try {
                        items[bindingAdapterPosition].position = it.toString().toFloat()
                    } catch (ignored: Throwable) {
                    }
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
            this.position.setText(item.position.toString())
        }

        override fun onMenuItemClick(menuItem: MenuItem): Boolean {
            when (menuItem.itemId) {
                R.id.action_delete -> try {
                    val position = bindingAdapterPosition
                    reference?.get()?.onItemRemoved(position, items[position])
                    items.removeAt(position)
                    notifyDataSetChanged()
                } catch (e: Throwable) {
                    Timber.e(e)
                }
            }
            return true
        }

        @Suppress("unused")
        fun release() {
            speed.removeTextChangedListener(speedWatcher)
            position.removeTextChangedListener(positionWatcher)
        }
    }
}

interface ListListener : IAdapter<LimitEntity> {

    fun onItemRemoved(position: Int, item: LimitEntity)
}