package com.example.mywallet.accountdetail

import android.app.ActionBar
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.mywallet.R
import com.example.mywallet.database.WalletDatabase
import com.example.mywallet.databinding.FragmentAccountDetailBinding
import com.example.mywallet.databinding.FragmentFinanceTrackerBinding
import com.example.mywallet.financetracker.FinanceTrackerViewModel
import com.example.mywallet.financetracker.FinanceTrackerViewModelFactory

class AccountDetailFragment : Fragment(){
    private lateinit var binding : FragmentAccountDetailBinding
    lateinit var accountDetailViewModel : AccountDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentAccountDetailBinding>(inflater,
            R.layout.fragment_account_detail,container,false)
        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource = WalletDatabase.getInstance(application).walletDatabaseDao
        val viewModelFactory = AccountDetailViewModelFactory(dataSource, application)

         accountDetailViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(AccountDetailViewModel::class.java)

        accountDetailViewModel.navigateToFinanceTracker.observe(viewLifecycleOwner, Observer {
            if (it == true){
                this.findNavController().navigate(
                    AccountDetailFragmentDirections.actionAccountDetailFragmentToFinanceTrackerFragment()
                )
                accountDetailViewModel.onFinanceTrackerNavigated()

            }
        })

        accountDetailViewModel.accountType.observe(viewLifecycleOwner, Observer {
            binding.accountTypeText.text = it.vieName
        })
        
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.title = "Thiết lập tài khoản"
        binding.accountViewModel = accountDetailViewModel

        binding.accountTypeText.setOnClickListener {

            val accountTypeDialog = AccountTypeDialog()
            accountTypeDialog.show(fragmentManager,tag)
            /*val arguments = AccountDetailFragmentArgs.fromBundle(arguments!!)
            Log.i("ReceiveAccountType", arguments.accountTypeVieName)
            accountDetailViewModel.editAccountType(arguments.accountTypeVieName)*/
        }


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.adding_account_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return  when (item!!.itemId){
                R.id.saveAccount -> {
                    saveAccount()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    }

    private fun saveAccount(){
        accountDetailViewModel.accountName = binding.accountNameEdit.text.toString()
        accountDetailViewModel.accountBalance = binding.accountBalanceEdit.text.toString().toLong()
        accountDetailViewModel.onSave()
        accountDetailViewModel.onFinanceTrackerClicked()
    }

//    override fun onResume() {
//
//        val arguments = AccountDetailFragmentArgs.fromBundle(arguments!!)
//        Log.i("ReceiveAccountType", arguments.accountTypeVieName)
//        accountDetailViewModel.editAccountType(arguments.accountTypeVieName)
//        super.onResume()
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == 1998) {
//            val accountType = data?.getStringExtra(
//                "account_type"
//            )
//            accountDetailViewModel.editAccountType(accountType!!)
//        }
//    }

}