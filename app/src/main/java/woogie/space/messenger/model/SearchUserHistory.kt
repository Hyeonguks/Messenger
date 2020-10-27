package woogie.space.messenger.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.sql.Date

//https://stackoverflow.com/questions/48962106/add-unique-constraint-in-room-database-to-multiple-column

@Parcelize
@Entity(tableName = "searchUserHistory", indices = [Index(value = ["SearchText"], unique = true)])
data class SearchUserHistory(
    @PrimaryKey (autoGenerate = true) val index: Int = 0,
    val SearchText : String,
) : Parcelable