package defpackage.odometer.local.dao

import androidx.room.*
import defpackage.odometer.local.entities.ReservationEntity

@Dao
abstract class ReservationDao {

    @Query(
        """
        SELECT * FROM reservations
        ORDER BY r_id ASC
    """
    )
    abstract fun getAll(): List<ReservationEntity>

    @Insert
    abstract fun insert(item: ReservationEntity): Long

    @Update
    abstract fun update(item: ReservationEntity): Int

    @Delete
    abstract fun delete(item: ReservationEntity): Int
}