package defpackage.odometer.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "reservations"
)
class ReservationEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "r_id")
    var id: Long? = null

}