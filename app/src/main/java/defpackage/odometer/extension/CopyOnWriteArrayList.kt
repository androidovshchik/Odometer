package defpackage.odometer.extension

import java.util.concurrent.CopyOnWriteArrayList

inline fun <T, R : Comparable<R>> CopyOnWriteArrayList<T>.sortListBy(crossinline selector: (T) -> R?) {
    if (size > 1) {
        if (isNougatPlus()) {
            sortBy(selector)
        } else {
            val list = ArrayList(this)
            list.sortBy(selector)
            clear()
            addAll(list)
        }
    }
}