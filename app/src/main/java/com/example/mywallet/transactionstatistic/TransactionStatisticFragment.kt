package com.example.mywallet.transactionstatistic

import TransactionStatisticAdapter
import android.os.Build

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mywallet.R
import com.example.mywallet.database.WalletDatabase

import com.example.mywallet.databinding.FragmentTransactionStatisticBinding

import com.example.mywallet.financetracker.TransactionListener



class TransactionStatisticFragment : Fragment() {



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentTransactionStatisticBinding>(
            inflater,
            R.layout.fragment_transaction_statistic, container, false
        )
        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource = WalletDatabase.getInstance(application).walletDatabaseDao
        val viewModelFactory = TransactionStatisticViewModelFactory(dataSource)

        val transactionStatisticViewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(TransactionStatisticViewModel::class.java)
        val transAdapter = TransactionStatisticAdapter(
            TransactionListener { transactionID ->
                Toast.makeText(activity!!.applicationContext, "$transactionID", Toast.LENGTH_LONG)
                    .show()
            }
        )
        binding.statisticList.adapter = transAdapter


        binding.sevenDayButton.setOnClickListener {
            transactionStatisticViewModel.transactions.observe(viewLifecycleOwner, Observer { transaction ->

                    transaction?.let {
                        transactionStatisticViewModel.onSevenDaysStatistic(it)
                        transAdapter.addHeaderAndSubmitList(revenueList = transactionStatisticViewModel.revenueList.value!!, transList = transaction)
                    }

            })
        }
        binding.thirtyDayButton.setOnClickListener {
            transactionStatisticViewModel.transactions.observe(viewLifecycleOwner, Observer { transaction ->

                transaction?.let {
                    transactionStatisticViewModel.onThirtyDayStatistic(it)
                    transAdapter.addHeaderAndSubmitList(revenueList = transactionStatisticViewModel.revenueList.value!!, transList = transaction)
                }

            })
        }
        return binding.root
    }


}