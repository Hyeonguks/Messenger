package woogie.space.messenger.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "friends")
data class Friends(
    val viewType : Int,
    val name : String,
    @PrimaryKey val email : String,
    val photo : String,
    val uid : String,
) : Parcelable