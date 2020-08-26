package com.example.mywallet.transactioncategory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mywallet.R
import com.example.mywallet.database.Category
import com.example.mywallet.databinding.FragmentTransactionCategoryBinding


/**
 * A simple [Fragment] subclass.
 * Use the [TransactionCategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionCategoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentTransactionCategoryBinding>(
            inflater,
            R.layout.fragment_transaction_category, container, false
        )
        val data = mutableListOf(
            Category.Entertainment,
            Category.Media, Category.Finance,
            Category.Vehicle, Category.FoodAndBeverage,
            Category.House, Category.Income, Category.Investment
        )
        val adapter = CategoryAdapter(
            CategoryListener {
                this.findNavController().navigate(
                    TransactionCategoryFragmentDirections.actionTransactionCategoryFragmentToTransactionFormFragment()
                        .setCategory(it)
                )
            }
        )

        binding.categoryList.adapter = adapter
        adapter.addHeaderAndSubmitList(data)

        // Inflate the layout for this fragment
        return binding.root
    }


}