package defpackage.odometer.local.dao

import androidx.room.Dao
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
abstract class CommonDao {

    @RawQuery
    abstract fun query(supportSQLiteQuery: SupportSQLiteQuery): Int

    open fun dumpData() = query(SimpleSQLiteQuery("pragma wal_checkpoint(full)"))
}