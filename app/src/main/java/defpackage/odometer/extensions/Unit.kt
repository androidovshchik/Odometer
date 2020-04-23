@file:Suppress("unused")

package defpackage.odometer.extensions

import android.os.SystemClock
import java.util.concurrent.TimeUnit

/**
 * Elapsed time since boot + 1 hour
 * Its needed to prevent negative values for measurement
 */
fun pseudoElapsedTime() = SystemClock.elapsedRealtime() + TimeUnit.HOURS.toMillis(1)