@file:Suppress("unused")

package defpackage.odometer.extensions

inline fun LongArray.removeAll(predicate: (Long) -> Boolean) {
    (0 until size).forEach {
        if (predicate(get(it))) {
            set(it, -1L)
        }
    }
}

fun LongArray.shiftLeft() {
    var i = -1
    forEachIndexed { i, item ->
        if (get(it) < 0) {
            set(it, value)
        }
        if (get(it) > 0) {
            set(++i, get(it))
        }
        if (it < size - 1) {
            set(it, get(it + 1))
        } else {
            set(it, -1L)
        }
    }
    (0 until size).forEach {
    }
}

fun LongArray.add(value: Long) {
    val i = indexOfFirst { it < 0 }
    if (i >= 0) {
        set(i, value)
    }
}