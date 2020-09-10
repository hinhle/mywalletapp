import android.util.Log
import android.view.LayoutInflater

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.example.mywallet.databinding.ListItemTransactionBinding
import com.example.mywallet.databinding.ListItemTransactionRevenueBinding
import com.example.mywallet.financetracker.TransactionListView
import com.example.mywallet.financetracker.TransactionListener
import com.example.mywallet.transactionstatistic.Revenue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_ITEM = 1
private const val ITEM_VIEW_TYPE_REVENUE = 2

class TransactionStatisticAdapter(
    val clickListener: TransactionListener
) : ListAdapter<DataItemII, RecyclerView.ViewHolder>(TransactionDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val item = getItem(position) as DataItemII.TransItem
                holder.bind(item.transaction, clickListener)
            }
            is RevenueViewHolder -> {
                val item = getItem(position) as DataItemII.RevenueItem
                holder.bind(item.revenue)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_REVENUE -> RevenueViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewtype ${viewType}!")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItemII.RevenueItem -> ITEM_VIEW_TYPE_REVENUE
            is DataItemII.TransItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    fun addHeaderAndSubmitList(transList: List<TransactionListView>, revenueList: List<Revenue>) {
        adapterScope.launch {
            val items : MutableList<DataItemII> = mutableListOf()
            var ptr = 0
                for (i in revenueList.indices) {
                    items.add(DataItemII.RevenueItem(revenueList[i]))
                    var j = 0
                    for (j in 0 until revenueList[i].quantity) {
                        items.add(DataItemII.TransItem(transList[ptr + j]))
                    }
                    ptr += revenueList[i].quantity
                }

            withContext(Dispatchers.Main) {
                submitList(items)
            }
            Log.i("inside submit list", "oke")
        }


    }



    class RevenueViewHolder(val binding: ListItemTransactionRevenueBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Revenue) {
            binding.revenue = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RevenueViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ListItemTransactionRevenueBinding.inflate(layoutInflater, parent, false)
                return RevenueViewHolder(binding)
            }
        }
    }

    class ViewHolder private constructor(val binding: ListItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TransactionListView, clickListener: TransactionListener) {
            binding.transaction = item
            binding.clickListener = clickListener
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





sealed class DataItemII {
    abstract val id: Long


    data class TransItem(val transaction: TransactionListView) : DataItemII() {
        override val id: Long
            get() = transaction.TransID
    }


    data  class RevenueItem(val revenue: Revenue) : DataItemII() {
        override val id: Long
            get() = - revenue.revenueID
    }
}