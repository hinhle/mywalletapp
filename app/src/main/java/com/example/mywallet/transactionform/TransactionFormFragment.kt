package com.example.mywallet.transactionform


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mywallet.R
import com.example.mywallet.databinding.FragmentTransactionFormBinding
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentTransactionFormBinding>(
            inflater,
            R.layout.fragment_transaction_form, container, false
        )
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
        }
        binding.expenditureButton.setOnClickListener {
            binding.signText.text = "-"
        }
        binding.categoryLabel.setOnClickListener { navigateToCategoryList() }
        binding.categoryText.setOnClickListener { navigateToCategoryList() }

        val args = TransactionFormFragmentArgs.fromBundle(arguments!!)
        binding.categoryText.text = args.category
        // Inflate the layout for this fragment
        return binding.root
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
}