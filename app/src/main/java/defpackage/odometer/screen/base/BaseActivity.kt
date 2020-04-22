package defpackage.odometer.screen.base

import android.app.Activity
import android.content.Context
import android.view.MenuItem
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import timber.log.Timber

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseActivity : Activity(), KodeinAware, CoroutineScope {

    override val kodein by closestKodein()

    protected val job = SupervisorJob()

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(context))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return false
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        job.cancelChildren()
        super.onDestroy()
    }

    override val coroutineContext = Dispatchers.Main + job + CoroutineExceptionHandler { _, e ->
        Timber.e(e)
    }
}