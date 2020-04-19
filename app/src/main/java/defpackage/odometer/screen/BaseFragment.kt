@file:Suppress("DEPRECATION")

package defpackage.odometer.screen

import android.app.Fragment
import android.os.Bundle
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()

    protected val args: Bundle
        get() = arguments ?: Bundle()
}