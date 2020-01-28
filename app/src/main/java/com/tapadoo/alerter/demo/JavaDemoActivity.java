package com.tapadoo.alerter.demo;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.tapadoo.alerter.Alerter;
import com.tapadoo.alerter.OnHideAlertListener;
import com.tapadoo.alerter.OnShowAlertListener;

/**
 * Java Demo Activity to ensure backwards compatibility
 *
 * @author Kevin Murphy
 * @since 5/06/2018
 */
public class JavaDemoActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.tapadoo.alerter.demo.R.layout.activity_demo);

        Toolbar toolbar = findViewById(com.tapadoo.alerter.demo.R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(com.tapadoo.alerter.demo.R.id.btnAlertDefault).setOnClickListener(this);
        findViewById(com.tapadoo.alerter.demo.R.id.btnAlertColoured).setOnClickListener(this);
        findViewById(com.tapadoo.alerter.demo.R.id.btnAlertCustomIcon).setOnClickListener(this);
        findViewById(com.tapadoo.alerter.demo.R.id.btnAlertTextOnly).setOnClickListener(this);
        findViewById(com.tapadoo.alerter.demo.R.id.btnAlertOnClick).setOnClickListener(this);
        findViewById(com.tapadoo.alerter.demo.R.id.btnAlertVerbose).setOnClickListener(this);
        findViewById(com.tapadoo.alerter.demo.R.id.btnAlertCallback).setOnClickListener(this);
        findViewById(com.tapadoo.alerter.demo.R.id.btnAlertInfiniteDuration).setOnClickListener(this);
        findViewById(com.tapadoo.alerter.demo.R.id.btnAlertWithProgress).setOnClickListener(this);
        findViewById(com.tapadoo.alerter.demo.R.id.btnAlertWithCustomFont).setOnClickListener(this);
        findViewById(com.tapadoo.alerter.demo.R.id.btnAlertSwipeToDismissEnabled).setOnClickListener(this);
        findViewById(com.tapadoo.alerter.demo.R.id.btnAlertSound).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        getWindow().setBackgroundDrawableResource(android.R.color.white);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == com.tapadoo.alerter.demo.R.id.btnAlertColoured) {
            showAlertColoured();
        } else if (i == com.tapadoo.alerter.demo.R.id.btnAlertCustomIcon) {
            showAlertWithIcon();
        } else if (i == com.tapadoo.alerter.demo.R.id.btnAlertTextOnly) {
            showAlertTextOnly();
        } else if (i == com.tapadoo.alerter.demo.R.id.btnAlertOnClick) {
            showAlertWithOnClick();
        } else if (i == com.tapadoo.alerter.demo.R.id.btnAlertVerbose) {
            showAlertVerbose();
        } else if (i == com.tapadoo.alerter.demo.R.id.btnAlertCallback) {
            showAlertCallbacks();
        } else if (i == com.tapadoo.alerter.demo.R.id.btnAlertInfiniteDuration) {
            showAlertInfiniteDuration();
        } else if (i == com.tapadoo.alerter.demo.R.id.btnAlertWithProgress) {
            showAlertWithProgress();
        } else if (i == com.tapadoo.alerter.demo.R.id.btnAlertWithCustomFont) {
            showAlertWithCustomFont();
        } else if (i == com.tapadoo.alerter.demo.R.id.btnAlertSwipeToDismissEnabled) {
            showAlertSwipeToDismissEnabled();
        } else if (i == com.tapadoo.alerter.demo.R.id.btnAlertSound){
            showAlertSound();
        }else if (i == com.tapadoo.alerter.demo.R.id.btnCenterAlert){
            showAlertFromCenter();
        }else if (i == com.tapadoo.alerter.demo.R.id.btnBottomAlert){
            showAlertFromBottom();
        } else {
            showAlertDefault();
        }
    }

    private void showAlertDefault() {
        Alerter.create(JavaDemoActivity.this)
                .setTitle(com.tapadoo.alerter.demo.R.string.title_activity_example)
                .setText("Alert text...")
                .show();
    }

    private void showAlertColoured() {
        Alerter.create(JavaDemoActivity.this)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .setBackgroundColorRes(com.tapadoo.alerter.demo.R.color.colorAccent)
                .show();
    }

    private void showAlertWithIcon() {
        Alerter.create(JavaDemoActivity.this)
                .setText("Alert text...")
                .setIcon(com.tapadoo.alerter.demo.R.drawable.alerter_ic_mail_outline)
                .setIconColorFilter(0) // Optional - Removes white tint
                .setIconSize(R.dimen.custom_icon_size) // Optional - default is 38dp
                .show();
    }

    private void showAlertTextOnly() {
        Alerter.create(JavaDemoActivity.this)
                .setText("Alert text...")
                .show();
    }

    private void showAlertWithOnClick() {
        Alerter.create(JavaDemoActivity.this)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .setDuration(10000)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(JavaDemoActivity.this, "OnClick Called", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    private void showAlertVerbose() {
        Alerter.create(JavaDemoActivity.this)
                .setTitle("Alert Title")
                .setText("The alert scales to accommodate larger bodies of text. " +
                         "The alert scales to accommodate larger bodies of text. " +
                         "The alert scales to accommodate larger bodies of text.")
                .show();
    }

    private void showAlertCallbacks(){
        Alerter.create(JavaDemoActivity.this)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .setDuration(10000)
                .setOnShowListener(new OnShowAlertListener() {
                    @Override
                    public void onShow() {
                        Toast.makeText(JavaDemoActivity.this, "Show Alert", Toast.LENGTH_LONG).show();
                    }
                })
                .setOnHideListener(new OnHideAlertListener() {
                    @Override
                    public void onHide() {
                        Toast.makeText(JavaDemoActivity.this, "Hide Alert", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    private void showAlertInfiniteDuration() {
        Alerter.create(JavaDemoActivity.this)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .enableInfiniteDuration(true)
                .show();
    }

    private void showAlertWithProgress() {
        Alerter.create(JavaDemoActivity.this)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .enableProgress(true)
                .setProgressColorRes(com.tapadoo.alerter.demo.R.color.colorPrimary)
                .show();
    }

    private void showAlertWithCustomFont() {
        Alerter.create(JavaDemoActivity.this)
                .setTitle("Alert Title")
                .setTitleAppearance(com.tapadoo.alerter.demo.R.style.AlertTextAppearance_Title)
                .setTitleTypeface(Typeface.createFromAsset(getAssets(), "Pacifico-Regular.ttf"))
                .setText("Alert text...")
                .setTextAppearance(com.tapadoo.alerter.demo.R.style.AlertTextAppearance_Text)
                .setTextTypeface(Typeface.createFromAsset(getAssets(), "ScopeOne-Regular.ttf"))
                .show();
    }

    private void showAlertSwipeToDismissEnabled() {
        Alerter.create(JavaDemoActivity.this)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .enableSwipeToDismiss()
                .setOnHideListener(new OnHideAlertListener() {
                    @Override
                    public void onHide() {
                        Toast.makeText(JavaDemoActivity.this, "Hide Alert", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    private void showAlertWithCustomAnimations() {
        Alerter.create(JavaDemoActivity.this)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .setEnterAnimation(com.tapadoo.alerter.demo.R.anim.alerter_slide_in_from_left)
                .setExitAnimation(com.tapadoo.alerter.demo.R.anim.alerter_slide_out_to_right)
                .show();
    }

    private void showAlertWithButtons() {
        Alerter.create(JavaDemoActivity.this)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .addButton("Okay", com.tapadoo.alerter.demo.R.style.AlertButton, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(JavaDemoActivity.this, "Okay Clicked", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void showAlertSound() {
        Alerter.create(JavaDemoActivity.this)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .setBackgroundColorRes(R.color.colorAccent)
                .enableSound(true)
                .show();
    }

    private void showAlertFromCenter() {
        Alerter.create(JavaDemoActivity.this)
                .setTitle(com.tapadoo.alerter.demo.R.string.title_activity_example)
                .setText("Alert text...")
                .setLayoutGravity(Gravity.CENTER)
                .show();
    }

    private void showAlertFromBottom() {
        Alerter.create(JavaDemoActivity.this)
                .setTitle(com.tapadoo.alerter.demo.R.string.title_activity_example)
                .setText("Alert text...")
                .setLayoutGravity(Gravity.BOTTOM)
                .show();
    }
}
