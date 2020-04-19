@file:Suppress("unused")

package defpackage.odometer.screen

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.layoutInflater
import ru.gidline.app.screen.base.listener.IRecycler
import java.lang.ref.WeakReference

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseAdapter<T : IRecycler<E>, E> : RecyclerView.Adapter<BaseHolder<E>> {

    val items = mutableListOf<E>()

    protected var reference: WeakReference<T>? = null

    constructor()

    constructor(listener: T) {
        setListener(listener)
    }

    /**
     * It is assumed that this will be called one time or never
     */
    fun setListener(listener: T) {
        reference = WeakReference(listener)
    }

    override fun onBindViewHolder(holder: BaseHolder<E>, position: Int) {
        holder.onBindItem(position, items[position])
    }

    override fun getItemCount() = items.size

    protected fun ViewGroup.inflate(@LayoutRes layout: Int): View {
        return context.layoutInflater.inflate(layout, this, false)
    }
}

abstract class BaseHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener {

    abstract fun onBindItem(position: Int, item: T)

    override fun onClick(v: View) {}

    val appContext: Context
        get() = itemView.context.applicationContext
}