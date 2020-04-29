package defpackage.odometer.local.dao

import androidx.room.*
import defpackage.odometer.local.entity.LimitEntity

@Dao
abstract class LimitDao {

    @Query(
        """
        SELECT * FROM limits
        ORDER BY l_position ASC
    """
    )
    abstract fun getAll(): List<LimitEntity>

    @Insert
    abstract fun insert(item: LimitEntity): Long

    @Update
    abstract fun update(items: List<LimitEntity>)

    @Delete
    abstract fun delete(item: LimitEntity): Int
}