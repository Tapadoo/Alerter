package com.tapadoo.alerter.demo

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.layout_modal_bottom_sheet.btnAlertColoured
import kotlinx.android.synthetic.main.layout_modal_bottom_sheet.btnAlertCustomIcon
import kotlinx.android.synthetic.main.layout_modal_bottom_sheet.btnAlertDefault

class CustomBottomSheetDialogFragment : BottomSheetDialogFragment() {

    var appCompatDialog: AppCompatDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_modal_bottom_sheet, container, false)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        if (dialog is AppCompatDialog) {

            // If the dialog is an AppCompatDialog, we'll handle it
            appCompatDialog = dialog
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btnAlertDefault.setOnClickListener {
            //handle click event
            showAlertDefault(appCompatDialog)
        }
        btnAlertColoured.setOnClickListener {
            //handle click event
            showAlertColoured(appCompatDialog)
        }
        btnAlertCustomIcon.setOnClickListener {
            //handle click event
            showAlertWithIcon(appCompatDialog)
        }
    }

    private fun showAlertDefault(view: Dialog?) {
        view?.let {
            Alerter.create(dialog = it)
                    .setTitle(R.string.title_activity_example)
                    .setText("Alert text...")
                    .show()
        }
    }

    private fun showAlertColoured(view: Dialog?) {
        view?.let {
            Alerter.create(dialog = it)
                    .setTitle("Alert Title")
                    .setText("Alert text...")
                    .setBackgroundColorRes(R.color.colorAccent)
                    .show()
        }
    }

    private fun showAlertWithIcon(view: Dialog?) {
        view?.let {
            Alerter.create(dialog = it)
                    .setText("Alert text...")
                    .setIcon(R.drawable.alerter_ic_mail_outline)
                    .setIconColorFilter(0) // Optional - Removes white tint
                    .setIconSize(R.dimen.custom_icon_size) // Optional - default is 38dp
                    .show()
        }
    }

    override fun onDestroy() {
        appCompatDialog = null
        super.onDestroy()
    }

    companion object {
        const val TAG = "CustomBottomSheetDialogFragment"
    }
}