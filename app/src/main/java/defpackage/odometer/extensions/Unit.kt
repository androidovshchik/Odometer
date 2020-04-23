@file:Suppress("unused")

package defpackage.odometer.extensions

import android.os.SystemClock

/**
 * Elapsed time since boot + 1 day
 * Its needed to prevent negative values for measurement
 */
fun pseudoElapsedTime() = SystemClock.elapsedRealtime() + 86_400L