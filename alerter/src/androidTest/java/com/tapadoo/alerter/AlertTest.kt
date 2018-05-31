package com.tapadoo.alerter

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v4.content.ContextCompat
import android.view.View
import junit.framework.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Alert Test Case Class
 *
 * @author Kevin Murphy, Tapadoo
 * @since 04/10/2016
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class AlertTest {

    //Rule which sets the Activity to be used
    @Rule
    internal val activityRule = ActivityTestRule<MockActivity>(MockActivity::class.java)

    @Before // Called before each test
    @Throws(Exception::class)
    fun setUp() {
        alert = Alert(activityRule.activity)
    }

    @Test
    fun testConstruction() {
        val alert = Alert(activityRule.activity)
        Assert.assertNotNull(alert)
    }

    @Test
    fun testLayoutElements() {
        val alert = Alert(activityRule.activity)

        //Ensure all elements are present
        Assert.assertNotNull(alert.alertBackground)
        Assert.assertNotNull(alert.title)
        Assert.assertNotNull(alert.text)
        Assert.assertNotNull(alert.icon)
    }

    @Test
    fun testTitleString() {
        //Strings
        alert!!.setTitle(HELLO)
        Assert.assertTrue(alert!!.title?.visibility === View.VISIBLE)

        Assert.assertNotNull(alert!!.title?.text)
        Assert.assertEquals(HELLO, alert!!.title?.text)
        Assert.assertNotSame(HI, alert!!.title?.text)
    }

    @Test
    fun testTitleStringRes() {
        //String Resources
        alert!!.setTitle(R.string.lib_name)
        Assert.assertTrue(alert!!.title?.visibility === View.VISIBLE)

        Assert.assertNotNull(alert!!.title?.text)
        Assert.assertEquals(ALERTER, alert!!.title?.text)
        Assert.assertNotSame(HI, alert!!.title?.text)
    }

    @Test
    fun testTextString() {
        //Strings
        alert!!.setText(HELLO)
        Assert.assertTrue(alert!!.text?.visibility === View.VISIBLE)

        Assert.assertNotNull(alert!!.text?.text)
        Assert.assertEquals(HELLO, alert!!.text?.text)
        Assert.assertNotSame(HI, alert!!.text?.text)
    }

    @Test
    fun testTextStringRes() {
        //Strings Resources
        alert!!.setText(R.string.lib_name)
        Assert.assertTrue(alert!!.text?.visibility === View.VISIBLE)

        Assert.assertNotNull(alert!!.text?.text)
        Assert.assertEquals(ALERTER, alert!!.text?.text)
        Assert.assertNotSame(HI, alert!!.text?.text)
    }

    @Test
    fun testBackgroundColour() {
        alert!!.setAlertBackgroundColor(ContextCompat.getColor(activityRule.activity, android.R.color.darker_gray))

        Assert.assertNotNull(alert!!.alertBackground?.background)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            Assert.assertEquals((alert!!.alertBackground?.background as ColorDrawable).color, ContextCompat.getColor(activityRule.activity, android.R.color.darker_gray))
        }
    }

    @Test
    fun testIcon() {
        //Compare same Drawables
        alert!!.setIcon(android.R.drawable.sym_def_app_icon)
        Assert.assertNotNull(alert!!.icon?.drawable)
    }

    @Test
    fun testOnClickListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            return
        }

        //Check default onClickListener
        Assert.assertTrue(alert!!.alertBackground?.hasOnClickListeners() ?: false)

        //Check nullifying
        alert!!.setOnClickListener(null)
        Assert.assertFalse(alert!!.alertBackground?.hasOnClickListeners() ?: false)
    }

    companion object {

        /**
         * Test Strings
         */
        private val HELLO = "Hello"
        private val HI = "Hi"
        private val ALERTER = "Alerter"

        @SuppressLint("StaticFieldLeak")
        private var alert: Alert? = null
    }

}