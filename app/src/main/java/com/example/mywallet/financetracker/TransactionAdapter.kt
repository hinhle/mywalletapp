package com.example.mywallet.financetracker



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mywallet.R
import com.example.mywallet.database.Transaction
import com.example.mywallet.databinding.ListItemTransactionBinding
import com.example.mywallet.databinding.TransactionLimitListFooterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1
private const val ITEM_VIEW_TYPE_FOOTER = 2

class TransactionAdapter(val clickListener : TransactionListener, val footerClickListener : TransFooterListener? = null) : ListAdapter<DataItemII, RecyclerView.ViewHolder>(TransactionDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val item = getItem(position) as DataItemII.TransItem
                holder.bind(item.transaction, clickListener)
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
            is DataItemII.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItemII.Footer -> ITEM_VIEW_TYPE_FOOTER
            is DataItemII.TransItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    fun addHeaderAndSubmitList(list : List<Transaction>?){
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItemII.Header, DataItemII.Footer)
                else -> listOf(DataItemII.Header) + list.map {DataItemII.TransItem(it)} + listOf(DataItemII.Footer)
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
                val view = layoutInflater.inflate(R.layout.transaction_limit_list_header, parent, false)
                return TextViewHolder(view)
            }
        }
    }

    class FooterViewHolder(val binding : TransactionLimitListFooterBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(clickListener: TransFooterListener?) {
            clickListener?.let {
                binding.clickListener = it
                binding.executePendingBindings()
            }

        }
        companion object{
            fun from(parent: ViewGroup) : FooterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TransactionLimitListFooterBinding.inflate(layoutInflater, parent, false)
                return FooterViewHolder(binding)
            }
        }
    }

    class ViewHolder private constructor(val binding: ListItemTransactionBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Transaction, clickListener: TransactionListener) {
            binding.transaction = item
            binding.clickListener= clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTransactionBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}


class TransactionDiffCallback : DiffUtil.ItemCallback<DataItemII>() {

    override fun areItemsTheSame(oldItem: DataItemII, newItem: DataItemII): Boolean {
       return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItemII, newItem: DataItemII): Boolean {
        return oldItem == newItem
    }

}

class TransactionListener(val clickListener : (transactionID : Long) -> Unit) {
    fun onClick(transaction: Transaction) = clickListener(transaction.TransID)
}
class TransFooterListener(val clickListener : () -> Unit) {
    fun onClick() = clickListener()
}

sealed class DataItemII {
    abstract val id: Long


    data class TransItem(val transaction: Transaction) : DataItemII() {
        override val id: Long
            get() = transaction.TransID
    }

    object Header : DataItemII() {
        override val id: Long
            get() = Long.MIN_VALUE
    }

    object Footer : DataItemII() {
        override val id: Long
            get() = Long.MAX_VALUE
    }
}