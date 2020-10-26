package woogie.space.messenger.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.search_user_default_item.view.*
import kotlinx.android.synthetic.main.search_user_default_item.view.TextViewSearched
import kotlinx.android.synthetic.main.search_user_first_item.view.*
import woogie.space.messenger.R
import woogie.space.messenger.model.SearchUserHistory

class SearchUserHistoryAdapter (var context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val DEFAULT_ITEM_DELETE = 201
    val FIRST_ITEM_DELETE = 101
    val FIRST_ITEM_DELETE_ALL = 102

    private var historyList = emptyList<SearchUserHistory>()

    interface ItemClick { fun onClick(view: View, position: Int, searchUserHistory: SearchUserHistory, requestCode : Int) }
    var itemClick: ItemClick? = null

    companion object {
        const val TYPE_FIRST = 1
        const val TYPE_DEFAULT = 2
    }

    inner class TYPE_DEFAULT(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(searchUserHistory: SearchUserHistory, context: Context) {
            itemView.TextViewSearched.text = searchUserHistory.SearchText
            itemView.Btn_Delete.setOnClickListener{ itemClick?.onClick(it,adapterPosition,searchUserHistory,DEFAULT_ITEM_DELETE) }
        }
    }

    inner class TYPE_FIRST(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(searchUserHistory: SearchUserHistory, context: Context) {
            itemView.TextViewSearched.text = searchUserHistory.SearchText
            itemView.Btn_Delete_Item.setOnClickListener{ itemClick?.onClick(it,adapterPosition,searchUserHistory,FIRST_ITEM_DELETE) }
            itemView.Btn_Delete_All.setOnClickListener { itemClick?.onClick(it,adapterPosition,searchUserHistory,FIRST_ITEM_DELETE_ALL) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_DEFAULT -> {
                TYPE_DEFAULT(
                    LayoutInflater.from(context)
                        .inflate(R.layout.search_user_default_item, parent, false)
                )
            }

            TYPE_FIRST -> {
                TYPE_FIRST(
                    LayoutInflater.from(context)
                        .inflate(R.layout.search_user_first_item, parent, false)
                )
            }
            else -> throw IllegalArgumentException("Invalid view type")

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TYPE_DEFAULT -> {
                holder.itemView.run {
                    holder.bind(historyList[position],context)
                }
            }

            is TYPE_FIRST -> {
                holder.itemView.run {
                    holder.bind(historyList[position],context)
                }
            }
        }
    }

    override fun getItemCount() = historyList.size

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_FIRST
            else -> TYPE_DEFAULT
        }
    }

    internal fun setList(historyList: List<SearchUserHistory>) {
        this.historyList = historyList
        notifyDataSetChanged()
    }
}