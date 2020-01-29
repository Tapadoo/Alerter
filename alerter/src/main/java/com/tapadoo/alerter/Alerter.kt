package com.tapadoo.alerter

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import java.lang.ref.WeakReference

/**
 * Alert helper class. Will attach a temporary layout to the current activity's content, on top of
 * all other views. It should appear under the status bar.
 *
 * @author Kevin Murphy
 * @since 03/11/2015.
 */
class Alerter private constructor() {

    /**
     * Sets the Alert
     *
     * @param alert The Alert to be references and maintained
     */
    private var alert: Alert? = null

    /**
     * Get the enclosing Decor View
     *
     * @return The Decor View of the Activity the Alerter was called from
     */
    private val activityDecorView: ViewGroup?
        get() {
            var decorView: ViewGroup? = null

            activityWeakReference?.get()?.let {
                decorView = it.window.decorView as ViewGroup
            }

            return decorView
        }

    /**
     * Shows the Alert, after it's built
     *
     * @return An Alert object check can be altered or hidden
     */
    fun show(): Alert? {
        //This will get the Activity Window's DecorView
        activityWeakReference?.get()?.let {
            it.runOnUiThread {
                //Add the new Alert to the View Hierarchy
                activityDecorView?.addView(alert)
            }
        }

        return alert
    }

    /**
     * Sets the title of the Alert
     *
     * @param titleId Title String Resource
     * @return This Alerter
     */
    fun setTitle(@StringRes titleId: Int): Alerter {
        alert?.setTitle(titleId)

        return this
    }

    /**
     * Set Title of the Alert
     *
     * @param title Title as a CharSequence
     * @return This Alerter
     */
    fun setTitle(title: CharSequence): Alerter {
        alert?.setTitle(title)

        return this
    }

    /**
     * Set the Title's Typeface
     *
     * @param typeface Typeface to use
     * @return This Alerter
     */
    fun setTitleTypeface(typeface: Typeface): Alerter {
        alert?.setTitleTypeface(typeface)

        return this
    }

    /**
     * Set the Title's text appearance
     *
     * @param textAppearance The style resource id
     * @return This Alerter
     */
    fun setTitleAppearance(@StyleRes textAppearance: Int): Alerter {
        alert?.setTitleAppearance(textAppearance)

        return this
    }

    /**
     * Set Layout Gravity of the Alert
     *
     * @param layoutGravity of Alert
     * @return This Alerter
     */
    fun setLayoutGravity(layoutGravity: Int): Alerter {
        alert?.layoutGravity = layoutGravity

        return this
    }

    /**
     * Set Gravity of the Alert
     *
     * @param gravity Gravity of Alert
     * @return This Alerter
     */
    fun setContentGravity(gravity: Int): Alerter {
        alert?.contentGravity = gravity

        return this
    }

    /**
     * Sets the Alert Text
     *
     * @param textId Text String Resource
     * @return This Alerter
     */
    fun setText(@StringRes textId: Int): Alerter {
        alert?.setText(textId)

        return this
    }

    /**
     * Sets the Alert Text
     *
     * @param text CharSequence of Alert Text
     * @return This Alerter
     */
    fun setText(text: CharSequence): Alerter {
        alert?.setText(text)

        return this
    }

    /**
     * Set the Text's Typeface
     *
     * @param typeface Typeface to use
     * @return This Alerter
     */
    fun setTextTypeface(typeface: Typeface): Alerter {
        alert?.setTextTypeface(typeface)

        return this
    }

    /**
     * Set the Text's text appearance
     *
     * @param textAppearance The style resource id
     * @return This Alerter
     */
    fun setTextAppearance(@StyleRes textAppearance: Int): Alerter {
        alert?.setTextAppearance(textAppearance)

        return this
    }

    /**
     * Set the Alert's Background Colour
     *
     * @param colorInt Colour int value
     * @return This Alerter
     */
    fun setBackgroundColorInt(@ColorInt colorInt: Int): Alerter {
        alert?.setAlertBackgroundColor(colorInt)

        return this
    }

    /**
     * Set the Alert's Background Colour
     *
     * @param colorResId Colour Resource Id
     * @return This Alerter
     */
    fun setBackgroundColorRes(@ColorRes colorResId: Int): Alerter {
        activityWeakReference?.get()?.let {
            alert?.setAlertBackgroundColor(ContextCompat.getColor(it, colorResId))
        }

        return this
    }

    /**
     * Set the Alert's Background Drawable
     *
     * @param drawable Drawable
     * @return This Alerter
     */
    fun setBackgroundDrawable(drawable: Drawable): Alerter {
        alert?.setAlertBackgroundDrawable(drawable)

        return this
    }

    /**
     * Set the Alert's Background Drawable Resource
     *
     * @param drawableResId Drawable Resource Id
     * @return This Alerter
     */
    fun setBackgroundResource(@DrawableRes drawableResId: Int): Alerter {
        alert?.setAlertBackgroundResource(drawableResId)

        return this
    }

