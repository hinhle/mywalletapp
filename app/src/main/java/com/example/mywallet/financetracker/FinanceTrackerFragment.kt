package com.example.mywallet.financetracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mywallet.R
import com.example.mywallet.database.WalletDatabase
import com.example.mywallet.databinding.FragmentFinanceTrackerBinding


class FinanceTrackerFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentFinanceTrackerBinding>(
            inflater,
            R.layout.fragment_finance_tracker, container, false
        )
        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource = WalletDatabase.getInstance(application).walletDatabaseDao
        val viewModelFactory = FinanceTrackerViewModelFactory(dataSource, application)

        val financeTrackerViewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(FinanceTrackerViewModel::class.java)

        binding.financeTrackerViewModel = financeTrackerViewModel

        val adapter = AccountAdapter(
            AccountListener { accountID ->
                Toast.makeText(context, "$accountID", Toast.LENGTH_LONG).show()
                //sleepTrackerViewModel.onSleepNightClicked(nightID)
            },
            FooterListener {
                financeTrackerViewModel.onAccountDetailClicked()
            })
        binding.accountList.adapter = adapter

        financeTrackerViewModel.accounts.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })

        financeTrackerViewModel.navigateToAccountDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    this.findNavController().navigate(
                        FinanceTrackerFragmentDirections
                            .actionFinanceTrackerFragmentToAccountDetailFragment()
                    )
                    financeTrackerViewModel.onAccountDetailNavigated()
                }

            }
        })

        binding.floatingActionButton.setOnClickListener {
            this.findNavController().navigate(
                FinanceTrackerFragmentDirections
                    .actionFinanceTrackerFragmentToTransactionFormFragment()
            )
        }


        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.lifecycleOwner = this

        val manager = GridLayoutManager(activity, 3)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                0 -> 3
                else -> 1
            }

        }
        binding.accountList.layoutManager = manager

        return binding.root
    }


}