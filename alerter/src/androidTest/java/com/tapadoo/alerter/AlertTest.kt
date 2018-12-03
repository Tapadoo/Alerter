package com.tapadoo.alerter

import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import androidx.core.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
    @JvmField
    internal val activityRule = ActivityTestRule<MockActivity>(MockActivity::class.java)

    private lateinit var alert: Alert

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
        Assert.assertNotNull(alert.findViewById(R.id.llAlertBackground))
        Assert.assertNotNull(alert.findViewById(R.id.tvTitle))
        Assert.assertNotNull(alert.findViewById(R.id.tvText))
        Assert.assertNotNull(alert.findViewById(R.id.ivIcon))
    }

    @Test
    fun testTitleString() {
        //Strings
        alert.setTitle(HELLO)

        alert.findViewById<TextView>(R.id.tvTitle)?.let {
            Assert.assertTrue(it.visibility == View.VISIBLE)
            Assert.assertNotNull(it.text)
            Assert.assertEquals(it.text, HELLO)
            Assert.assertNotSame(it.text, HI)
        }
    }

    @Test
    fun testTitleStringRes() {
        //String Resources
        alert.setTitle(R.string.lib_name)

        alert.findViewById<TextView>(R.id.tvTitle)?.let {
            Assert.assertTrue(it.visibility == View.VISIBLE)

            Assert.assertNotNull(it.text)
            Assert.assertEquals(it.text, ALERTER)
            Assert.assertNotSame(it.text, HI)
        }

    }

    @Test
    fun testTextString() {
        //Strings
        alert.setText(HELLO)

        alert.findViewById<TextView>(R.id.tvText)?.let {
            Assert.assertTrue(it.visibility == View.VISIBLE)

            Assert.assertNotNull(it.text)
            Assert.assertEquals(it.text, HELLO)
            Assert.assertNotSame(it.text, HI)
        }
    }

    @Test
    fun testTextStringRes() {
        //Strings Resources
        alert.setText(R.string.lib_name)

        alert.findViewById<TextView>(R.id.tvText)?.let {
            Assert.assertTrue(it.visibility == View.VISIBLE)

            Assert.assertNotNull(it.text)
            Assert.assertEquals(it.text, ALERTER)
            Assert.assertNotSame(it.text, HI)
        }
    }

    @Test
    fun testBackgroundColour() {
        alert.setAlertBackgroundColor(ContextCompat.getColor(activityRule.activity, android.R.color.darker_gray))

        alert.findViewById<ViewGroup>(R.id.llAlertBackground)?.let {
            Assert.assertNotNull(it.background)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                Assert.assertEquals((it.background as ColorDrawable).color, ContextCompat.getColor(activityRule.activity, android.R.color.darker_gray))
            }
        }
    }

    @Test
    fun testIcon() {
        //Compare same Drawables
        alert.setIcon(android.R.drawable.sym_def_app_icon)
        Assert.assertNotNull(alert.findViewById<ImageView>(R.id.ivIcon)?.drawable)
    }

    @Test
    fun testOnClickListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            return
        }

        alert.findViewById<ViewGroup>(R.id.llAlertBackground)?.let {
            //Check default onClickListener
            Assert.assertTrue(it.hasOnClickListeners())

            //Check nullifying
            alert.setOnClickListener(null)
            Assert.assertFalse(it.hasOnClickListeners())
        }
    }

    companion object {
        /**
         * Test Strings
         */
        private val HELLO = "Hello"
        private val HI = "Hi"
        private val ALERTER = "Alerter"
    }

}