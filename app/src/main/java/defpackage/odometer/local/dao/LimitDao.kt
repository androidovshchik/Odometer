package defpackage.odometer.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import defpackage.odometer.local.entities.LimitEntity

@Dao
abstract class LimitDao {

    @Query(
        """
        SELECT * FROM limits
        ORDER BY l_id ASC
    """
    )
    abstract fun getAll(): List<LimitEntity>

    @Insert
    abstract fun insert(item: LimitEntity): Long

    @Update
    abstract fun update(item: LimitEntity): Int
}