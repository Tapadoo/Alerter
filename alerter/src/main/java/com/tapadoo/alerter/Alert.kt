package com.tapadoo.alerter

import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.*
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.content.res.AppCompatResources
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.*

/**
 * Custom Alert View
 *
 * @author Kevin Murphy, Tapadoo, Dublin, Ireland, Europe, Earth.
 * @since 26/01/2016
 */
class Alert @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0)
    : FrameLayout(context, attrs, defStyle), View.OnClickListener, Animation.AnimationListener, SwipeDismissTouchListener.DismissCallbacks {

    //UI
    internal var flClickShield: FrameLayout? = null
    internal var rlContainer: ViewGroup? = null
    internal var progressBar: ProgressBar? = null
    internal var alertBackground: FrameLayout? = null

    internal var title: TextView? = null
    internal var text: TextView? = null
    internal var icon: ImageView? = null

    internal var onShowListener: OnShowAlertListener? = null
    internal var onHideListener: OnHideAlertListener? = null

    internal var slideInAnimation: Animation? = null
    internal var slideOutAnimation: Animation? = null

    internal var duration = DISPLAY_TIME_IN_SECONDS

    private var enableIconPulse = true
    private var enableInfiniteDuration: Boolean = false
    private var enableProgress: Boolean = false

    private var runningAnimation: Runnable? = null

    /**
     * Flag to ensure we only set the margins once
     */
    private var marginSet: Boolean = false

    /**
     * Flag to enable / disable haptic feedback
     */
    private var vibrationEnabled = true

    /**
     * Sets the Gravity of the Alert
     *
     * @param contentGravity Gravity of the Alert
     */
    var contentGravity: Int
        get() = (rlContainer!!.layoutParams as FrameLayout.LayoutParams).gravity
        set(contentGravity) {
            val paramsTitle = title?.layoutParams as LinearLayout.LayoutParams
            paramsTitle.gravity = contentGravity
            title?.layoutParams = paramsTitle

            val paramsText = text?.layoutParams as LinearLayout.LayoutParams
            paramsText.gravity = contentGravity
            text?.layoutParams = paramsText
        }

    init {
        initView()
    }

    private fun initView() {
        View.inflate(context, R.layout.alerter_alert_view, this)
        isHapticFeedbackEnabled = true

        ViewCompat.setTranslationZ(this, Integer.MAX_VALUE.toFloat())

        alertBackground = findViewById(R.id.flAlertBackground)
        flClickShield = findViewById(R.id.flClickShield)
        icon = findViewById(R.id.ivIcon)
        title = findViewById(R.id.tvTitle)
        text = findViewById(R.id.tvText)
        rlContainer = findViewById(R.id.rlContainer)
        progressBar = findViewById(R.id.pbProgress)

        alertBackground!!.setOnClickListener(this)

        //Setup Enter & Exit Animations
        slideInAnimation = AnimationUtils.loadAnimation(context, R.anim.alerter_slide_in_from_top)
        slideOutAnimation = AnimationUtils.loadAnimation(context, R.anim.alerter_slide_out_to_top)
        slideInAnimation!!.setAnimationListener(this)

        //Set Animation to be Run when View is added to Window
        animation = slideInAnimation
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (!marginSet) {
            marginSet = true

            // Add a negative top margin to compensate for overshoot enter animation
            val params = layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin = context.resources.getDimensionPixelSize(R.dimen.alerter_alert_negative_margin_top)
            requestLayout()
        }
    }

    // Release resources once view is detached.
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        slideInAnimation!!.setAnimationListener(null)
    }

    /* Override Methods */

    override fun onTouchEvent(event: MotionEvent): Boolean {
        performClick()
        return super.onTouchEvent(event)
    }

    override fun onClick(v: View) {
        hide()
    }

    override fun setOnClickListener(listener: View.OnClickListener?) {
        alertBackground!!.setOnClickListener(listener)
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
        }
    }

    override fun onAnimationEnd(animation: Animation) {
        //Start the Icon Animation once the Alert is settled
        if (enableIconPulse && icon!!.visibility == View.VISIBLE) {
            try {
                icon!!.startAnimation(AnimationUtils.loadAnimation(context, R.anim.alerter_pulse))
            } catch (ex: Exception) {
                Log.e(javaClass.simpleName, Log.getStackTraceString(ex))
            }

        }

        if (onShowListener != null) {
            onShowListener!!.onShow()
        }

        startHideAnimation()
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private fun startHideAnimation() {
        //Start the Handler to clean up the Alert
        if (!enableInfiniteDuration) {
            runningAnimation = Runnable { hide() }

            postDelayed(runningAnimation, duration)
        }

        if (enableProgress) {
            progressBar!!.visibility = View.VISIBLE

            val valueAnimator = ValueAnimator.ofInt(0, 100)
            valueAnimator.duration = duration
            valueAnimator.interpolator = LinearInterpolator()
            valueAnimator.addUpdateListener { animation -> progressBar!!.progress = animation.animatedValue as Int }
            valueAnimator.start()
        }

    }

    override fun onAnimationRepeat(animation: Animation) {
        //Ignore
    }

    /* Clean Up Methods */

    /**
     * Cleans up the currently showing alert view.
     */
    fun hide() {
        try {
            slideOutAnimation!!.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    alertBackground!!.setOnClickListener(null)
                    alertBackground!!.isClickable = false
                }

                override fun onAnimationEnd(animation: Animation) {
                    removeFromParent()
                }

                override fun onAnimationRepeat(animation: Animation) {
                    //Ignore
                }
            })

            startAnimation(slideOutAnimation)
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
                    if (parent == null) {
                        Log.e(javaClass.simpleName, "getParent() returning Null")
                    } else {
                        try {
                            (parent as ViewGroup).removeView(this@Alert)

                            if (onHideListener != null) {
                                onHideListener!!.onHide()
                            }
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
        alertBackground!!.setBackgroundColor(color)
    }

    /**
     * Sets the Alert Background Drawable Resource
     *
     * @param resource The qualified drawable integer
     */
    fun setAlertBackgroundResource(@DrawableRes resource: Int) {
        alertBackground!!.setBackgroundResource(resource)
    }

    /**
     * Sets the Alert Background Drawable
     *
     * @param drawable The qualified drawable
     */
    fun setAlertBackgroundDrawable(drawable: Drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            alertBackground!!.background = drawable
        } else {
            alertBackground!!.setBackgroundDrawable(drawable)
        }
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
        flClickShield!!.isClickable = true
    }

    /**
     * Sets the Title of the Alert
     *
     * @param title String object to be used as the Alert title
     */
    fun setTitle(title: String) {
        if (!TextUtils.isEmpty(title)) {
            this.title!!.visibility = View.VISIBLE
            this.title!!.text = title
        }
    }

    /**
     * Set the Title's text appearance of the Title
     *
     * @param textAppearance The style resource id
     */
    fun setTitleAppearance(@StyleRes textAppearance: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            title!!.setTextAppearance(textAppearance)
        } else {
            title!!.setTextAppearance(title!!.context, textAppearance)
        }
    }

    /**
     * Set the Title's typeface
     *
     * @param typeface The typeface to use
     */
    fun setTitleTypeface(typeface: Typeface) {
        title!!.typeface = typeface
    }

    /**
     * Set the Text's typeface
     *
     * @param typeface The typeface to use
     */
    fun setTextTypeface(typeface: Typeface) {
        text!!.typeface = typeface
    }

    /**
     * Sets the Text of the Alert
     *
     * @param text String resource id of the Alert text
     */
    fun setText(text: String) {
        if (!TextUtils.isEmpty(text)) {
            this.text!!.visibility = View.VISIBLE
            this.text!!.text = text
        }
    }

    /**
     * Set the Text's text appearance of the Title
     *
     * @param textAppearance The style resource id
     */
    fun setTextAppearance(@StyleRes textAppearance: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            text!!.setTextAppearance(textAppearance)
        } else {
            text!!.setTextAppearance(text!!.context, textAppearance)
        }
    }

    /**
     * Set the inline icon for the Alert
     *
     * @param iconId Drawable resource id of the icon to use in the Alert
     */
    fun setIcon(@DrawableRes iconId: Int) {
        icon!!.setImageDrawable(AppCompatResources.getDrawable(context, iconId))
    }

    /**
     * Set the icon color for the Alert
     *
     * @param color Color int
     */
    fun setIconColorFilter(@ColorInt color: Int) {
        if (icon != null) {
            icon!!.setColorFilter(color)
        }
    }

    /**
     * Set the icon color for the Alert
     *
     * @param colorFilter ColorFilter
     */
    fun setIconColorFilter(colorFilter: ColorFilter) {
        if (icon != null) {
            icon!!.colorFilter = colorFilter
        }
    }

    /**
     * Set the icon color for the Alert
     *
     * @param color Color int
     * @param mode  PorterDuff.Mode
     */
    fun setIconColorFilter(@ColorInt color: Int, mode: PorterDuff.Mode) {
        if (icon != null) {
            icon!!.setColorFilter(color, mode)
        }
    }

    /**
     * Set the inline icon for the Alert
     *
     * @param bitmap Bitmap image of the icon to use in the Alert.
     */
    fun setIcon(bitmap: Bitmap) {
        icon!!.setImageBitmap(bitmap)
    }

    /**
     * Set the inline icon for the Alert
     *
     * @param drawable Drawable image of the icon to use in the Alert.
     */
    fun setIcon(drawable: Drawable) {
        icon!!.setImageDrawable(drawable)
    }

    /**
     * Set whether to show the icon in the alert or not
     *
     * @param showIcon True to show the icon, false otherwise
     */
    fun showIcon(showIcon: Boolean) {
        icon!!.visibility = if (showIcon) View.VISIBLE else View.GONE
    }

    /**
     * Set whether to enable swipe to dismiss or not
     */
    fun enableSwipeToDismiss() {
        alertBackground!!.setOnTouchListener(SwipeDismissTouchListener(alertBackground!!, null, object : SwipeDismissTouchListener.DismissCallbacks {
            override fun canDismiss(token: Any): Boolean {
                return true
            }

            override fun onDismiss(view: View, token: Any) {
                removeFromParent()
            }

            override fun onTouch(view: View, touch: Boolean) {
                // Ignore
            }
        }))
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
        progressBar!!.progressDrawable.colorFilter = LightingColorFilter(MUL, ContextCompat.getColor(context, color))
    }

    /**
     * Set the Progress bar color from a color resource
     *
     * @param color The color resource
     */
    fun setProgressColorInt(@ColorInt color: Int) {
        progressBar!!.progressDrawable.colorFilter = LightingColorFilter(MUL, color)
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

    override fun canDismiss(token: Any): Boolean {
        return true
    }

    override fun onDismiss(view: View, token: Any) {
        flClickShield!!.removeView(alertBackground)
    }

    override fun onTouch(view: View, touch: Boolean) {
        if (touch) {
            removeCallbacks(runningAnimation)
        } else {
            startHideAnimation()
        }
    }

    companion object {

        private val CLEAN_UP_DELAY_MILLIS = 100

        /**
         * The amount of time the alert will be visible on screen in seconds
         */
        private val DISPLAY_TIME_IN_SECONDS: Long = 3000
        private val MUL = -0x1000000
    }
}