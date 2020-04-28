@file:Suppress("unused")

package defpackage.odometer.extension

fun LongArray.shiftLeft() {
    var i = -1
    forEach {
        if (it >= 0) {
            set(++i, it)
        }
    }
    (i + 1 until size).forEach {
        set(it, -1L)
    }
}

fun LongArray.add(value: Long) {
    val i = indexOfFirst { it < 0 }
    if (i >= 0) {
        set(i, value)
    }
}