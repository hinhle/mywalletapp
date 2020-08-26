package com.example.mywallet.transactioncategory


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mywallet.R
import com.example.mywallet.database.Category
import com.example.mywallet.databinding.ListItemTransactionCategoryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class CategoryAdapter(val clickListener: CategoryListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(CategoryDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val categoryItem = getItem(position) as DataItem.CategoryItem
                holder.bind(categoryItem.category, clickListener)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewtype ${viewType}!")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.CategoryItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    fun addHeaderAndSubmitList(list: List<Category>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.CategoryItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }


    }

    class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.category_list_header, parent, false)
                return TextViewHolder(view)
            }
        }
    }


}

class ViewHolder private constructor(val binding: ListItemTransactionCategoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Category, clickListener: CategoryListener) {
        binding.category = item
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemTransactionCategoryBinding.inflate(layoutInflater, parent, false)
            return ViewHolder(binding)
        }
    }
}


class CategoryDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}


class CategoryListener(val clickListener: (name: String) -> Unit) {
    fun onClick(category: Category) = clickListener(category.vieName)
}


sealed class DataItem {
    abstract val id: Int


    data class CategoryItem(val category: Category) : DataItem() {
        override val id: Int
            get() = category.ordinal
    }

    object Header : DataItem() {
        override val id: Int
            get() = Int.MIN_VALUE
    }

}