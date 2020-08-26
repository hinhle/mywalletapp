package com.example.mywallet.financetracker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mywallet.R
import com.example.mywallet.database.Account
import com.example.mywallet.databinding.ListItemAccountBinding
import com.example.mywallet.databinding.ListItemAccountFooterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1
private const val ITEM_VIEW_TYPE_FOOTER = 2

class AccountAdapter(val clickListener : AccountListener, val footerClickListener : FooterListener? = null) : ListAdapter<DataItem, RecyclerView.ViewHolder>(AccountDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val nightItem = getItem(position) as DataItem.AccountItem
                holder.bind(nightItem.account, clickListener)
            }
            is FooterViewHolder -> {
                holder.bind(footerClickListener)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_FOOTER -> FooterViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewtype ${viewType}!")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)){
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.Footer -> ITEM_VIEW_TYPE_FOOTER
            is DataItem.AccountItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    fun addHeaderAndSubmitList(list : List<Account>?){
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header, DataItem.Footer)
                else -> listOf(DataItem.Header) + list.map {DataItem.AccountItem(it)} + listOf(DataItem.Footer)
            }
            withContext(Dispatchers.Main){
                submitList(items)
            }
        }


    }

    class TextViewHolder(view : View) : RecyclerView.ViewHolder(view){
        companion object{
            fun from(parent: ViewGroup) : TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.account_list_header, parent, false)
                return TextViewHolder(view)
            }
        }
    }

    class FooterViewHolder(val binding : ListItemAccountFooterBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(clickListener: FooterListener?) {
            clickListener?.let {
                    binding.clickListener = it
                    binding.executePendingBindings()
            }

        }
        companion object{
            fun from(parent: ViewGroup) : FooterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemAccountFooterBinding.inflate(layoutInflater, parent, false)
                return FooterViewHolder(binding)
            }
        }
    }

    class ViewHolder private constructor(val binding: ListItemAccountBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Account, clickListener: AccountListener) {
            binding.account = item
            binding.clickListener= clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemAccountBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}


class AccountDiffCallback : DiffUtil.ItemCallback<DataItem>() {

    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }


}

class AccountListener(val clickListener : (accountID : Long) -> Unit) {
    fun onClick(account: Account) = clickListener(account.AccountID)
}
class FooterListener(val clickListener : () -> Unit) {
    fun onClick() = clickListener()
}

sealed class DataItem {
    abstract val id: Long


    data class AccountItem(val account: Account) : DataItem() {
        override val id: Long
            get() = account.AccountID
    }

    object Header : DataItem() {
        override val id: Long
            get() = Long.MIN_VALUE
    }

    object Footer : DataItem() {
        override val id: Long
            get() = Long.MAX_VALUE
    }
}