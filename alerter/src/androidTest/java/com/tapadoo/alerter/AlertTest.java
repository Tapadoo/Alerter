package com.tapadoo.alerter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.tapadoo.android.R;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Alert Test Case Class
 *
 * @author Kevin Murphy, Tapadoo
 * @since 04/10/2016
 **/
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AlertTest {

    //Rule which sets the Activity to be used
    @ClassRule
    public static final ActivityTestRule<MockActivity> activityRule = new ActivityTestRule<>(MockActivity.class, true, false);

    /**
     * Test Strings
     */
    private static final String HELLO = "Hello";
    private static final String HI = "Hi";
    private static final String ALERTER = "Alerter";

    @SuppressLint("StaticFieldLeak")
    private static Activity mockActivity;

    @SuppressLint("StaticFieldLeak")
    private static Alert alert;

    @BeforeClass // Called once before all tests
    public static void beforeClass() {
        //Start our activity
        activityRule.launchActivity(null);

        //Store a reference to it
        mockActivity = activityRule.getActivity();
    }

    @Before // Called before each test
    public void setUp() throws Exception {
        alert = new Alert(mockActivity);
    }

    @Test
    public void testConstruction() {
        final Alert alert = new Alert(mockActivity);
        Assert.assertNotNull(alert);
    }

    @Test
    public void testLayoutElements() {
        final Alert alert = new Alert(mockActivity);

        //Ensure all elements are present
        Assert.assertNotNull(alert.getAlertBackground());
        Assert.assertNotNull(alert.getTitle());
        Assert.assertNotNull(alert.getText());
        Assert.assertNotNull(alert.getIcon());
    }

    @Test
    public void testTitleString() {
        //Strings
        alert.setTitle(HELLO);
        Assert.assertTrue(alert.getTitle().getVisibility() == View.VISIBLE);

        Assert.assertNotNull(alert.getTitle().getText());
        Assert.assertEquals(HELLO, alert.getTitle().getText());
        Assert.assertNotSame(HI, alert.getTitle().getText());
    }

    @Test
    public void testTitleStringRes() {
        //String Resources
        alert.setTitle(R.string.lib_name);
        Assert.assertTrue(alert.getTitle().getVisibility() == View.VISIBLE);

        Assert.assertNotNull(alert.getTitle().getText());
        Assert.assertEquals(ALERTER, alert.getTitle().getText());
        Assert.assertNotSame(HI, alert.getTitle().getText());
    }

    @Test
    public void testTextString() {
        //Strings
        alert.setText(HELLO);
        Assert.assertTrue(alert.getText().getVisibility() == View.VISIBLE);

        Assert.assertNotNull(alert.getText().getText());
        Assert.assertEquals(HELLO, alert.getText().getText());
        Assert.assertNotSame(HI, alert.getText().getText());
    }

    @Test
    public void testTextStringRes() {
        //Strings Resources
        alert.setText(R.string.lib_name);
        Assert.assertTrue(alert.getText().getVisibility() == View.VISIBLE);

        Assert.assertNotNull(alert.getText().getText());
        Assert.assertEquals(ALERTER, alert.getText().getText());
        Assert.assertNotSame(HI, alert.getText().getText());
    }

    @Test
    public void testBackgroundColour() {
        alert.setAlertBackgroundColor(ContextCompat.getColor(mockActivity, android.R.color.darker_gray));

        Assert.assertNotNull(alert.getAlertBackground().getBackground());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            Assert.assertEquals(((ColorDrawable) alert.getAlertBackground().getBackground()).getColor(), ContextCompat.getColor(mockActivity, android.R.color.darker_gray));
        }
    }

    @Test
    public void testIcon() {
        //Compare same Drawables
        alert.setIcon(android.R.drawable.sym_def_app_icon);
        Assert.assertEquals(alert.getIcon().getDrawable().getConstantState(), ContextCompat.getDrawable(mockActivity, android.R.drawable.sym_def_app_icon).getConstantState());

        //Compare Different
        alert.setIcon(android.R.drawable.sym_def_app_icon);
        Assert.assertNotSame(alert.getIcon().getDrawable().getConstantState(), ContextCompat.getDrawable(mockActivity, android.R.drawable.sym_action_call).getConstantState());
    }

    @Test
    public void testOnClickListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            return;
        }

        //Check default onClickListener
        Assert.assertTrue(alert.getAlertBackground().hasOnClickListeners());

        //Check nullifying
        alert.setOnClickListener(null);
        Assert.assertFalse(alert.getAlertBackground().hasOnClickListeners());
    }

}