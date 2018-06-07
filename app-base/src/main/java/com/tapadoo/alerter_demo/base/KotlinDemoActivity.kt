package com.tapadoo.alerter_demo.base

import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast

import com.tapadoo.alerter.Alerter
import com.tapadoo.alerter.OnHideAlertListener
import com.tapadoo.alerter.OnShowAlertListener
import kotlinx.android.synthetic.main.content_example.*

class KotlinDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        setupOnClickListeners()
    }

    override fun onStart() {
        super.onStart()

        window.setBackgroundDrawableResource(android.R.color.white)
    }

    private fun showAlertDefault() {
        Alerter.create(this@KotlinDemoActivity)
                .setTitle(R.string.title_activity_example)
                .setText("Alert text...")
                .show()
    }

    private fun showAlertColoured() {
        Alerter.create(this@KotlinDemoActivity)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .setBackgroundColorRes(R.color.colorAccent)
                .show()
    }

    private fun showAlertWithIcon() {
        Alerter.create(this@KotlinDemoActivity)
                .setText("Alert text...")
                .setIcon(R.drawable.alerter_ic_mail_outline)
                .setIconColorFilter(0) // Optional - Removes white tint
                .show()
    }

    private fun showAlertTextOnly() {
        Alerter.create(this@KotlinDemoActivity)
                .setText("Alert text...")
                .show()
    }

    private fun showAlertWithOnClick() {
        Alerter.create(this@KotlinDemoActivity)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .setDuration(10000)
                .setOnClickListener(View.OnClickListener { Toast.makeText(this@KotlinDemoActivity, "OnClick Called", Toast.LENGTH_LONG).show() })
                .show()
    }

    private fun showAlertVerbose() {
        Alerter.create(this@KotlinDemoActivity)
                .setTitle("Alert Title")
                .setText("The alert scales to accommodate larger bodies of text. " +
                        "The alert scales to accommodate larger bodies of text. " +
                        "The alert scales to accommodate larger bodies of text.")
                .show()
    }

    private fun showAlertCallbacks() {
        Alerter.create(this@KotlinDemoActivity)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .setDuration(10000)
                .setOnShowListener(OnShowAlertListener {
                    Toast.makeText(this@KotlinDemoActivity, "Show Alert", Toast.LENGTH_LONG).show()
                })
                .setOnHideListener(OnHideAlertListener {
                    Toast.makeText(this@KotlinDemoActivity, "Hide Alert", Toast.LENGTH_LONG).show()
                })
                .show()
    }

    private fun showAlertInfiniteDuration() {
        Alerter.create(this@KotlinDemoActivity)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .enableInfiniteDuration(true)
                .show()
    }

    private fun showAlertWithProgress() {
        Alerter.create(this@KotlinDemoActivity)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .enableProgress(true)
                .setProgressColorRes(R.color.colorPrimary)
                .show()
    }

    private fun showAlertWithCustomFont() {
        Alerter.create(this@KotlinDemoActivity)
                .setTitle("Alert Title")
                .setTitleAppearance(R.style.AlertTextAppearance_Title)
                .setTitleTypeface(Typeface.createFromAsset(assets, "Pacifico-Regular.ttf"))
                .setText("Alert text...")
                .setTextAppearance(R.style.AlertTextAppearance_Text)
                .setTextTypeface(Typeface.createFromAsset(assets, "ScopeOne-Regular.ttf"))
                .show()
    }

    private fun showAlertSwipeToDismissEnabled() {
        Alerter.create(this@KotlinDemoActivity)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .enableSwipeToDismiss()
                .setOnHideListener(OnHideAlertListener {
                    Toast.makeText(this@KotlinDemoActivity, "Hide Alert", Toast.LENGTH_LONG).show()
                })
                .show()
    }

    private fun setupOnClickListeners() {
        btnAlertDefault.setOnClickListener {
            showAlertDefault()
        }

        btnAlertColoured.setOnClickListener {
            showAlertColoured()
        }

        btnAlertCustomIcon.setOnClickListener {
            showAlertWithIcon()
        }

        btnAlertTextOnly.setOnClickListener {
            showAlertTextOnly()
        }

        btnAlertOnClick.setOnClickListener {
            showAlertWithOnClick()
        }

        btnAlertVerbose.setOnClickListener {
            showAlertVerbose()
        }

        btnAlertCallback.setOnClickListener {
            showAlertCallbacks()
        }

        btnAlertInfiniteDuration.setOnClickListener {
            showAlertInfiniteDuration()
        }

        btnAlertWithProgress.setOnClickListener {
            showAlertWithProgress()
        }

        btnAlertWithCustomFont.setOnClickListener {
            showAlertWithCustomFont()
        }

        btnAlertSwipeToDismissEnabled.setOnClickListener {
            showAlertSwipeToDismissEnabled()
        }
    }

}
