package com.tapadoo.alerter

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.view.ViewGroup

import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Alerter Test Case Class
 *
 * @author Kevin Murphy, Tapadoo
 * @since 05/10/2016
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class AlerterTest {

    //Rule which sets the Activity to be used
    @Rule
    internal val activityRule = ActivityTestRule(MockActivity::class.java)

    @Test
    fun testConstruction() {
        val alerter = Alerter.create(activityRule.activity)
        Assert.assertNotNull(alerter)
    }

    @Test
    fun testShow() {
        //Get the Activity's initial number of Child Views
        val childCount = (activityRule.activity.findViewById<View>(android.R.id.content).rootView as ViewGroup).childCount

        //Instantiate Alerter
        val alerter = Alerter.create(activityRule.activity)

        //Show it to the User
        alerter.show()

        //Wait to ensure Alert layout is fully added
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            Log.e(javaClass.simpleName, Log.getStackTraceString(e))
        }

        //Get the new child count - Should be initial + 1
        val childCountAfter = (activityRule.activity.findViewById<View>(android.R.id.content).rootView as ViewGroup).childCount

        //Check to ensure we have an additional child added
        Assert.assertNotSame(childCount, childCountAfter)
        Assert.assertSame(childCount + 1, childCountAfter)
    }

    @Test
    fun testBuilderStrings() {
        val alert = Alerter.create(activityRule.activity)
                .setTitle("Hello")
                .setText("Hi")
                .show()

        Assert.assertSame(alert?.title?.text, "Hello")
        Assert.assertSame(alert?.text?.text, "Hi")
    }

    @Test
    fun testBuilderStringsRes() {
        val alert = Alerter.create(activityRule.activity)
                .setTitle(R.string.lib_name)
                .setText(R.string.msg_no_alert_showing)
                .show()

        Assert.assertSame(alert?.title?.text, activityRule.activity.getString(R.string.lib_name))
        Assert.assertSame(alert?.text?.text, activityRule.activity.getString(R.string.msg_no_alert_showing))
    }

    @Test
    fun testBuilderIcon() {
        val alert = Alerter.create(activityRule.activity)
                .setIcon(android.R.drawable.sym_def_app_icon)
                .show()

        Assert.assertNotNull(alert?.icon?.drawable)
        Assert.assertNotSame(alert?.icon?.drawable?.constantState, ContextCompat.getDrawable(activityRule.activity, android.R.drawable.sym_action_call)!!.constantState)
    }

    @Test
    fun testBuilderBackground() {
        val alert = Alerter.create(activityRule.activity)
                .setBackgroundColorRes(android.R.color.darker_gray)
                .show()

        Assert.assertNotNull(alert?.alertBackground?.getBackground())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Assert.assertEquals((alert?.alertBackground?.getBackground() as ColorDrawable).color.toLong(), ContextCompat.getColor(activityRule.activity, android.R.color.darker_gray).toLong())
        }
    }

    @Test
    fun testBuilderOnClickListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            return
        }

        //Test default hide listener
        val alert1 = Alerter.create(activityRule.activity).show()

        Assert.assertTrue(alert1?.alertBackground?.hasOnClickListeners() ?: false)

        //Test nullifying listener
        val alert2 = Alerter.create(activityRule.activity).setOnClickListener(null!!).show()

        Assert.assertFalse(alert2?.alertBackground?.hasOnClickListeners() ?: false)

        //Test setting listener
        val alert3 = Alerter.create(activityRule.activity).setOnClickListener(View.OnClickListener {
            //Ignore
        }).show()

        Assert.assertTrue(alert3!!.alertBackground?.hasOnClickListeners() ?: false)
    }
}