    /**
     * Set the Alert's Icon
     *
     * @param iconId The Drawable's Resource Idw
     * @return This Alerter
     */
    fun setIcon(@DrawableRes iconId: Int): Alerter {
        alert?.setIcon(iconId)

        return this
    }

    /**
     * Set the Alert's Icon
     *
     * @param bitmap The Bitmap object to use for the icon.
     * @return This Alerter
     */
    fun setIcon(bitmap: Bitmap): Alerter {
        alert?.setIcon(bitmap)

        return this
    }

    /**
     * Set the Alert's Icon
     *
     * @param drawable The Drawable to use for the icon.
     * @return This Alerter
     */
    fun setIcon(drawable: Drawable): Alerter {
        alert?.setIcon(drawable)

        return this
    }

    /**
     * Set the Alert's Icon size
     *
     * @param size Dimension int.
     * @return This Alerter
     */
    fun setIconSize(@DimenRes size: Int): Alerter {
        alert?.setIconSize(size)

        return this
    }

    /**
     * Set the Alert's Icon size
     *
     * @param size Icon size in pixel.
     * @return This Alerter
     */
    fun setIconPixelSize(@Px size: Int): Alerter {
        alert?.setIconPixelSize(size)

        return this
    }

    /**
     * Set the icon color for the Alert
     *
     * @param color Color int
     * @return This Alerter
     */
    fun setIconColorFilter(@ColorInt color: Int): Alerter {
        alert?.setIconColorFilter(color)

        return this
    }

    /**
     * Set the icon color for the Alert
     *
     * @param colorFilter ColorFilter
     * @return This Alerter
     */
    fun setIconColorFilter(colorFilter: ColorFilter): Alerter {
        alert?.setIconColorFilter(colorFilter)

        return this
    }

    /**
     * Set the icon color for the Alert
     *
     * @param color Color int
     * @param mode  PorterDuff.Mode
     * @return This Alerter
     */
    fun setIconColorFilter(@ColorInt color: Int, mode: PorterDuff.Mode): Alerter {
        alert?.setIconColorFilter(color, mode)

        return this
    }

    /**
     * Hide the Icon
     *
     * @return This Alerter
     */
    fun hideIcon(): Alerter {
        alert?.showIcon(false)

        return this
    }

    /**
     * Set the onClickListener for the Alert
     *
     * @param onClickListener The onClickListener for the Alert
     * @return This Alerter
     */
    fun setOnClickListener(onClickListener: View.OnClickListener): Alerter {
        alert?.setOnClickListener(onClickListener)

        return this
    }

    /**
     * Set the on screen duration of the alert
     *
     * @param milliseconds The duration in milliseconds
     * @return This Alerter
     */
    fun setDuration(milliseconds: Long): Alerter {
        alert?.duration = milliseconds

        return this
    }

    /**
     * Enable or Disable Icon Pulse Animations
     *
     * @param pulse True if the icon should pulse
     * @return This Alerter
     */
    fun enableIconPulse(pulse: Boolean): Alerter {
        alert?.pulseIcon(pulse)

        return this
    }

    /**
     * Set whether to show the icon in the alert or not
     *
     * @param showIcon True to show the icon, false otherwise
     * @return This Alerter
     */
    fun showIcon(showIcon: Boolean): Alerter {
        alert?.showIcon(showIcon)

        return this
    }

    /**
     * Enable or disable infinite duration of the alert
     *
     * @param infiniteDuration True if the duration of the alert is infinite
     * @return This Alerter
     */
    fun enableInfiniteDuration(infiniteDuration: Boolean): Alerter {
        alert?.setEnableInfiniteDuration(infiniteDuration)

        return this
    }

    /**
     * Sets the Alert Shown Listener
     *
     * @param listener OnShowAlertListener of Alert
     * @return This Alerter
     */
    fun setOnShowListener(listener: OnShowAlertListener): Alerter {
        alert?.setOnShowListener(listener)

        return this
    }

    /**
     * Sets the Alert Hidden Listener
     *
     * @param listener OnHideAlertListener of Alert
     * @return This Alerter
     */
    fun setOnHideListener(listener: OnHideAlertListener): Alerter {
        alert?.onHideListener = listener

        return this
    }

    /**
     * Enables swipe to dismiss
     *
     * @return This Alerter
     */
    fun enableSwipeToDismiss(): Alerter {
        alert?.enableSwipeToDismiss()

        return this
    }

    /**
     * Enable or Disable Vibration
     *
     * @param enable True to enable, False to disable
     * @return This Alerter
     */
    fun enableVibration(enable: Boolean): Alerter {
        alert?.setVibrationEnabled(enable)

        return this
    }

    /**
     * Enable or Disable Sound
     *
     * @param enable True to enable, False to disable
     * @return This Alerter
     */
    fun enableSound(enable: Boolean): Alerter {
        alert?.setSoundEnabled(enable)

        return this
    }

