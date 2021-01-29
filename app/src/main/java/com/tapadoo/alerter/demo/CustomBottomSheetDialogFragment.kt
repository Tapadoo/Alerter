package com.tapadoo.alerter.demo

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.layout_modal_bottom_sheet.btnAlertColoured
import kotlinx.android.synthetic.main.layout_modal_bottom_sheet.btnAlertCustomIcon
import kotlinx.android.synthetic.main.layout_modal_bottom_sheet.btnAlertDefault

class CustomBottomSheetDialogFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_modal_bottom_sheet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btnAlertDefault.setOnClickListener {
            //handle click event
            showAlertDefault(dialog)
        }
        btnAlertColoured.setOnClickListener {
            //handle click event
            showAlertColoured(dialog)
        }
        btnAlertCustomIcon.setOnClickListener {
            //handle click event
            showAlertWithIcon(dialog)
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

}