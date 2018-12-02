package com.tapadoo.alerter

/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Modifications Copyright (C) 2017 David Kwon
 */

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Build
import androidx.annotation.RequiresApi
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration

/**
 * A [View.OnTouchListener] that makes any [View] dismissable when the
 * user swipes (drags her finger) horizontally across the view.
 *
 * @param mView     The view to make dismissable.
 * @param mCallbacks The callback to trigger when the user has indicated that she would like to
 * dismiss this view.
 */
internal class SwipeDismissTouchListener(
        private val mView: View,
        private val mCallbacks: DismissCallbacks) : View.OnTouchListener {

    // Cached ViewConfiguration and system-wide constant values
    private val mSlop: Int
    private val mMinFlingVelocity: Int
    private val mAnimationTime: Long
    private var mViewWidth = 1 // 1 and not 0 to prevent dividing by zero

    // Transient properties
    private var mDownX: Float = 0.toFloat()
    private var mDownY: Float = 0.toFloat()
    private var mSwiping: Boolean = false
    private var mSwipingSlop: Int = 0
    private var mVelocityTracker: VelocityTracker? = null
    private var mTranslationX: Float = 0.toFloat()

    init {
        val vc = ViewConfiguration.get(mView.context)
        mSlop = vc.scaledTouchSlop
        mMinFlingVelocity = vc.scaledMinimumFlingVelocity * 16
        mAnimationTime = mView.context.resources.getInteger(
                android.R.integer.config_shortAnimTime).toLong()
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        // offset because the view is translated during swipe
        motionEvent.offsetLocation(mTranslationX, 0f)

        if (mViewWidth < 2) {
            mViewWidth = mView.width
        }

        when (motionEvent.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mDownX = motionEvent.rawX
                mDownY = motionEvent.rawY
                if (mCallbacks.canDismiss()) {
                    mVelocityTracker = VelocityTracker.obtain()
                    mVelocityTracker!!.addMovement(motionEvent)
                }
                mCallbacks.onTouch(view, true)
                return false
            }
            MotionEvent.ACTION_UP -> {
                mVelocityTracker?.run {
                    val deltaX = motionEvent.rawX - mDownX
                    this.addMovement(motionEvent)
                    this.computeCurrentVelocity(1000)
                    val velocityX = this.xVelocity
                    val absVelocityX = Math.abs(velocityX)
                    val absVelocityY = Math.abs(this.yVelocity)
                    var dismiss = false
                    var dismissRight = false
                    if (Math.abs(deltaX) > mViewWidth / 2 && mSwiping) {
                        dismiss = true
                        dismissRight = deltaX > 0
                    } else if (mMinFlingVelocity <= absVelocityX && absVelocityY < absVelocityX && mSwiping) {
                        // dismiss only if flinging in the same direction as dragging
                        dismiss = velocityX < 0 == deltaX < 0
                        dismissRight = this.xVelocity > 0
                    }
                    if (dismiss) {
                        // dismiss
                        mView.animate()
                                .translationX((if (dismissRight) mViewWidth else -mViewWidth).toFloat())
                                .alpha(0f)
                                .setDuration(mAnimationTime)
                                .setListener(object : AnimatorListenerAdapter() {
                                    override fun onAnimationEnd(animation: Animator) {
                                        performDismiss()
                                    }
                                })
                    } else if (mSwiping) {
                        // cancel
                        mView.animate()
                                .translationX(0f)
                                .alpha(1f)
                                .setDuration(mAnimationTime)
                                .setListener(null)
                        mCallbacks.onTouch(view, false)
                    }
                    this.recycle()
                    mVelocityTracker = null
                    mTranslationX = 0f
                    mDownX = 0f
                    mDownY = 0f
                    mSwiping = false
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                mVelocityTracker?.run {
                    mView.animate()
                            .translationX(0f)
                            .alpha(1f)
                            .setDuration(mAnimationTime)
                            .setListener(null)
                    this.recycle()
                    mVelocityTracker = null
                    mTranslationX = 0f
                    mDownX = 0f
                    mDownY = 0f
                    mSwiping = false
                }
            }
            MotionEvent.ACTION_MOVE -> {
                mVelocityTracker?.run {
                   this.addMovement(motionEvent)
                    val deltaX = motionEvent.rawX - mDownX
                    val deltaY = motionEvent.rawY - mDownY
                    if (Math.abs(deltaX) > mSlop && Math.abs(deltaY) < Math.abs(deltaX) / 2) {
                        mSwiping = true
                        mSwipingSlop = if (deltaX > 0) mSlop else -mSlop
                        mView.parent.requestDisallowInterceptTouchEvent(true)

                        // Cancel listview's touch
                        val cancelEvent = MotionEvent.obtain(motionEvent)
                        cancelEvent.action = MotionEvent.ACTION_CANCEL or (motionEvent.actionIndex shl MotionEvent.ACTION_POINTER_INDEX_SHIFT)
                        mView.onTouchEvent(cancelEvent)
                        cancelEvent.recycle()
                    }

                    if (mSwiping) {
                        mTranslationX = deltaX
                        mView.translationX = deltaX - mSwipingSlop
                        // TODO: use an ease-out interpolator or such
                        mView.alpha = Math.max(0f, Math.min(1f,
                                1f - 2f * Math.abs(deltaX) / mViewWidth))
                        return true
                    }
                }
            }
            else -> {
                view.performClick()
                return false
            }
        }
        return false
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private fun performDismiss() {
        // Animate the dismissed view to zero-height and then fire the dismiss callback.
        // This triggers layout on each animation frame; in the future we may want to do something
        // smarter and more performant.

        val lp = mView.layoutParams
        val originalHeight = mView.height

        val animator = ValueAnimator.ofInt(originalHeight, 1).setDuration(mAnimationTime)

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mCallbacks.onDismiss(mView)
                // Reset view presentation
                mView.alpha = 1f
                mView.translationX = 0f
                lp.height = originalHeight
                mView.layoutParams = lp
            }
        })

        animator.addUpdateListener { valueAnimator ->
            lp.height = valueAnimator.animatedValue as Int
            mView.layoutParams = lp
        }

        animator.start()
    }

    /**
     * The callback interface used by [SwipeDismissTouchListener] to inform its client
     * about a successful dismissal of the view for which it was created.
     */
    internal interface DismissCallbacks {
        /**
         * Called to determine whether the view can be dismissed.
         *
         * @return boolean The view can dismiss.
         */
        fun canDismiss(): Boolean

        /**
         * Called when the user has indicated they she would like to dismiss the view.
         *
         * @param view  The originating [View]
         */
        fun onDismiss(view: View)

        /**
         * Called when the user touches the view or release the view.
         *
         * @param view  The originating [View]
         * @param touch The view is being touched.
         */
        fun onTouch(view: View, touch: Boolean)
    }
}