package com.tapadoo.alerter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

/**
 * Alert helper class. Will attach a temporary layout to the current activity's content, on top of
 * all other views. It should appear under the status bar.
 *
 * @author Kevin Murphy
 * @since 03/11/2015.
 */
public final class Alerter {

    private static WeakReference<Activity> activityWeakReference;

    private Alert alert;

    /**
     * Constructor
     */
    private Alerter() {
        //Utility classes should not be instantiated
    }

    /**
     * Creates the Alert, and maintains a reference to the calling Activity
     *
     * @param activity The calling Activity
     * @return This Alerter
     */
    public static Alerter create(@NonNull final Activity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("Activity cannot be null!");
        }

        final Alerter alerter = new Alerter();

        //Hide current Alert, if one is active
        Alerter.clearCurrent(activity);

        alerter.setActivity(activity);
        alerter.setAlert(new Alert(activity));

        return alerter;
    }

    /**
     * Cleans up the currently showing alert view, if one is present
     *
     * @param activity The current Activity
     */
    public static void clearCurrent(@NonNull final Activity activity) {
        if (activity == null) {
            return;
        }

        try {
            final ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();

            //Find all Alert Views in Parent layout
            for (int i = 0; i < decorView.getChildCount(); i++) {
                final Alert childView = decorView.getChildAt(i) instanceof Alert ? (Alert) decorView.getChildAt(i) : null;
                if (childView != null && childView.getWindowToken() != null) {
                    ViewCompat.animate(childView).alpha(0).withEndAction(getRemoveViewRunnable(childView));
                }
            }

        } catch (Exception ex) {
            Log.e(Alerter.class.getClass().getSimpleName(), Log.getStackTraceString(ex));
        }
    }

    /**
     * Hides the currently showing alert view, if one is present
     */
    public static void hide() {
        if (activityWeakReference != null && activityWeakReference.get() != null) {
            clearCurrent(activityWeakReference.get());
        }
    }

    @NonNull
    private static Runnable getRemoveViewRunnable(final Alert childView) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    if (childView != null) {
                        ((ViewGroup) childView.getParent()).removeView(childView);
                    }
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), Log.getStackTraceString(e));
                }
            }
        };
    }

    /**
     * Check if an Alert is currently showing
     *
     * @return True if an Alert is showing, false otherwise
     */
    public static boolean isShowing() {
        boolean isShowing = false;
        if (activityWeakReference != null && activityWeakReference.get() != null) {
            isShowing = activityWeakReference.get().findViewById(R.id.flAlertBackground) != null;
        }

        return isShowing;
    }

    /**
     * Shows the Alert, after it's built
     *
     * @return An Alert object check can be altered or hidden
     */
    public Alert show() {
        //This will get the Activity Window's DecorView
        if (getActivityWeakReference() != null) {
            getActivityWeakReference().get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Add the new Alert to the View Hierarchy
                    final ViewGroup decorView = getActivityDecorView();
                    if (decorView != null && getAlert().getParent() == null) {
                        decorView.addView(getAlert());
                    }
                }
            });
        }

        return getAlert();
    }

    /**
     * Sets the title of the Alert
     *
     * @param titleId Title String Resource
     * @return This Alerter
     */
    public Alerter setTitle(@StringRes final int titleId) {
        if (getAlert() != null) {
            getAlert().setTitle(titleId);
        }

        return this;
    }

    /**
     * Set Title of the Alert
     *
     * @param title Title as a String
     * @return This Alerter
     */
    public Alerter setTitle(final String title) {
        if (getAlert() != null) {
            getAlert().setTitle(title);
        }

        return this;
    }

    /**
     * Set the Title's Typeface
     *
     * @param typeface Typeface to use
     * @return This Alerter
     */
    public Alerter setTitleTypeface(@NonNull final Typeface typeface) {
        if (getAlert() != null) {
            getAlert().setTitleTypeface(typeface);
        }

        return this;
    }

    /**
     * Set the Title's text appearance
     *
     * @param textAppearance The style resource id
     * @return This Alerter
     */
    public Alerter setTitleAppearance(@StyleRes final int textAppearance) {
        if (getAlert() != null) {
            getAlert().setTitleAppearance(textAppearance);
        }

        return this;
    }

    /**
     * Set Gravity of the Alert
     *
     * @param gravity Gravity of Alert
     * @return This Alerter
     */
    public Alerter setContentGravity(final int gravity) {
        if (getAlert() != null) {
            getAlert().setContentGravity(gravity);
        }

        return this;
    }

    /**
     * Sets the Alert Text
     *
     * @param textId Text String Resource
     * @return This Alerter
     */
    public Alerter setText(@StringRes final int textId) {
        if (getAlert() != null) {
            getAlert().setText(textId);
        }

        return this;
    }

    /**
     * Sets the Alert Text
     *
     * @param text String of Alert Text
     * @return This Alerter
     */
    public Alerter setText(final String text) {
        if (getAlert() != null) {
            getAlert().setText(text);
        }

        return this;
    }

    /**
     * Set the Text's Typeface
     *
     * @param typeface Typeface to use
     * @return This Alerter
     */
    public Alerter setTextTypeface(@NonNull final Typeface typeface) {
        if (getAlert() != null) {
            getAlert().setTextTypeface(typeface);
        }

        return this;
    }

    /**
     * Set the Text's text appearance
     *
     * @param textAppearance The style resource id
     * @return This Alerter
     */
    public Alerter setTextAppearance(@StyleRes final int textAppearance) {
        if (getAlert() != null) {
            getAlert().setTextAppearance(textAppearance);
        }

        return this;
    }

    /**
     * Set the Alert's Background Colour
     *
     * @param colorInt Colour int value
     * @return This Alerter
     */
    public Alerter setBackgroundColorInt(@ColorInt final int colorInt) {
        if (getAlert() != null) {
            getAlert().setAlertBackgroundColor(colorInt);
        }

        return this;
    }

    /**
     * Set the Alert's Background Colour
     *
     * @param colorResId Colour Resource Id
     * @return This Alerter
     */
    public Alerter setBackgroundColorRes(@ColorRes final int colorResId) {
        if (getAlert() != null && getActivityWeakReference() != null) {
            getAlert().setAlertBackgroundColor(ContextCompat.getColor(getActivityWeakReference().get(), colorResId));
        }

        return this;
    }

    /**
     * Set the Alert's Background Drawable
     *
     * @param drawable Drawable
     * @return This Alerter
     */
    public Alerter setBackgroundDrawable(final Drawable drawable) {
        if (getAlert() != null) {
            getAlert().setAlertBackgroundDrawable(drawable);
        }

        return this;
    }

    /**
     * Set the Alert's Background Drawable Resource
     *
     * @param drawableResId Drawable Resource Id
     * @return This Alerter
     */
    public Alerter setBackgroundResource(@DrawableRes final int drawableResId) {
        if (getAlert() != null) {
            getAlert().setAlertBackgroundResource(drawableResId);
        }

        return this;
    }

    /**
     * Set the Alert's Icon
     *
     * @param iconId The Drawable's Resource Idw
     * @return This Alerter
     */
    public Alerter setIcon(@DrawableRes final int iconId) {
        if (getAlert() != null) {
            getAlert().setIcon(iconId);
        }

        return this;
    }

    /**
     * Set the Alert's Icon
     *
     * @param bitmap The Bitmap object to use for the icon.
     * @return This Alerter
     */
    public Alerter setIcon(@NonNull final Bitmap bitmap) {
        if (getAlert() != null) {
            getAlert().setIcon(bitmap);
        }

        return this;
    }

    /**
     * Set the Alert's Icon
     *
     * @param drawable The Drawable to use for the icon.
     * @return This Alerter
     */
    public Alerter setIcon(@NonNull final Drawable drawable) {
        if (getAlert() != null) {
            getAlert().setIcon(drawable);
        }

        return this;
    }

    /**
     * Set the icon color for the Alert
     *
     * @param color Color int
     * @return This Alerter
     */
    public Alerter setIconColorFilter(@ColorInt final int color) {
        if (getAlert() != null) {
            getAlert().setIconColorFilter(color);
        }

        return this;
    }

    /**
     * Set the icon color for the Alert
     *
     * @param colorFilter ColorFilter
     * @return This Alerter
     */
    public Alerter setIconColorFilter(@NonNull final ColorFilter colorFilter) {
        if (getAlert() != null) {
            getAlert().setIconColorFilter(colorFilter);
        }

        return this;
    }

    /**
     * Set the icon color for the Alert
     *
     * @param color Color int
     * @param mode  PorterDuff.Mode
     * @return This Alerter
     */
    public Alerter setIconColorFilter(@ColorInt final int color, final PorterDuff.Mode mode) {
        if (getAlert() != null) {
            getAlert().setIconColorFilter(color, mode);
        }

        return this;
    }

    /**
     * Hide the Icon
     *
     * @return This Alerter
     */
    public Alerter hideIcon() {
        if (getAlert() != null) {
            getAlert().getIcon().setVisibility(View.GONE);
        }

        return this;
    }

    /**
     * Set the onClickListener for the Alert
     *
     * @param onClickListener The onClickListener for the Alert
     * @return This Alerter
     */
    public Alerter setOnClickListener(@NonNull final View.OnClickListener onClickListener) {
        if (getAlert() != null) {
            getAlert().setOnClickListener(onClickListener);
        }

        return this;
    }

    /**
     * Set if the Alert is dismissable or not
     *
     * @param dismissable true if it can be dismissed
     * @return This Alerter
     */
    public Alerter setDismissable(final boolean dismissable) {
        if (getAlert() != null) {
            getAlert().setDismissable(dismissable);
        }
        return this;
    }

    /**
     * Set the on screen duration of the alert
     *
     * @param milliseconds The duration in milliseconds
     * @return This Alerter
     */
    public Alerter setDuration(@NonNull final long milliseconds) {
        if (getAlert() != null) {
            getAlert().setDuration(milliseconds);
        }
        return this;
    }

    /**
     * Enable or Disable Icon Pulse Animations
     *
     * @param pulse True if the icon should pulse
     * @return This Alerter
     */
    public Alerter enableIconPulse(final boolean pulse) {
        if (getAlert() != null) {
            getAlert().pulseIcon(pulse);
        }
        return this;
    }

    /**
     * Set whether to show the icon in the alert or not
     *
     * @param showIcon True to show the icon, false otherwise
     * @return This Alerter
     */
    public Alerter showIcon(final boolean showIcon) {
        if (getAlert() != null) {
            getAlert().showIcon(showIcon);
        }
        return this;
    }

    /**
     * Enable or disable infinite duration of the alert
     *
     * @param infiniteDuration True if the duration of the alert is infinite
     * @return This Alerter
     */
    public Alerter enableInfiniteDuration(final boolean infiniteDuration) {
        if (getAlert() != null) {
            getAlert().setEnableInfiniteDuration(infiniteDuration);
        }
        return this;
    }

    /**
     * Sets the Alert Shown Listener
     *
     * @param listener OnShowAlertListener of Alert
     * @return This Alerter
     */
    public Alerter setOnShowListener(@NonNull final OnShowAlertListener listener) {
        if (getAlert() != null) {
            getAlert().setOnShowListener(listener);
        }
        return this;
    }

    /**
     * Sets the Alert Hidden Listener
     *
     * @param listener OnHideAlertListener of Alert
     * @return This Alerter
     */
    public Alerter setOnHideListener(@NonNull final OnHideAlertListener listener) {
        if (getAlert() != null) {
            getAlert().setOnHideListener(listener);
        }
        return this;
    }

    /**
     * Enables swipe to dismiss
     *
     * @return This Alerter
     */
    public Alerter enableSwipeToDismiss() {
        if (getAlert() != null) {
            getAlert().enableSwipeToDismiss();
        }
        return this;
    }

    /**
     * Enable or Disable Vibration
     *
     * @param enable True to enable, False to disable
     * @return This Alerter
     */
    public Alerter enableVibration(final boolean enable) {
        if (getAlert() != null) {
            getAlert().setVibrationEnabled(enable);
        }

        return this;
    }

    /**
     * Disable touch events outside of the Alert
     *
     * @return This Alerter
     */
    public Alerter disableOutsideTouch() {
        if (getAlert() != null) {
            getAlert().disableOutsideTouch();
        }

        return this;
    }

    /**
     * Enable or disable progress bar
     *
     * @param enable True to enable, False to disable
     * @return This Alerter
     */
    public Alerter enableProgress(final boolean enable) {
        if (getAlert() != null) {
            getAlert().setEnableProgress(enable);
        }

        return this;
    }

    /**
     * Set the Progress bar color from a color resource
     *
     * @param color The color resource
     * @return This Alerter
     */
    public Alerter setProgressColorRes(@ColorRes final int color) {
        if (getAlert() != null) {
            getAlert().setProgressColorRes(color);
        }

        return this;
    }

    /**
     * Set the Progress bar color from a color resource
     *
     * @param color The color resource
     * @return This Alerter
     */
    public Alerter setProgressColorInt(@ColorInt final int color) {
        if (getAlert() != null) {
            getAlert().setProgressColorInt(color);
        }

        return this;
    }

    /**
     * Gets the Alert associated with the Alerter
     *
     * @return The current Alert
     */
    Alert getAlert() {
        return alert;
    }

    /**
     * Sets the Alert
     *
     * @param alert The Alert to be references and maintained
     */
    private void setAlert(final Alert alert) {
        this.alert = alert;
    }

    @Nullable
    private WeakReference<Activity> getActivityWeakReference() {
        return activityWeakReference;
    }

    /**
     * Get the enclosing Decor View
     *
     * @return The Decor View of the Activity the Alerter was called from
     */
    @Nullable
    ViewGroup getActivityDecorView() {
        ViewGroup decorView = null;

        if (getActivityWeakReference() != null && getActivityWeakReference().get() != null) {
            decorView = (ViewGroup) getActivityWeakReference().get().getWindow().getDecorView();
        }

        return decorView;
    }

    /**
     * Creates a weak reference to the calling Activity
     *
     * @param activity The calling Activity
     */
    private void setActivity(@NonNull final Activity activity) {
        activityWeakReference = new WeakReference<>(activity);
    }
}