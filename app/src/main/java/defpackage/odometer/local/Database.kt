package defpackage.odometer.local

import androidx.room.RoomDatabase
import defpackage.odometer.local.dao.CommonDao
import defpackage.odometer.local.dao.LimitDao
import defpackage.odometer.local.entities.LimitEntity

@androidx.room.Database(
    entities = [
        LimitEntity::class
    ],
    version = 1
)
abstract class Database : RoomDatabase() {

    abstract fun commonDao(): CommonDao

    abstract fun limitDao(): LimitDao
}