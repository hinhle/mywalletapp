package com.example.mywallet.transactionform


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.mywallet.R
import com.example.mywallet.database.Category
import com.example.mywallet.database.WalletDatabase
import com.example.mywallet.databinding.FragmentTransactionFormBinding
import com.google.android.material.snackbar.Snackbar
import net.objecthunter.exp4j.ExpressionBuilder
import java.text.DecimalFormat
import java.text.NumberFormat


/**
 * A simple [Fragment] subclass.
 * Use the [TransactionFormFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionFormFragment : Fragment() {
    // TextView used to display the input and output
    lateinit var txtInput: TextView

    // Represent whether the lastly pressed key is numeric or not
    var lastNumeric: Boolean = false
    // Represent that current state is in error or not
    var stateError: Boolean = false
    // If true, do not allow to add another DOT
    var lastDot: Boolean = false
    private var expression = ""
    private var numberString = ""
    val formatter: NumberFormat = DecimalFormat("#,###")

    private lateinit var transactionFormViewModel: TransactionFormViewModel

    private lateinit var categoryArg : Category
    private  var accountIDArg: Long = 0L

    private var money : Long = 0L
    get() = if (!txtInput.text.toString().isNullOrEmpty()) numberString.toLong() else 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentTransactionFormBinding>(
            inflater,
            R.layout.fragment_transaction_form, container, false
        )

        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory.
        val dataSource = WalletDatabase.getInstance(application).walletDatabaseDao
        val viewModelFactory = TransactionFormViewModelFactory(dataSource, application)
        transactionFormViewModel =
            ViewModelProviders.of(
                this, viewModelFactory
            ).get(TransactionFormViewModel::class.java)

        //val view = inflater.inflate(R.layout.fragment_transaction_form, container, false)
        txtInput = binding.txtInput

        val digitViews = listOf(
            binding.btnOne, binding.btnTwo,
            binding.btnThree, binding.btnFour,
            binding.btnFive, binding.btnSix,
            binding.btnSeven, binding.btnEight,
            binding.btnNine, binding.btnZero
        )
        digitViews.forEach { it.setOnClickListener { onDigit(it) } }

        val operatorViews = listOf(
            binding.btnAdd, binding.btnSubtract,
            binding.btnMultiply, binding.btnDivide
        )
        operatorViews.forEach { it.setOnClickListener { onOperator(it) } }

        binding.btnClear.setOnClickListener { onClear(it) }
        binding.btnEqual.setOnClickListener { onEqual(it) }

        binding.signText.text = "+"

        binding.incomeButton.setOnClickListener {
            binding.signText.text = "+"
            transactionFormViewModel.isIncome = true
        }
        binding.expenditureButton.setOnClickListener {
            binding.signText.text = "-"
            transactionFormViewModel.isIncome = false
        }

        binding.categoryLabel.setOnClickListener { navigateToCategoryList() }
        binding.categoryText.setOnClickListener { navigateToCategoryList() }

        //val args = TransactionFormFragmentArgs.fromBundle(arguments!!)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return binding.root
        val categoryPref = sharedPref.getString(getString(R.string.category_name_key),Category.Other.name)
        val accountName = sharedPref.getString(getString(R.string.account_name_key),"Chọn tài khoản")
        categoryArg = Category.valueOf(categoryPref!!)
        accountIDArg= sharedPref.getLong(getString(R.string.account_id_key), -1L)
//        if (args.accountId != -1L){
//            onSave(args.accountId, categoryArg)
//        }

        binding.categoryText.text = categoryArg.vieName
        binding.accountText.text = accountName

        binding.accountLabel.setOnClickListener { navigateToAccountList() }
        binding.accountText.setOnClickListener { navigateToAccountList() }

        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.adding_account_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.saveAccount -> {
                Log.i("Button clicked","OK")
                if (accountIDArg != -1L && isValidated()){
                    Log.i("inside if block", "OK")
                    onSave(accountID = accountIDArg, category = categoryArg)
                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    fun onDigit(view: View) {
        numberString += (view as Button).text
        expression += view.text

        val str = formatter.format(numberString.toLong())
        if (stateError) {
            // If current state is Error, replace the error message
            txtInput.text = str
            stateError = false
        } else {
            // If not, already there is a valid expression so append to it
            txtInput.text = str

        }
        // Set the flag
        lastNumeric = true
    }

    /**
     * Append . to the TextView
     */
    fun onDecimalPoint(view: View) {
        if (lastNumeric && !stateError && !lastDot) {
            numberString = numberString.dropLast(1)
            val str = formatter.format(numberString.toLong())
            txtInput.text = str
            //txtInput.append(".")
            lastNumeric = false
            lastDot = true
        }
    }

    /**
     * Append +,-,*,/ operators to the TextView
     */
    fun onOperator(view: View) {
        if (lastNumeric && !stateError) {
            expression += (view as Button).text
            numberString = ""
            //txtInput.append((view as Button).text)
            lastNumeric = false
            lastDot = false    // Reset the DOT flag
        }
    }


    /**
     * Clear the TextView
     */
    fun onClear(view: View) {
        expression = ""
        this.txtInput.text = ""
        lastNumeric = false
        stateError = false
        lastDot = false
    }

    /**
     * Calculate the output using Exp4j
     */
    fun onEqual(view: View) {
        // If the current state is error, nothing to do.
        // If the last input is a number only, solution can be found.
        if (lastNumeric && !stateError) {
            // Read the expression
            val txt = this.expression//txtInput.text.toString()
            // Create an Expression (A class from exp4j library)
            val expression = ExpressionBuilder(txt).build()
            try {
                // Calculate the result and display
                val result = expression.evaluate()
                txtInput.text = formatter.format(result)
                lastDot = true // Result contains a dot
                numberString = result.toString()
                this.expression = result.toString()
            } catch (ex: ArithmeticException) {
                // Display an error message
                txtInput.text = "Error"
                stateError = true
                lastNumeric = false
                numberString = ""
            }
        }
    }

    fun navigateToCategoryList() {
        this.findNavController().navigate(
            TransactionFormFragmentDirections.actionTransactionFormFragmentToTransactionCategoryFragment()
        )
    }

    fun navigateToAccountList() {
        this.findNavController().navigate(
            TransactionFormFragmentDirections.actionTransactionFormFragmentToAccountListFragment()
        )
    }

    fun onSave(accountID : Long, category: Category){
        transactionFormViewModel.onSave(accountID, category)
        Log.i("TransactionFormFragment",accountID.toString())
        transactionFormViewModel.onChangeBalance(money, accountID)
        Toast.makeText(context,"Thêm giao dịch thành công",Toast.LENGTH_SHORT)
    }
    fun isValidated() : Boolean {
        if (txtInput.text.toString().isNullOrEmpty()){
            Snackbar.make(view!!, "Xin vui lòng nhập số tiền", Snackbar.LENGTH_SHORT)
                .show()
            return false
        }
        return true
    }
    fun navigateToFinanceTracker(){
        this.findNavController().navigate(
            TransactionFormFragmentDirections.actionTransactionFormFragmentToFinanceTrackerFragment()
        )
    }
}