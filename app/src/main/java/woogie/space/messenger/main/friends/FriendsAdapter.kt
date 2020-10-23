package woogie.space.messenger.main.friends

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.friends_default_item.view.*
import woogie.space.messenger.R
import woogie.space.messenger.model.Friends

class FriendsAdapter (var context: Context, var friendsList : ArrayList<Friends>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface ItemClick { fun onClick(view: View, position: Int, chatModel: Friends, requestCode : Int) }
    var itemClick: ItemClick? = null

    companion object {
        const val TYPE_DEFAULT = 1

        const val TYPE_DELETED = 888
        const val TYPE_LOADING = 777

        const val FROM_ROOM_LIST = 222
    }

    inner class TYPE_DEFAULT(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(friends: Friends, context: Context) {
//            itemView.UserImage.text = friends.userName
            itemView.UserName.text = friends.userName
        }
    }

    inner class TYPE_LOADING(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(friends: Friends, context: Context) {
//            itemView.UserName.text = friends.userName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_DEFAULT -> {
                TYPE_DEFAULT(
                    LayoutInflater.from(context)
                        .inflate(R.layout.friends_default_item, parent, false)
                )
            }

            TYPE_LOADING -> {
                TYPE_LOADING(
                    LayoutInflater.from(context)
                        .inflate(R.layout.loading_item, parent, false)
                )
            }
            else -> throw IllegalArgumentException("Invalid view type")

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TYPE_DEFAULT -> {
                holder.itemView.run {
                    holder.bind(friendsList[position],context)
                }
            }

            is TYPE_LOADING -> {
                holder.itemView.run {
                    holder.bind(friendsList[position],context)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return friendsList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (friendsList[position].viewType) {
            TYPE_DEFAULT -> TYPE_DEFAULT
            TYPE_LOADING -> TYPE_LOADING
            else -> TYPE_LOADING
        }
    }
}