package com.example.mywallet.accountlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mywallet.R
import com.example.mywallet.database.WalletDatabase


class AccountListFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account_list, container, false)

        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource = WalletDatabase.getInstance(application).walletDatabaseDao
        val viewModelFactory = AccountListViewModelFactory(dataSource, application)

        val accountListViewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(AccountListViewModel::class.java)

        val accountList = view.findViewById<RecyclerView>(R.id.accountList)
        val adapter = AccountAdapter(
            AccountListener { accountID, accountName ->
                val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@AccountListener
                with (sharedPref.edit()) {
                    putString(getString(R.string.account_name_key), accountName)
                    putLong(getString(R.string.account_id_key), accountID)
                    apply()
                }
                this.findNavController().navigate(
                    AccountListFragmentDirections.actionAccountListFragmentToTransactionFormFragment()
                )

            })
        accountList.adapter = adapter

        accountListViewModel.accounts.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })

        (activity as AppCompatActivity).title = "Tài khoản"
        return view
    }


}