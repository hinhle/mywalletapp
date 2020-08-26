package com.example.mywallet.accountdetail


import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.mywallet.R
import com.example.mywallet.database.WalletDatabase
import com.example.mywallet.databinding.FragmentAccountDetailBinding


class AccountDetailFragment : Fragment() {
    private lateinit var binding: FragmentAccountDetailBinding
    lateinit var accountDetailViewModel: AccountDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_account_detail, container, false
        )
        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource = WalletDatabase.getInstance(application).walletDatabaseDao
        val viewModelFactory = AccountDetailViewModelFactory(dataSource, application)

        binding.lifecycleOwner = this

        accountDetailViewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(AccountDetailViewModel::class.java)

        accountDetailViewModel.navigateToFinanceTracker.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController().navigate(
                    AccountDetailFragmentDirections.actionAccountDetailFragmentToFinanceTrackerFragment()
                )
                accountDetailViewModel.onFinanceTrackerNavigated()

            }
        })

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.title = "Thiết lập tài khoản"
        binding.accountViewModel = accountDetailViewModel

        binding.accountTypeText.setOnClickListener {

            val accountTypeDialog = AccountTypeDialog()
            accountTypeDialog.show(fragmentManager!!, tag)
        }

        binding.accountNameEdit.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.warningLabel.visibility = View.INVISIBLE
            }
        }

        binding.accountNameEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.warningLabel.visibility = View.INVISIBLE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.warningLabel.visibility = View.INVISIBLE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.warningLabel.visibility = View.INVISIBLE
            }

        })


        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.adding_account_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (binding.accountBalanceEdit.text.toString().toLong() != 0L)
                    openDialog()
                else findNavController().navigateUp()
                true
            }
            R.id.saveAccount -> {
                if (validateInput())
                    saveAccount()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun saveAccount() {
        accountDetailViewModel.accountName = binding.accountNameEdit.text.toString()
        accountDetailViewModel.accountBalance = binding.accountBalanceEdit.text.toString().toLong()
        accountDetailViewModel.onSave()
        accountDetailViewModel.onFinanceTrackerClicked()
    }

    private fun validateInput(): Boolean {
        if (binding.accountNameEdit.text.trim().isNullOrEmpty()) {
            binding.warningLabel.visibility = View.VISIBLE
            return false

        }
        binding.warningLabel.visibility = View.INVISIBLE
        return true
    }

    private fun openDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)

        builder.setTitle("Thoát")
        builder.setMessage("Bạn thực sự muốn thoát mà không lưu?")

        builder.setPositiveButton(
            "CÓ"
        ) { dialog, which -> // Do nothing but close the dialog
            findNavController().navigateUp()
            dialog.dismiss()
        }

        builder.setNegativeButton(
            "KHÔNG"
        ) { dialog, which -> // Do nothing
            dialog.dismiss()
        }

        val alert: AlertDialog = builder.create()
        alert.show()
    }
}


