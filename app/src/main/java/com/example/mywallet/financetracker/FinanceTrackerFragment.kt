package com.example.mywallet.financetracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mywallet.R
import com.example.mywallet.accountdetail.AccountDetailFragment
import com.example.mywallet.database.WalletDatabase
import com.example.mywallet.databinding.FragmentFinanceTrackerBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FinanceTrackerFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentFinanceTrackerBinding>(inflater,
            R.layout.fragment_finance_tracker,container,false)
        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource = WalletDatabase.getInstance(application).walletDatabaseDao
        val viewModelFactory = FinanceTrackerViewModelFactory(dataSource, application)

        val financeTrackerViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(FinanceTrackerViewModel::class.java)

        binding.financeTrackerViewModel = financeTrackerViewModel

        val adapter = AccountAdapter(AccountListener { accountID ->
            Toast.makeText(context, "${accountID}", Toast.LENGTH_LONG).show()
            //sleepTrackerViewModel.onSleepNightClicked(nightID)
        },
        FooterListener{
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
                this.findNavController().navigate(
                    FinanceTrackerFragmentDirections
                        .actionFinanceTrackerFragmentToAccountDetailFragment()
                )
            }
        })

        // Specify the current activity as the lifecycle owner of the binding.
        // This is necessary so that the binding can observe LiveData updates.
        binding.setLifecycleOwner(this)

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