package com.tapadoo.alerter

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.TextViewCompat
import com.tapadoo.alerter.utils.getDimenPixelSize
import com.tapadoo.alerter.utils.getRippleDrawable
import com.tapadoo.alerter.utils.notchHeight
import kotlinx.android.synthetic.main.alerter_alert_default_layout.view.*
import kotlinx.android.synthetic.main.alerter_alert_view.view.*

/**
 * Custom Alert View
 *
 * @author Kevin Murphy, Tapadoo, Dublin, Ireland, Europe, Earth.
 * @since 26/01/2016
 */
@SuppressLint("ViewConstructor")
class Alert @JvmOverloads constructor(context: Context,
                                      @LayoutRes layoutId: Int,
                                      attrs: AttributeSet? = null,
                                      defStyle: Int = 0)
    : FrameLayout(context, attrs, defStyle), View.OnClickListener, Animation.AnimationListener, SwipeDismissTouchListener.DismissCallbacks {

    private var onShowListener: OnShowAlertListener? = null
    internal var onHideListener: OnHideAlertListener? = null

    internal var enterAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.alerter_slide_in_from_top)
    internal var exitAnimation: Animation = AnimationUtils.loadAnimation(context, R.anim.alerter_slide_out_to_top)

    internal var duration = DISPLAY_TIME_IN_SECONDS

    private var showIcon: Boolean = true
    private var enableIconPulse = true
    private var enableInfiniteDuration: Boolean = false
    private var enableProgress: Boolean = false

    private var showRightIcon: Boolean = false
    private var enableClickAnimation: Boolean = true
    private var enableRightIconPurse = true

    private var runningAnimation: Runnable? = null

    private var isDismissible = true

    private var buttons = ArrayList<Button>()
    var buttonTypeFace: Typeface? = null

    /**
     * Flag to ensure we only set the margins once
     */
    private var marginSet: Boolean = false

    /**
     * Flag to enable / disable haptic feedback
     */
    private var vibrationEnabled = true

    /**
     * Uri to set sound
     */
    private var soundUri: Uri? = null

    /**
     * Sets the Layout Gravity of the Alert
     *
     * @param layoutGravity Layout Gravity of the Alert
     */
    var layoutGravity = Gravity.TOP
        set(value) {

            if (value != Gravity.TOP) {
                enterAnimation = AnimationUtils.loadAnimation(context, R.anim.alerter_slide_in_from_bottom)
                exitAnimation = AnimationUtils.loadAnimation(context, R.anim.alerter_slide_out_to_bottom)
            }

            field = value
        }

    /**
     * Sets the Gravity of the Alert
     *
     * @param contentGravity Gravity of the Alert
     */
    var contentGravity: Int
        get() = (llAlertBackground?.layoutParams as LayoutParams).gravity
        set(contentGravity) {

            (tvTitle?.layoutParams as? LinearLayout.LayoutParams)?.apply {
                gravity = contentGravity
            }

            val paramsText = tvText?.layoutParams as? LinearLayout.LayoutParams
            paramsText?.gravity = contentGravity
            tvText?.layoutParams = paramsText
        }

    val layoutContainer: View? by lazy { findViewById<View>(R.id.vAlertContentContainer) }

    private val currentDisplay: Display? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context.display
        } else {
            @Suppress("DEPRECATION")
            (context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        }
    }

    private val physicalScreenHeight: Int
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics().also { currentDisplay?.getRealMetrics(it) }.heightPixels
        } else {
            usableScreenHeight
        }

    private val usableScreenHeight: Int
        get() = Resources.getSystem().displayMetrics.heightPixels

    private val cutoutsHeight: Int
        get() = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ->
                currentDisplay?.cutout?.run { safeInsetTop + safeInsetBottom } ?: 0
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.P ->
                rootWindowInsets?.displayCutout?.run { safeInsetTop + safeInsetBottom } ?: 0
            else -> 0
        }

    private val navigationBarHeight by lazy {
        physicalScreenHeight - usableScreenHeight - cutoutsHeight
    }

    init {
        inflate(context, R.layout.alerter_alert_view, this)

        vAlertContentContainer.layoutResource = layoutId
        vAlertContentContainer.inflate()

        isHapticFeedbackEnabled = true

        ViewCompat.setTranslationZ(this, Integer.MAX_VALUE.toFloat())

        llAlertBackground.setOnClickListener(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        llAlertBackground.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                foreground = if (enableClickAnimation.not()) {
                    null
                } else {
                    context.getRippleDrawable()
                }
            }

            (layoutParams as LayoutParams).gravity = layoutGravity

            if (layoutGravity != Gravity.TOP) {
                setPadding(
                        paddingLeft, getDimenPixelSize(R.dimen.alerter_padding_default),
                        paddingRight, getDimenPixelSize(R.dimen.alerter_alert_padding)
                )
            }
        }

        (layoutParams as MarginLayoutParams).apply {
            if (layoutGravity != Gravity.TOP) {
                bottomMargin = navigationBarHeight
            }
        }

        enterAnimation.setAnimationListener(this)

        // Set Animation to be Run when View is added to Window
        animation = enterAnimation

        // Add all buttons
        buttons.forEach { button ->
            buttonTypeFace?.let { button.typeface = it }
            llButtonContainer.addView(button)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (!marginSet) {
            marginSet = true

            // Add a negative top margin to compensate for overshoot enter animation
            (layoutParams as MarginLayoutParams).topMargin = getDimenPixelSize(R.dimen.alerter_alert_negative_margin_top)

            // Check for Cutout
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                llAlertBackground.apply {
                    setPadding(paddingLeft, paddingTop + (notchHeight() / 2), paddingRight, paddingBottom)
                }
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    // Release resources once view is detached.
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        enterAnimation.setAnimationListener(null)
    }

    /* Override Methods */

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.performClick()
        return super.onTouchEvent(event)
    }

    override fun onClick(v: View) {
        if (isDismissible) {
            hide()
        }
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        llAlertBackground.setOnClickListener(listener)
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        for (i in 0 until childCount) {
            getChildAt(i).visibility = visibility
        }
    }

    /* Interface Method Implementations */

    override fun onAnimationStart(animation: Animation) {
        if (!isInEditMode) {
            visibility = View.VISIBLE

            if (vibrationEnabled) {
                performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            }
            soundUri?.let {
                val r = RingtoneManager.getRingtone(context, soundUri)
                r.play()
            }

            if (enableProgress) {
                ivIcon?.visibility = View.INVISIBLE
                ivRightIcon?.visibility = View.INVISIBLE
                pbProgress?.visibility = View.VISIBLE
            } else {
                if (showIcon) {
                    ivIcon?.visibility = View.VISIBLE
                    // Only pulse if we're not showing the progress
                    if (enableIconPulse) {
                        ivIcon?.startAnimation(AnimationUtils.loadAnimation(context, R.anim.alerter_pulse))
                    }
                } else {
                    flIconContainer?.visibility = View.GONE
                }
                if (showRightIcon) {
                    ivRightIcon?.visibility = View.VISIBLE

                    if (enableRightIconPurse) {
                        ivRightIcon?.startAnimation(AnimationUtils.loadAnimation(context, R.anim.alerter_pulse))
                    }
                } else {
                    flRightIconContainer?.visibility = View.GONE
                }
            }
        }
    }

    override fun onAnimationEnd(animation: Animation) {
        onShowListener?.onShow()

        startHideAnimation()
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private fun startHideAnimation() {
        //Start the Handler to clean up the Alert
        if (!enableInfiniteDuration) {
            runningAnimation = Runnable { hide() }

            postDelayed(runningAnimation, duration)
        }
    }

    override fun onAnimationRepeat(animation: Animation) {
        //Ignore
    }

    /* Clean Up Methods */

    /**
     * Cleans up the currently showing alert view.
     */
    private fun hide() {
        try {
            exitAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    llAlertBackground?.setOnClickListener(null)
                    llAlertBackground?.isClickable = false
                }

                override fun onAnimationEnd(animation: Animation) {
                    removeFromParent()
                }

                override fun onAnimationRepeat(animation: Animation) {
                    //Ignore
                }
            })

            startAnimation(exitAnimation)
        } catch (ex: Exception) {
            Log.e(javaClass.simpleName, Log.getStackTraceString(ex))
        }
    }

    /**
     * Removes Alert View from its Parent Layout
     */
    internal fun removeFromParent() {
        clearAnimation()
        visibility = View.GONE

        postDelayed(object : Runnable {
            override fun run() {
                try {
                    if (parent != null) {
                        try {
                            (parent as ViewGroup).removeView(this@Alert)

                            onHideListener?.onHide()
                        } catch (ex: Exception) {
                            Log.e(javaClass.simpleName, "Cannot remove from parent layout")
                        }
                    }
                } catch (ex: Exception) {
                    Log.e(javaClass.simpleName, Log.getStackTraceString(ex))
                }
            }
        }, CLEAN_UP_DELAY_MILLIS.toLong())
    }

    /* Setters and Getters */

    /**
     * Sets the Alert Background colour
     *
     * @param color The qualified colour integer
     */
    fun setAlertBackgroundColor(@ColorInt color: Int) {
        llAlertBackground.setBackgroundColor(color)
    }

    /**
     * Sets the Alert Background Drawable Resource
     *
     * @param resource The qualified drawable integer
     */
    fun setAlertBackgroundResource(@DrawableRes resource: Int) {
        llAlertBackground.setBackgroundResource(resource)
    }

    /**
     * Sets the Alert Background Drawable
     *
     * @param drawable The qualified drawable
     */
    fun setAlertBackgroundDrawable(drawable: Drawable) {
        ViewCompat.setBackground(llAlertBackground, drawable)
    }

    /**
     * Sets the Title of the Alert
     *
     * @param titleId String resource id of the Alert title
     */
    fun setTitle(@StringRes titleId: Int) {
        setTitle(context.getString(titleId))
    }

    /**
     * Sets the Text of the Alert
     *
     * @param textId String resource id of the Alert text
     */
    fun setText(@StringRes textId: Int) {
        setText(context.getString(textId))
    }

    /**
     * Disable touches while the Alert is showing
     */
    fun disableOutsideTouch() {
        flClickShield.isClickable = true
    }

    /**
     * Sets the Title of the Alert
     *
     * @param title CharSequence object to be used as the Alert title
     */
    fun setTitle(title: CharSequence) {
        if (!TextUtils.isEmpty(title)) {
            tvTitle?.visibility = View.VISIBLE
            tvTitle?.text = title
        }
    }

    /**
     * Set the Title's text appearance of the Title
     *
     * @param textAppearance The style resource id
     */
    fun setTitleAppearance(@StyleRes textAppearance: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tvTitle?.setTextAppearance(textAppearance)
        } else {
            TextViewCompat.setTextAppearance(tvTitle, textAppearance)
        }
    }

    /**
     * Set the Title's typeface
     *
     * @param typeface The typeface to use
     */
    fun setTitleTypeface(typeface: Typeface) {
        tvTitle?.typeface = typeface
    }

    /**
     * Set the Text's typeface
     *
     * @param typeface The typeface to use
     */
    fun setTextTypeface(typeface: Typeface) {
        tvText?.typeface = typeface
    }

    /**
     * Sets the Text of the Alert
     *
     * @param text CharSequence object to be used as the Alert text
     */
    fun setText(text: CharSequence) {
        if (!TextUtils.isEmpty(text)) {
            tvText?.visibility = View.VISIBLE
            tvText?.text = text
        }
    }

    /**
     * Set the Text's text appearance of the Title
     *
     * @param textAppearance The style resource id
     */
    fun setTextAppearance(@StyleRes textAppearance: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tvText?.setTextAppearance(textAppearance)
        } else {
            TextViewCompat.setTextAppearance(tvText, textAppearance)
        }
    }

    /**
     * Set the inline icon for the Alert
     *
     * @param iconId Drawable resource id of the icon to use in the Alert
     */
    fun setIcon(@DrawableRes iconId: Int) {
        ivIcon?.setImageDrawable(AppCompatResources.getDrawable(context, iconId))
    }

    /**
     * Set the icon color for the Alert
     *
     * @param color Color int
     */
    fun setIconColorFilter(@ColorInt color: Int) {
        ivIcon?.setColorFilter(color)
    }

    /**
     * Set the icon color for the Alert
     *
     * @param colorFilter ColorFilter
     */
    fun setIconColorFilter(colorFilter: ColorFilter) {
        ivIcon?.colorFilter = colorFilter
    }

    /**
     * Set the icon color for the Alert
     *
     * @param color Color int
     * @param mode  PorterDuff.Mode
     */
    fun setIconColorFilter(@ColorInt color: Int, mode: PorterDuff.Mode) {
        ivIcon?.setColorFilter(color, mode)
    }

    /**
     * Set the inline icon for the Alert
     *
     * @param bitmap Bitmap image of the icon to use in the Alert.
     */
    fun setIcon(bitmap: Bitmap) {
        ivIcon?.setImageBitmap(bitmap)
    }

    /**
     * Set the inline icon for the Alert
     *
     * @param drawable Drawable image of the icon to use in the Alert.
     */
    fun setIcon(drawable: Drawable) {
        ivIcon?.setImageDrawable(drawable)
    }

    /**
     * Set the inline icon size for the Alert
     *
     * @param size Dimension int.
     */
    fun setIconSize(@DimenRes size: Int) {
        val pixelSize = getDimenPixelSize(size)
        setIconPixelSize(pixelSize)
    }

    /**
     * Set the inline icon size for the Alert
     *
     * @param size Icon size in pixel.
     */
    fun setIconPixelSize(@Px size: Int) {
        ivIcon.layoutParams = ivIcon.layoutParams.apply {
            width = size
            height = size
            minimumWidth = size
            minimumHeight = size
        }
    }

    /**
     * Set whether to show the icon in the alert or not
     *
     * @param showIcon True to show the icon, false otherwise
     */
    fun showIcon(showIcon: Boolean) {
        this.showIcon = showIcon
    }

    /**
     * Set the inline right icon for the Alert
     *
     * @param iconId Drawable resource id of the right icon to use in the Alert
     */
    fun setRightIcon(@DrawableRes iconId: Int) {
        ivRightIcon?.setImageDrawable(AppCompatResources.getDrawable(context, iconId))
    }

    /**
     * Set the right icon color for the Alert
     *
     * @param color Color int
     */
    fun setRightIconColorFilter(@ColorInt color: Int) {
        ivRightIcon?.setColorFilter(color)
    }

    /**
     * Set the right icon color for the Alert
     *
     * @param colorFilter ColorFilter
     */
    fun setRightIconColorFilter(colorFilter: ColorFilter) {
        ivRightIcon?.colorFilter = colorFilter
    }

    /**
     * Set the right icon color for the Alert
     *
     * @param color Color int
     * @param mode  PorterDuff.Mode
     */
    fun setRightIconColorFilter(@ColorInt color: Int, mode: PorterDuff.Mode) {
        ivRightIcon?.setColorFilter(color, mode)
    }

    /**
     * Set the inline right icon for the Alert
     *
     * @param bitmap Bitmap image of the right icon to use in the Alert.
     */
    fun setRightIcon(bitmap: Bitmap) {
        ivRightIcon?.setImageBitmap(bitmap)
    }

    /**
     * Set the inline right icon for the Alert
     *
     * @param drawable Drawable image of the right icon to use in the Alert.
     */
    fun setRightIcon(drawable: Drawable) {
        ivRightIcon?.setImageDrawable(drawable)
    }

    /**
     * Set the inline right icon size for the Alert
     *
     * @param size Dimension int.
     */
    fun setRightIconSize(@DimenRes size: Int) {
        val pixelSize = context.resources.getDimensionPixelSize(size)
        setRightIconPixelSize(pixelSize)
    }

    /**
     * Set the inline right icon size for the Alert
     *
     * @param size Icon size in pixel.
     */
    fun setRightIconPixelSize(@Px size: Int) {
        ivRightIcon.layoutParams = ivRightIcon.layoutParams.apply {
            width = size
            height = size
            minimumWidth = size
            minimumHeight = size
        }
    }

    /**
     * Set whether to show the right icon in the alert or not
     *
     * @param showRightIcon True to show the right icon, false otherwise
     */
    fun showRightIcon(showRightIcon: Boolean) {
        this.showRightIcon = showRightIcon
    }

    /**
     * Set whether to show the animation on focus/pressed states
     *
     * @param enabled True to show the animation, false otherwise
     */
    fun enableClickAnimation(enabled: Boolean) {
        this.enableClickAnimation = enabled
    }

    /**
     * Set right icon position
     *
     * @param position gravity of an right icon's parent. Can be: Gravity.TOP,
     * Gravity.CENTER, Gravity.CENTER_VERTICAL or Gravity.BOTTOM
     */
    fun setRightIconPosition(position: Int) {
        if (position == Gravity.TOP
                || position == Gravity.CENTER
                || position == Gravity.CENTER_VERTICAL
                || position == Gravity.BOTTOM) {
            flRightIconContainer.layoutParams = (flRightIconContainer.layoutParams as LinearLayout.LayoutParams).apply {
                gravity = position
            }
        }
    }

    /**
     * Set if the alerter is isDismissible or not
     *
     * @param dismissible True if alert can be dismissed
     */
    fun setDismissible(dismissible: Boolean) {
        this.isDismissible = dismissible
    }

    /**
     * Get if the alert is isDismissible
     * @return
     */
    fun isDismissible(): Boolean {
        return isDismissible
    }

    /**
     * Set whether to enable swipe to dismiss or not
     */
    fun enableSwipeToDismiss() {
        llAlertBackground.let {
            it.setOnTouchListener(SwipeDismissTouchListener(it, object : SwipeDismissTouchListener.DismissCallbacks {
                override fun canDismiss(): Boolean {
                    return true
                }

                override fun onDismiss(view: View) {
                    removeFromParent()
                }

                override fun onTouch(view: View, touch: Boolean) {
                    // Ignore
                }
            }))
        }
    }

    /**
     * Set if the Icon should pulse or not
     *
     * @param shouldPulse True if the icon should be animated
     */
    fun pulseIcon(shouldPulse: Boolean) {
        this.enableIconPulse = shouldPulse
    }

    /**
     * Set if the Right Icon should pulse or not
     *
     * @param shouldPulse True if the right icon should be animated
     */
    fun pulseRightIcon(shouldPulse: Boolean) {
        this.enableRightIconPurse = shouldPulse
    }

    /**
     * Set if the duration of the alert is infinite
     *
     * @param enableInfiniteDuration True if the duration of the alert is infinite
     */
    fun setEnableInfiniteDuration(enableInfiniteDuration: Boolean) {
        this.enableInfiniteDuration = enableInfiniteDuration
    }

    /**
     * Enable or disable progress bar
     *
     * @param enableProgress True to enable, False to disable
     */
    fun setEnableProgress(enableProgress: Boolean) {
        this.enableProgress = enableProgress
    }

    /**
     * Set the Progress bar color from a color resource
     *
     * @param color The color resource
     */
    fun setProgressColorRes(@ColorRes color: Int) {
        pbProgress?.progressDrawable?.colorFilter = LightingColorFilter(MUL, ContextCompat.getColor(context, color))
    }

    /**
     * Set the Progress bar color from a color resource
     *
     * @param color The color resource
     */
    fun setProgressColorInt(@ColorInt color: Int) {
        pbProgress?.progressDrawable?.colorFilter = LightingColorFilter(MUL, color)
    }

    /**
     * Set the alert's listener to be fired on the alert being fully shown
     *
     * @param listener Listener to be fired
     */
    fun setOnShowListener(listener: OnShowAlertListener) {
        this.onShowListener = listener
    }

    /**
     * Enable or Disable haptic feedback
     *
     * @param vibrationEnabled True to enable, false to disable
     */
    fun setVibrationEnabled(vibrationEnabled: Boolean) {
        this.vibrationEnabled = vibrationEnabled
    }

    /**
     * Set sound Uri
     *
     * @param soundUri To set sound Uri (raw folder)
     */
    fun setSound(soundUri: Uri?) {
        this.soundUri = soundUri
    }

    /**
     *  Set elevation of the alert background.
     *
     *  Only available for version LOLLIPOP and above.
     *
     *  @param elevation Elevation value, in pixel.
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setBackgroundElevation(elevation: Float) {
        llAlertBackground.elevation = elevation
    }

    /**
     * Show a button with the given text, and on click listener
     *
     * @param text The text to display on the button
     * @param onClick The on click listener
     */
    fun addButton(text: CharSequence, @StyleRes style: Int, onClick: OnClickListener) {
        Button(ContextThemeWrapper(context, style), null, style).apply {
            this.text = text
            this.setOnClickListener(onClick)

            buttons.add(this)
        }

        // Alter padding
        llAlertBackground?.apply {
            this.setPadding(this.paddingLeft, this.paddingTop, this.paddingRight, this.paddingBottom / 2)
        }
    }

    /**
     * @return the TextView for the title
     */
    fun getTitle(): TextView {
        return tvTitle
    }

    /**
     * @return the TextView for the text
     */
    fun getText(): TextView {
        return tvText
    }

    override fun canDismiss(): Boolean {
        return isDismissible
    }

    override fun onDismiss(view: View) {
        flClickShield?.removeView(llAlertBackground)
    }

    override fun onTouch(view: View, touch: Boolean) {
        if (touch) {
            removeCallbacks(runningAnimation)
        } else {
            startHideAnimation()
        }
    }

    companion object {

        private const val CLEAN_UP_DELAY_MILLIS = 100

        /**
         * The amount of time the alert will be visible on screen in seconds
         */
        private const val DISPLAY_TIME_IN_SECONDS: Long = 3000
        private const val MUL = -0x1000000
    }
}
