package woogie.space.messenger.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "searchUserHistory")
data class SearchUserHistory(
    @PrimaryKey val SearchText : String,
) : Parcelable