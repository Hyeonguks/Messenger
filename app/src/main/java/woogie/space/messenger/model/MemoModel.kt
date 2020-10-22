package woogie.space.messenger.model

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
@Entity(tableName = "memo")
data class MemoModel(
        @PrimaryKey(autoGenerate = true)
        var Num : Int = 0,
        var ViewType: Int = 1,
        var UserID : String? = null,
        var Title : String = "",
        var Memo : String = "",
        var Date : String = Date().time.toString(),
        var Edited : Boolean = false,
        @Ignore var Image : ArrayList<Bitmap>? = null
) : Parcelable