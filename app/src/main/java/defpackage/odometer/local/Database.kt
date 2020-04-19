package defpackage.odometer.local

import androidx.room.RoomDatabase
import defpackage.odometer.local.dao.CommonDao
import defpackage.odometer.local.dao.ReservationDao
import defpackage.odometer.local.entities.ReservationEntity

@androidx.room.Database(
    entities = [
        ReservationEntity::class
    ],
    version = 1
)
abstract class Database : RoomDatabase() {

    abstract fun commonDao(): CommonDao

    abstract fun reservationDao(): ReservationDao
}