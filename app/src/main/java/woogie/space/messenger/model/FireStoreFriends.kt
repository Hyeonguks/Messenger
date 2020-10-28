package woogie.space.messenger.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FireStoreFriends(
    val blockList: ArrayList<String> = ArrayList(),
    val friendsList: ArrayList<String> = ArrayList(),
    val received: ArrayList<String> = ArrayList(),
) : Parcelable {

}