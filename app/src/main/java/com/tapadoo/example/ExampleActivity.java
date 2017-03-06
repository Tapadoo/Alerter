package com.tapadoo.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.tapadoo.alerter.Alerter;
import com.tapadoo.alerter.OnHideAlertListener;
import com.tapadoo.alerter.OnShowAlertListener;

public class ExampleActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.btnAlertDefault).setOnClickListener(this);
        findViewById(R.id.btnAlertColoured).setOnClickListener(this);
        findViewById(R.id.btnAlertCustomIcon).setOnClickListener(this);
        findViewById(R.id.btnAlertTextOnly).setOnClickListener(this);
        findViewById(R.id.btnAlertOnClick).setOnClickListener(this);
        findViewById(R.id.btnAlertVerbose).setOnClickListener(this);
        findViewById(R.id.btnAlertCallback).setOnClickListener(this);
        findViewById(R.id.btnAlertInfiniteDuration).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAlertColoured: {
                showAlertColoured();
                break;
            }
            case R.id.btnAlertCustomIcon: {
                showAlertWithIcon();
                break;
            }
            case R.id.btnAlertTextOnly: {
                showAlertTextOnly();
                break;
            }
            case R.id.btnAlertOnClick: {
                showAlertWithOnClick();
                break;
            }
            case R.id.btnAlertVerbose: {
                showAlertVerbose();
                break;
            }
            case R.id.btnAlertCallback: {
                showAlertCallbacks();
                break;
            }
            case R.id.btnAlertInfiniteDuration: {
                showAlertInfiniteDuration();
            }
            default: {
                showAlertDefault();
            }
        }
    }

    private void showAlertDefault() {
        Alerter.create(ExampleActivity.this)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .enableInfiniteDuration(true)
                .show();
    }

    private void showAlertColoured() {
        Alerter.create(ExampleActivity.this)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .setBackgroundColor(R.color.colorAccent)
                .show();
    }

    private void showAlertWithIcon() {
        Alerter.create(ExampleActivity.this)
                .setText("Alert text...")
                .setIcon(R.drawable.alerter_ic_face)
                .show();
    }

    private void showAlertTextOnly() {
        Alerter.create(ExampleActivity.this)
                .setText("Alert text...")
                .show();
    }

    private void showAlertWithOnClick() {
        Alerter.create(ExampleActivity.this)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .setDuration(10000)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(ExampleActivity.this, "OnClick Called", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    private void showAlertVerbose() {
        Alerter.create(ExampleActivity.this)
                .setTitle("Alert Title")
                .setText("The alert scales to accommodate larger bodies of text. " +
                         "The alert scales to accommodate larger bodies of text. " +
                         "The alert scales to accommodate larger bodies of text.")
                .show();
    }

    private void showAlertCallbacks(){
        Alerter.create(ExampleActivity.this)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .setDuration(10000)
                .setOnShowListener(new OnShowAlertListener() {
                    @Override
                    public void onShow() {
                        Toast.makeText(ExampleActivity.this, "Show Alert", Toast.LENGTH_LONG).show();
                    }
                })
                .setOnHideListener(new OnHideAlertListener() {
                    @Override
                    public void onHide() {
                        Toast.makeText(ExampleActivity.this, "Hide Alert", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    private void showAlertInfiniteDuration() {
        Alerter.create(ExampleActivity.this)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .enableInfiniteDuration(true)
                .show();
    }
}