    /**
     * Disable touch events outside of the Alert
     *
     * @return This Alerter
     */
    fun disableOutsideTouch(): Alerter {
        alert?.disableOutsideTouch()

        return this
    }

    /**
     * Enable or disable progress bar
     *
     * @param enable True to enable, False to disable
     * @return This Alerter
     */
    fun enableProgress(enable: Boolean): Alerter {
        alert?.setEnableProgress(enable)

        return this
    }

    /**
     * Set the Progress bar color from a color resource
     *
     * @param color The color resource
     * @return This Alerter
     */
    fun setProgressColorRes(@ColorRes color: Int): Alerter {
        alert?.setProgressColorRes(color)

        return this
    }

    /**
     * Set the Progress bar color from a color resource
     *
     * @param color The color resource
     * @return This Alerter
     */
    fun setProgressColorInt(@ColorInt color: Int): Alerter {
        alert?.setProgressColorInt(color)

        return this
    }

    /**
     * Set if the Alert is dismissible or not
     *
     * @param dismissible true if it can be dismissed
     * @return This Alerter
     */
    fun setDismissable(dismissible: Boolean): Alerter {
        alert?.setDismissible(dismissible)

        return this
    }

    /**
     * Set a Custom Enter Animation
     *
     * @param animation The enter animation to play
     * @return This Alerter
     */
    fun setEnterAnimation(@AnimRes animation: Int): Alerter {
        alert?.enterAnimation = AnimationUtils.loadAnimation(alert?.context, animation)

        return this
    }

    /**
     * Set a Custom Exit Animation
     *
     * @param animation The exit animation to play
     * @return This Alerter
     */
    fun setExitAnimation(@AnimRes animation: Int): Alerter {
        alert?.exitAnimation = AnimationUtils.loadAnimation(alert?.context, animation)

        return this
    }

    /**
     * Show a button with the given text, and on click listener
     *
     * @param text The text to display on the button
     * @param onClick The on click listener
     */
    fun addButton(
            text: CharSequence, @StyleRes style: Int = R.style.AlertButton,
            onClick: View.OnClickListener
    ): Alerter {
        alert?.addButton(text, style, onClick)

        return this
    }

    /**
     * Set the Button's Typeface
     *
     * @param typeface Typeface to use
     * @return This Alerter
     */
    fun setButtonTypeface(typeface: Typeface): Alerter {
        alert?.buttonTypeFace = typeface

        return this
    }

    fun getLayoutContainer(): View? {
        return alert?.layoutContainer
    }

    /**
     * Creates a weak reference to the calling Activity
     *
     * @param activity The calling Activity
     */
    private fun setActivity(activity: Activity) {
        activityWeakReference = WeakReference(activity)
    }

    companion object {

        private var activityWeakReference: WeakReference<Activity>? = null

        /**
         * Creates the Alert, and maintains a reference to the calling Activity
         *
         * @param activity The calling Activity
         * @return This Alerter
         */
        @JvmStatic
        fun create(activity: Activity?): Alerter {
            return create(activity, R.layout.alerter_alert_default_layout)
        }

        /**
         * Creates the Alert with custom view, and maintains a reference to the calling Activity
         *
         * @param activity The calling Activity
         * @param layoutId Custom view layout res id
         * @return This Alerter
         */
        @JvmStatic
        fun create(activity: Activity?, @LayoutRes layoutId: Int): Alerter {
            requireNotNull(activity) { "Activity cannot be null!" }

            val alerter = Alerter()

            //Hide current Alert, if one is active
            clearCurrent(activity)

            alerter.setActivity(activity)
            alerter.alert = Alert(activity, layoutId)

            return alerter
        }

        /**
         * Cleans up the currently showing alert view, if one is present
         *
         * @param activity The current Activity
         */
        @JvmStatic
        fun clearCurrent(activity: Activity?) {
            (activity?.window?.decorView as? ViewGroup)?.let {
                //Find all Alert Views in Parent layout
                for (i in 0..it.childCount) {
                    val childView = if (it.getChildAt(i) is Alert) it.getChildAt(i) as Alert else null
                    if (childView != null && childView.windowToken != null) {
                        ViewCompat.animate(childView).alpha(0f).withEndAction(getRemoveViewRunnable(childView))
                    }
                }
            }
        }

        /**
         * Hides the currently showing alert view, if one is present
         */
        @JvmStatic
        fun hide() {
            activityWeakReference?.get()?.let {
                clearCurrent(it)
            }
        }

        /**
         * Check if an Alert is currently showing
         *
         * @return True if an Alert is showing, false otherwise
         */
        @JvmStatic
        val isShowing: Boolean
            get() {
                var isShowing = false

                activityWeakReference?.get()?.let {
                    isShowing = it.findViewById<View>(R.id.llAlertBackground) != null
                }

                return isShowing
            }

        private fun getRemoveViewRunnable(childView: Alert?): Runnable {
            return Runnable {
                childView?.let {
                    (childView.parent as? ViewGroup)?.removeView(childView)
                }
            }
        }
    }
}
