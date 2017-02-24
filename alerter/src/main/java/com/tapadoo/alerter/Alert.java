package com.tapadoo.alerter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tapadoo.android.R;

/**
 * Custom Alert View
 *
 * @author Kevin Murphy, Tapadoo, Dublin, Ireland, Europe, Earth.
 * @since 26/01/2016
 **/
public class Alert extends FrameLayout implements View.OnClickListener, Animation.AnimationListener {

    private static final int CLEAN_UP_DELAY_MILLIS = 100;

    /**
     * The amount of time the alert will be visible on screen in seconds
     */
    private static final long DISPLAY_TIME_IN_SECONDS = 3000;

    //UI
    private FrameLayout flBackground;
    private TextView tvTitle;
    private TextView tvText;
    private ImageView ivIcon;

    private Animation slideInAnimation;
    private Animation slideOutAnimation;

    private OnShowAlertListener onShowListener;
    private OnHideAlertListener onHideListener;

    private long duration = DISPLAY_TIME_IN_SECONDS;

    private boolean enableIconPulse = true;
    private boolean enableInfiniteDuration;

    /**
     * Flag to ensure we only set the margins once
     */
    private boolean marginSet;

    /**
     * This is the default view constructor. It requires a Context, and holds a reference to it.
     * If not cleaned up properly, memory will leak.
     *
     * @param context The Activity Context
     */
    public Alert(@NonNull final Context context) {
        super(context, null, R.attr.alertStyle);
        initView();
    }

