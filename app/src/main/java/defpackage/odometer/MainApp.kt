package defpackage.odometer

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import defpackage.odometer.local.Database
import defpackage.odometer.local.Preferences
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import timber.log.Timber

class MainApp : Application(), KodeinAware {

    override val kodein by Kodein.lazy {

        bind<Context>() with provider {
            applicationContext
        }

        bind<Preferences>() with provider {
            Preferences(instance())
        }

        bind<Database>() with singleton {
            Room.databaseBuilder(instance(), Database::class.java, "app.db")
                .setJournalMode(RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Class.forName("com.facebook.stetho.Stetho")
                .getDeclaredMethod("initializeWithDefaults", Context::class.java)
                .invoke(null, applicationContext)
        }
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )
    }
}