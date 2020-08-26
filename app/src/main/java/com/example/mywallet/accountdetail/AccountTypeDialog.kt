package com.example.mywallet.accountdetail

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import com.example.mywallet.R


class AccountTypeDialog : DialogFragment() {
    private val array =
        arrayOf("Thường", "Tiền mặt", "Thẻ tín dụng", "Tài khoản tiết kiệm", "Bảo hiểm", "Đầu tư")

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.choose_account_type)
                .setItems(
                    array
                ) { dialog, which ->
                    val accountType = array[which]
                    val fragment = fragmentManager?.fragments
                    (fragment!!.get(0) as AccountDetailFragment).accountDetailViewModel.editAccountType(
                        accountType
                    )
                    Log.i("accountTypeArg", accountType)
                    //sendResult(1998,accountType)
                    view?.findNavController()?.navigate(
                        AccountTypeDialogDirections.actionAccountTypeDialogToAccountDetailFragment()
                            .setAccountTypeVieName(accountType)
                    )

                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}