    /**
     * This is the default view constructor. It requires a Context, and holds a reference to it.
     * If not cleaned up properly, memory will leak.
     *
     * @param context The Activity Context
     * @param attrs   View Attributes
     */
    public Alert(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs, R.attr.alertStyle);
        initView();
    }

    /**
     * This is the default view constructor. It requires a Context, and holds a reference to it.
     * If not cleaned up properly, memory will leak.
     *
     * @param context      The Activity Context
     * @param attrs        View Attributes
     * @param defStyleAttr Styles
     */
    public Alert(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.alerter_alert_view, this);
        setHapticFeedbackEnabled(true);

        flBackground = (FrameLayout) findViewById(R.id.flAlertBackground);
        ivIcon = (ImageView) findViewById(R.id.ivIcon);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvText = (TextView) findViewById(R.id.tvText);

        flBackground.setOnClickListener(this);

        //Setup Enter & Exit Animations
        slideInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.alerter_slide_in_from_top);
        slideOutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.alerter_slide_out_to_top);
        slideInAnimation.setAnimationListener(this);

        //Set Animation to be Run when View is added to Window
        setAnimation(slideInAnimation);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!marginSet) {
            marginSet = true;

            // Add a negative top margin to compensate for overshoot enter animation
            final ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getLayoutParams();
            params.topMargin = getContext().getResources().getDimensionPixelSize(R.dimen.alerter_alert_negative_margin_top);
            requestLayout();
        }
    }

    // Release resources once view is detached.
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        slideInAnimation.setAnimationListener(null);
    }

    /* Override Methods */

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        performClick();
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(final View v) {
        hide();
    }

    @Override
    public void setOnClickListener(final OnClickListener listener) {
        flBackground.setOnClickListener(listener);
    }

    @Override
    public void setVisibility(final int visibility) {
        super.setVisibility(visibility);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(visibility);
        }
    }

    /* Interface Method Implementations */

    @Override
    public void onAnimationStart(final Animation animation) {
        if (!isInEditMode()) {
            performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAnimationEnd(final Animation animation) {
        //Start the Icon Animation once the Alert is settled
        if (enableIconPulse) {
            try {
                ivIcon.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.alerter_pulse));
            } catch (Exception ex) {
                Log.e(getClass().getSimpleName(), Log.getStackTraceString(ex));
            }
        }

        if (onShowListener != null) {
            onShowListener.onShow();
        }

        //Start the Handler to clean up the Alert
        if (!enableInfiniteDuration) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    hide();
                }
            }, duration);
        }
    }

    @Override
    public void onAnimationRepeat(final Animation animation) {
        //Ignore
    }

    /* Clean Up Methods */

    /**
     * Cleans up the currently showing alert view.
     */
    public void hide() {
        try {
            slideOutAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(final Animation animation) {
                    flBackground.setOnClickListener(null);
                    flBackground.setClickable(false);
                }

                @Override
                public void onAnimationEnd(final Animation animation) {
                    removeFromParent();
                }

                @Override
                public void onAnimationRepeat(final Animation animation) {
                    //Ignore
                }
            });
            startAnimation(slideOutAnimation);
        } catch (Exception ex) {
            Log.e(getClass().getSimpleName(), Log.getStackTraceString(ex));
        }
    }

    /**
     * Removes Alert View from its Parent Layout
     */
    private void removeFromParent() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (getParent() == null) {
                        Log.e(getClass().getSimpleName(), "getParent() returning Null");
                    } else {
                        try {
                            ((ViewGroup) getParent()).removeView(Alert.this);

                            if (onHideListener != null) {
                                onHideListener.onHide();
                            }
                        } catch (Exception ex) {
                            Log.e(getClass().getSimpleName(), "Cannot remove from parent layout");
                        }
                    }
                } catch (Exception ex) {
                    Log.e(getClass().getSimpleName(), Log.getStackTraceString(ex));
                }
            }
        }, CLEAN_UP_DELAY_MILLIS);
    }

    /* Setters and Getters */

    /**
     * Sets the Alert Background colour
     *
     * @param color The qualified colour integer
     */
    public void setAlertBackgroundColor(@ColorInt final int color) {
        flBackground.setBackgroundColor(color);
    }

    /**
     * Sets the Title of the Alert
     *
     * @param titleId String resource id of the Alert title
     */
    public void setTitle(@StringRes final int titleId) {
        setTitle(getContext().getString(titleId));
    }

    /**
     * Sets the Text of the Alert
     *
     * @param textId String resource id of the Alert text
     */
    public void setText(@StringRes final int textId) {
        setText(getContext().getString(textId));
    }

    public FrameLayout getAlertBackground() {
        return flBackground;
    }

    public TextView getTitle() {
        return tvTitle;
    }

    /**
     * Sets the Title of the Alert
     *
     * @param title String object to be used as the Alert title
     */
    public void setTitle(@NonNull final String title) {
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(VISIBLE);
            tvTitle.setText(title);
        }
    }

    public TextView getText() {
        return tvText;
    }

    /**
     * Sets the Text of the Alert
     *
     * @param text String resource id of the Alert text
     */
    public void setText(final String text) {
        if (!TextUtils.isEmpty(text)) {
            tvText.setVisibility(VISIBLE);
            tvText.setText(text);
        }
    }

    public ImageView getIcon() {
        return ivIcon;
    }

    /**
     * Set the inline icon for the Alert
     *
     * @param iconId Drawable resource id of the icon to use in the Alert
     */
    public void setIcon(@DrawableRes final int iconId) {
        final Drawable iconDrawable = ContextCompat.getDrawable(getContext(), iconId);
        ivIcon.setImageDrawable(iconDrawable);
    }

    /**
     * Get the Alert's on screen duration
     *
     * @return The given duration, defaulting to 3000 milliseconds
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Set the alert's on screen duation
     *
     * @param duration The duration of alert on screen
     */
    public void setDuration(final long duration) {
        this.duration = duration;
    }

    /**
     * Set if the Icon should pulse or not
     *
     * @param shouldPulse True if the icon should be animated
     */
    public void pulseIcon(final boolean shouldPulse) {
        this.enableIconPulse = shouldPulse;
    }

    /**
     * Set if the duration of the alert is infinite
     *
     * @param enableInfiniteDuration True if the duration of the alert is infinite
     */
    public void setEnableInfiniteDuration(final boolean enableInfiniteDuration) {
        this.enableInfiniteDuration = enableInfiniteDuration;
    }

    /**
     * Set the alert's listener to be fired on the alert being fully shown
     *
     * @param listener Listener to be fired
     */
    public void setOnShowListener(@NonNull final OnShowAlertListener listener) {
        this.onShowListener = listener;
    }

    /**
     * Set the alert's listener to be fired on the alert being fully hidden
     *
     * @param listener Listener to be fired
     */
    public void setOnHideListener(@NonNull final OnHideAlertListener listener) {
        this.onHideListener = listener;
    }
}
