package com.tapadoo.alerter;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Mock Activity for Testing
 *
 * @author Kevin Murphy
 * @since 5/10/2016
 */
public class MockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);
    }
}
