package com.tapadoo.alerter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.tapadoo.android.R;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Alerter Test Case Class
 *
 * @author Kevin Murphy, Tapadoo
 * @since 05/10/2016
 **/
@RunWith(AndroidJUnit4.class)
public class AlerterTest {

    //Rule which sets the Activity to be used
    @ClassRule
    public static final ActivityTestRule<MockActivity> activityRule = new ActivityTestRule<>(MockActivity.class, true, false);

    @SuppressLint("StaticFieldLeak")
    private static Activity mockActivity;

    @BeforeClass // Called once before all tests
    public static void beforeClass() {
        //Start our activity
        activityRule.launchActivity(null);

        //Store a reference to it
        mockActivity = activityRule.getActivity();
    }

    @Test
    public void testConstruction() {
        final Alerter alerter = Alerter.create(mockActivity);
        Assert.assertNotNull(alerter);
    }

    @Test
    public void testShow() {
        //Get the Activity's initial number of Child Views
        final int childCount = ((ViewGroup) mockActivity.findViewById(android.R.id.content).getRootView()).getChildCount();

        //Instantiate Alerter
        final Alerter alerter = Alerter.create(mockActivity);

        //Show it to the User
        alerter.show();

        //Wait to ensure Alert layout is fully added
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Log.e(getClass().getSimpleName(), Log.getStackTraceString(e));
        }

        //Get the new child count - Should be initial + 1
        final int childCountAfter = ((ViewGroup) mockActivity.findViewById(android.R.id.content).getRootView()).getChildCount();

        //Check to ensure we have an additional child added
        Assert.assertNotSame(childCount, childCountAfter);
        Assert.assertSame(childCount + 1, childCountAfter);
    }

    @Test
    public void testBuilderStrings() {
        final Alert alert = Alerter.create(mockActivity)
                .setTitle("Hello")
                .setText("Hi")
                .show();

        Assert.assertSame(alert.getTitle().getText(), "Hello");
        Assert.assertSame(alert.getText().getText(), "Hi");
    }

    @Test
    public void testBuilderStringsRes() {
        final Alert alert = Alerter.create(mockActivity)
                .setTitle(R.string.lib_name)
                .setText(R.string.msg_no_alert_showing)
                .show();

        Assert.assertSame(alert.getTitle().getText(), mockActivity.getString(R.string.lib_name));
        Assert.assertSame(alert.getText().getText(), mockActivity.getString(R.string.msg_no_alert_showing));
    }

    @Test
    public void testBuilderIcon() {
        final Alert alert = Alerter.create(mockActivity)
                .setIcon(android.R.drawable.sym_def_app_icon)
                .show();

        Assert.assertEquals(alert.getIcon().getDrawable().getConstantState(), ContextCompat.getDrawable(mockActivity, android.R.drawable.sym_def_app_icon).getConstantState());
        Assert.assertNotSame(alert.getIcon().getDrawable().getConstantState(), ContextCompat.getDrawable(mockActivity, android.R.drawable.sym_action_call).getConstantState());
    }

    @Test
    public void testBuilderBackground() {
        final Alert alert = Alerter.create(mockActivity)
                .setBackgroundColor(android.R.color.darker_gray)
                .show();

        Assert.assertNotNull(alert.getAlertBackground().getBackground());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Assert.assertEquals(((ColorDrawable) alert.getAlertBackground().getBackground()).getColor(), ContextCompat.getColor(mockActivity, android.R.color.darker_gray));
        }
    }

    @Test
    public void testBuilderOnClickListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            return;
        }

        //Test default hide listener
        final Alert alert1 = Alerter.create(mockActivity).show();

        Assert.assertTrue(alert1.getAlertBackground().hasOnClickListeners());

        //Test nullifying listener
        final Alert alert2 = Alerter.create(mockActivity).setOnClickListener(null).show();

        Assert.assertFalse(alert2.getAlertBackground().hasOnClickListeners());

        //Test setting listener
        final Alert alert3 = Alerter.create(mockActivity).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        //Ignore
                    }
                })
                .show();

        Assert.assertTrue(alert3.getAlertBackground().hasOnClickListeners());
    }
}
