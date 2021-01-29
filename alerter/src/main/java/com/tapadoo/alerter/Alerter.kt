package com.tapadoo.alerter

import android.app.Activity
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.net.Uri
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.*
import androidx.appcompat.app.AppCompatDialog
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
     * Shows the Alert, after it's built
     *
     * @return An Alert object check can be altered or hidden
     */
    fun show(): Alert? {
        //This will get the Activity Window's DecorView
        decorView?.get()?.let {
            android.os.Handler(Looper.getMainLooper()).post {
                it.addView(alert)
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
        decorView?.get()?.let {
            alert?.setAlertBackgroundColor(ContextCompat.getColor(it.context.applicationContext, colorResId))
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
     * Set the Alert's Right Icon
     *
     * @param iconId The Drawable's Resource Idw
     * @return This Alerter
     */
    fun setRightIcon(@DrawableRes rightIconId: Int): Alerter {
        alert?.setRightIcon(rightIconId)

        return this
    }

    /**
     * Set the Alert's Right Icon
     *
     * @param bitmap The Bitmap object to use for the right icon.
     * @return This Alerter
     */
    fun setRightIcon(bitmap: Bitmap): Alerter {
        alert?.setRightIcon(bitmap)

        return this
    }

    /**
     * Set the Alert's Right Icon
     *
     * @param drawable The Drawable to use for the right icon.
     * @return This Alerter
     */
    fun setRightIcon(drawable: Drawable): Alerter {
        alert?.setRightIcon(drawable)

        return this
    }

    /**
     * Set the Alert's Right Icon size
     *
     * @param size Dimension int.
     * @return This Alerter
     */
    fun setRightIconSize(@DimenRes size: Int): Alerter {
        alert?.setRightIconSize(size)

        return this
    }

    /**
     * Set the Alert's Right Icon size
     *
     * @param size Right Icon size in pixel.
     * @return This Alerter
     */
    fun setRightIconPixelSize(@Px size: Int): Alerter {
        alert?.setRightIconPixelSize(size)

        return this
    }

    /**
     * Set the right icon color for the Alert
     *
     * @param color Color int
     * @return This Alerter
     */
    fun setRightIconColorFilter(@ColorInt color: Int): Alerter {
        alert?.setRightIconColorFilter(color)

        return this
    }

    /**
     * Set the right icon color for the Alert
     *
     * @param colorFilter ColorFilter
     * @return This Alerter
     */
    fun setRightIconColorFilter(colorFilter: ColorFilter): Alerter {
        alert?.setRightIconColorFilter(colorFilter)

        return this
    }

    /**
     * Set the right icon color for the Alert
     *
     * @param color Color int
     * @param mode  PorterDuff.Mode
     * @return This Alerter
     */
    fun setRightIconColorFilter(@ColorInt color: Int, mode: PorterDuff.Mode): Alerter {
        alert?.setRightIconColorFilter(color, mode)

        return this
    }

    /**
     * Set the right icons's position for the Alert
     *
     * @param gravity Gravity int
     * @return This Alerter
     */
    fun setRightIconPosition(gravity: Int): Alerter {
        alert?.setRightIconPosition(gravity)

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
     * Enable or Disable Right Icon Pulse Animations
     *
     * @param pulse True if the right icon should pulse
     * @return This Alerter
     */
    fun enableRightIconPulse(pulse: Boolean): Alerter {
        alert?.pulseRightIcon(pulse)

        return this
    }

    /**
     * Set whether to show the right icon in the alert or not
     *
     * @param showRightIcon True to show the right icon, false otherwise
     * @return This Alerter
     */
    fun showRightIcon(showRightIcon: Boolean): Alerter {
        alert?.showRightIcon(showRightIcon)

        return this
    }

    /**
     * Set whether to show the animation on focus/pressed states
     *
     * @param enabled True to show the animation, false otherwise
     */
    fun enableClickAnimation(enabled: Boolean): Alerter {
        alert?.enableClickAnimation(enabled)

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
     * Set sound Uri
     * if set null, sound will be disabled
     *
     * @param uri To set sound Uri (raw folder)
     * @return This Alerter
     */
    @JvmOverloads
    fun setSound(uri: Uri? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)): Alerter {
        alert?.setSound(uri)

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

    companion object {

        private var decorView: WeakReference<ViewGroup>? = null

        /**
         * Creates the Alert
         *
         * @param activity The calling Activity
         * @return This Alerter
         */
        @JvmStatic
        @JvmOverloads
        fun create(activity: Activity, layoutId: Int = R.layout.alerter_alert_default_layout): Alerter {
            return create(activity = activity, dialog = null, layoutId = layoutId)
        }

        /**
         * Creates the Alert
         *
         * @param dialog The calling Dialog
         * @return This Alerter
         */
        @JvmStatic
        @JvmOverloads
        fun create(dialog: Dialog, layoutId: Int = R.layout.alerter_alert_default_layout): Alerter {
            return create(activity = null, dialog = dialog, layoutId = layoutId)
        }

        /**
         * Creates the Alert with custom view, and maintains a reference to the calling Activity or Dialog's
         * DecorView
         *
         * @param activity The calling Activity
         * @param dialog The calling Dialog
         * @param layoutId Custom view layout res id
         * @return This Alerter
         */
        @JvmStatic
        private fun create(activity: Activity? = null, dialog: Dialog? = null, @LayoutRes layoutId: Int): Alerter {
            val alerter = Alerter()

            //Hide current Alert, if one is active
            clearCurrent(activity, dialog)

            alerter.alert = dialog?.window?.let {
                decorView = WeakReference(it.decorView as ViewGroup)
                Alert(context = it.decorView.context, layoutId = layoutId)
            } ?: run {
                activity?.window?.let {
                    decorView = WeakReference(it.decorView as ViewGroup)
                    Alert(context = it.decorView.context, layoutId = layoutId)
                }
            }

            return alerter
        }

        /**
         * Cleans up the currently showing alert view, if one is present. Either pass
         * the calling Activity, or the calling Dialog
         *
         * @param activity The current Activity
         * @param dialog The current Dialog
         */
        @JvmStatic
        fun clearCurrent(activity: Activity?, dialog: Dialog?) {
            dialog?.let {
                it.window?.decorView as? ViewGroup
            } ?: kotlin.run {
                activity?.window?.decorView as? ViewGroup
            }?.also {
                removeAlertFromParent(it)
            }
        }

        /**
         * Hides the currently showing alert view, if one is present
         */
        @JvmStatic
        fun hide() {
            decorView?.get()?.let {
                removeAlertFromParent(it)
            }
        }

        private fun removeAlertFromParent(decorView: ViewGroup) {
            //Find all Alert Views in Parent layout
            for (i in 0..decorView.childCount) {
                val childView = if (decorView.getChildAt(i) is Alert) decorView.getChildAt(i) as Alert else null
                if (childView != null && childView.windowToken != null) {
                    ViewCompat.animate(childView).alpha(0f).withEndAction(getRemoveViewRunnable(childView))
                }
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

                decorView?.get()?.let {
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
