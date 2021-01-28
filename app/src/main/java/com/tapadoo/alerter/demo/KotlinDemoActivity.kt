package com.tapadoo.alerter.demo

import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.tapadoo.alerter.Alerter
import com.tapadoo.alerter.OnHideAlertListener
import com.tapadoo.alerter.OnShowAlertListener
import kotlinx.android.synthetic.main.content_example.*
import kotlinx.android.synthetic.main.custom_layout.view.*

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
                .setIconSize(R.dimen.custom_icon_size) // Optional - default is 38dp
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
                .setText("The alert scales to accommodate larger bodies of text." +
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
                .setTitle("Loading")
                .setText("Tap to dismiss")
                .enableProgress(true)
                .enableInfiniteDuration(true)
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

    private fun showAlertWithCustomColor() {
        var alert = Alerter.create(this@KotlinDemoActivity)
                .setTitle("Yellow Alert Title")
                .setText("Red Alert text...")
                .show()
        alert?.getTitle()?.setTextColor(Color.YELLOW)
        alert?.getText()?.setTextColor(Color.RED)
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

    private fun showAlertWithCustomAnimations() {
        Alerter.create(this@KotlinDemoActivity)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .setEnterAnimation(R.anim.alerter_slide_in_from_left)
                .setExitAnimation(R.anim.alerter_slide_out_to_right)
                .show()
    }

    private fun showAlertWithButtons() {
        Alerter.create(this@KotlinDemoActivity)
                .setTitle(R.string.title_activity_example)
                .setText("Alert text...")
                .addButton("Okay", R.style.AlertButton, View.OnClickListener {
                    Toast.makeText(this@KotlinDemoActivity, "Okay Clicked", Toast.LENGTH_LONG).show()
                })
                .addButton("No", R.style.AlertButton, View.OnClickListener {
                    Toast.makeText(this@KotlinDemoActivity, "No Clicked", Toast.LENGTH_LONG).show()
                })
                .show()
    }

    private fun showAlertSound() {
        Alerter.create(this@KotlinDemoActivity)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .setBackgroundColorRes(R.color.colorAccent)
                .setSound(Uri.parse("android.resource://$packageName/raw/ringtone"))
                .show()
    }

    private fun showAlertWithCustomLayout() {
        Alerter.create(this@KotlinDemoActivity, R.layout.custom_layout)
                .setBackgroundColorRes(R.color.colorAccent)
                .also { alerter ->
                    val tvCustomView = alerter.getLayoutContainer()?.tvCustomLayout
                    tvCustomView?.setText(R.string.with_custom_layout)
                }
                .show()
    }

    private fun showAlertFromCenter() {
        Alerter.create(this@KotlinDemoActivity)
                .setTitle(R.string.title_activity_example)
                .setText("Alert text...")
                .setLayoutGravity(Gravity.CENTER)
                .show()
    }


    private fun showAlertFromBottom() {
        Alerter.create(this@KotlinDemoActivity)
                .setTitle(R.string.title_activity_example)
                .setText("Alert text...")
                .setLayoutGravity(Gravity.BOTTOM)
                .show()
    }

    private fun showAlertWithRightIcon() {
        Alerter.create(this@KotlinDemoActivity)
                .setText("Alert text...")
                .setIcon(R.drawable.alerter_ic_mail_outline)
                .setIconColorFilter(0) // Optional - Removes white tint
                .setIconSize(R.dimen.custom_icon_size) // Optional - default is 38dp
                .setRightIcon(R.drawable.alerter_ic_face)
                .showRightIcon(true)
                .setRightIconSize(R.dimen.custom_icon_size)  // Optional - default is 38dp
                .show()
    }

    private fun showAlertWithOnlyRightIcon() {
        Alerter.create(this@KotlinDemoActivity)
                .setText("Alert text...")
                .showIcon(false)
                .setRightIcon(R.drawable.alerter_ic_face)
                .showRightIcon(true)
                .show()
    }

    private fun showAlertRightIconOnTop() {
        Alerter.create(this)
                .setText("The alert scales to accommodate larger bodies of text." +
                        "The alert scales to accommodate larger bodies of text. " +
                        "The alert scales to accommodate larger bodies of text.")
                .showIcon(false)
                .setRightIcon(R.drawable.alerter_ic_face)
                .setRightIconPosition(Gravity.TOP) // Optional - default is center
                .showRightIcon(true)
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

        btnAlertWithCustomColor.setOnClickListener {
            showAlertWithCustomColor()
        }

        btnAlertSwipeToDismissEnabled.setOnClickListener {
            showAlertSwipeToDismissEnabled()
        }

        btnAlertCustomAnimations.setOnClickListener {
            showAlertWithCustomAnimations()
        }

        btnAlertWithButtons.setOnClickListener {
            showAlertWithButtons()
        }

        btnAlertSound.setOnClickListener {
            showAlertSound()
        }

        btnAlertWithCustomLayout.setOnClickListener {
            showAlertWithCustomLayout()
        }

        btnCenterAlert.setOnClickListener {
            showAlertFromCenter()
        }

        btnBottomAlert.setOnClickListener {
            showAlertFromBottom()
        }

        btnShowAlertWithRightIcon.setOnClickListener {
            showAlertWithRightIcon()
        }

        btnShowAlertWithOnlyRightIcon.setOnClickListener {
            showAlertWithOnlyRightIcon()
        }

        btnRightIconOnTop.setOnClickListener {
            showAlertRightIconOnTop()
        }

        btnBottomSheetModal.setOnClickListener {
            CustomBottomSheetDialogFragment().apply {
                show(supportFragmentManager, CustomBottomSheetDialogFragment::class.java.canonicalName)
            }
        }
    }
}
