package defpackage.odometer.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "limits"
)
class LimitEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "l_id")
    var id: Long? = null

    @ColumnInfo(name = "l_speed")
    var speed = 0

    @ColumnInfo(name = "l_position")
    var position = 0f
}