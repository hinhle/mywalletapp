package com.example.mywallet.transactionstatistic

import TransactionStatisticAdapter
import android.graphics.Color
import android.os.Build

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mywallet.R
import com.example.mywallet.database.WalletDatabase

import com.example.mywallet.databinding.FragmentTransactionStatisticBinding

import com.example.mywallet.financetracker.TransactionListener



class TransactionStatisticFragment : Fragment() {

    private lateinit var binding : FragmentTransactionStatisticBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentTransactionStatisticBinding>(
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

        transactionStatisticViewModel.onSevenDayClick()
        transactionStatisticViewModel.transactions.observe(viewLifecycleOwner, Observer { transaction ->

            transaction?.let {
                transactionStatisticViewModel.onSevenDaysStatistic(it)
                transAdapter.addHeaderAndSubmitList(revenueList = transactionStatisticViewModel.revenueList.value!!, transList = transaction)
            }

        })
        binding.sevenDayButton.setBackgroundResource(R.drawable.rounded_button)
        binding.sevenDayButton.setTextColor(Color.WHITE)


        binding.sevenDayButton.setOnClickListener {
            onChangeButtonBackground(it)
            transactionStatisticViewModel.onSevenDayClick()
            transactionStatisticViewModel.transactions.observe(viewLifecycleOwner, Observer { transaction ->

                    transaction?.let {
                        transactionStatisticViewModel.onSevenDaysStatistic(it)
                        transAdapter.addHeaderAndSubmitList(revenueList = transactionStatisticViewModel.revenueList.value!!, transList = transaction)
                    }

            })
        }
        binding.thirtyDayButton.setOnClickListener {
            onChangeButtonBackground(it)
            transactionStatisticViewModel.onThirtyDayClick()
            transactionStatisticViewModel.transactions.observe(viewLifecycleOwner, Observer { transaction ->

                transaction?.let {
                    transactionStatisticViewModel.onThirtyDayStatistic(it)
                    transAdapter.addHeaderAndSubmitList(revenueList = transactionStatisticViewModel.revenueList.value!!, transList = transaction)
                }

            })
        }
        binding.twelveWeekButton.setOnClickListener {
            onChangeButtonBackground(it)
            transactionStatisticViewModel.onTwelveWeekClick()
            transactionStatisticViewModel.transactions.observe(viewLifecycleOwner, Observer { transaction ->

                transaction?.let {
                    transactionStatisticViewModel.onTwelveWeekStatistic(it)
                    transAdapter.addHeaderAndSubmitList(revenueList = transactionStatisticViewModel.revenueList.value!!, transList = transaction)
                }

            })
        }
        binding.sixMonthButton.setOnClickListener {
            onChangeButtonBackground(it)
            transactionStatisticViewModel.onSixMonthClick()
            transactionStatisticViewModel.transactions.observe(viewLifecycleOwner, Observer { transaction ->

                transaction?.let {
                    transactionStatisticViewModel.onSixMonthStatistic(it)
                    transAdapter.addHeaderAndSubmitList(revenueList = transactionStatisticViewModel.revenueList.value!!, transList = transaction)
                }

            })
        }
        binding.oneYearButton.setOnClickListener {
            onChangeButtonBackground(it)
            transactionStatisticViewModel.onOneYearClick()
            transactionStatisticViewModel.transactions.observe(viewLifecycleOwner, Observer { transaction ->

                transaction?.let {
                    transactionStatisticViewModel.onOneYearStatistic(it)
                    transAdapter.addHeaderAndSubmitList(revenueList = transactionStatisticViewModel.revenueList.value!!, transList = transaction)
                }

            })
        }

        (activity as AppCompatActivity).supportActionBar?.title = "Các Giao Dịch"

        return binding.root
    }


    fun onChangeButtonBackground(v : View){
        val buttonList = listOf(binding.sevenDayButton, binding.thirtyDayButton
                            , binding.twelveWeekButton, binding.sixMonthButton
                            , binding.oneYearButton)
        buttonList.forEach {
            it.setBackgroundResource(R.drawable.rounded_bordered_button)
            it.setTextColor(Color.rgb(52,152,219))
        }
        (v as Button).setBackgroundResource(R.drawable.rounded_button)
        v.setTextColor(Color.WHITE)

    }

}