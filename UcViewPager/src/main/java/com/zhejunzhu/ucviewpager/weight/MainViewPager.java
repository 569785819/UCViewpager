package com.zhejunzhu.ucviewpager.weight;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;

import com.zhejunzhu.ucviewpager.utils.AnimConstant;
import com.zhejunzhu.ucviewpager.utils.LLog;
import com.zhejunzhu.ucviewpager.viewobserver.MainViewPagerChangedObservable;
import com.zhejunzhu.ucviewpager.viewobserver.StreamViewChangedObservable;
import com.zhejunzhu.ucviewpager.viewobserver.ViewChangedObservableManager;

public class MainViewPager extends ViewPager {

    private boolean mAnimingTag = false;

    private boolean mSteamOpenedTag = false;

    private boolean mSwitchViewPagerTouch = true;

    private boolean mDoViewPagerTouch = true;

    private StreamViewChangedObservable mStreamViewChangedObservable;

    private float mDownX;

    private float mDownY;

    private OnViewPagerStreamToggledListener mStreamToggledListener;

    private boolean mInterceptTouch = false;

    private Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            mAnimingTag = true;
            LLog.e("toggle start ");
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mAnimingTag = false;
            LLog.e("toggle end ");
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            mAnimingTag = false;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    public MainViewPager(Context context) {
        super(context);
        mStreamViewChangedObservable = ViewChangedObservableManager.getInstance(StreamViewChangedObservable
                .class);
        ViewChangedObservableManager.getInstance(MainViewPagerChangedObservable.class).setup(this);
    }

    public MainViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mStreamViewChangedObservable = ViewChangedObservableManager.getInstance(StreamViewChangedObservable
                .class);
        ViewChangedObservableManager.getInstance(MainViewPagerChangedObservable.class).setup(this);
    }

    public void setStreamToggledListener(OnViewPagerStreamToggledListener mStreamToggledListener) {
        this.mStreamToggledListener = mStreamToggledListener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mInterceptTouch = false;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mInterceptTouch) {
                    mInterceptTouch = false;
                    return true;
                }
                break;
            default:
                break;
        }

        if (mInterceptTouch) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (mAnimingTag) {
            return true;
        }

        if (getCurrentItem() > 0) {
            return super.dispatchTouchEvent(event);
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                LLog.e("dispatchTouchEvent ACTION_DOWN");
                mSwitchViewPagerTouch = true;
                mDoViewPagerTouch = true;
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
//                LLog.e("dispatchTouchEvent ACTION_MOVE");
                if (mSwitchViewPagerTouch) {
                    float mMoveX = event.getX();
                    float mMoveY = event.getY();
                    if (Math.abs(mMoveX - mDownX) + Math.abs(mMoveY - mDownY) < 5) {
                        break;
                    }

                    if (Math.abs(mMoveY - mDownY) > Math.abs(mMoveX - mDownX)) {
                        mDoViewPagerTouch = false;
                    } else {
                        mDoViewPagerTouch = true;
                    }
                    mSwitchViewPagerTouch = false;
                }

                if (mInterceptTouch == false) {
                    float mMoveX = event.getX();
                    float mMoveY = event.getY();
                    if (Math.abs(mMoveX - mDownX) + Math.abs(mMoveY - mDownY) > 5) {
                        mInterceptTouch = true;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
//                LLog.e("dispatchTouchEvent ACTION_CANCEL ACTION_UP");
            default:
                break;
        }

        if (mDoViewPagerTouch || mSteamOpenedTag) {
            return super.dispatchTouchEvent(event);
        } else {
            return onStreamDispatchTouchEvent(event);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDoViewPagerTouch) {
            try {
                return super.onTouchEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean onStreamDispatchTouchEvent(MotionEvent event) {
        if (mStreamViewChangedObservable == null) {
            return super.dispatchTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (mStreamViewChangedObservable.getLastProcess() == 1) {
                    return super.dispatchTouchEvent(event);
                }

                mStreamViewChangedObservable.onViewScrolled((mDownY - event.getY()));
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                float process = mStreamViewChangedObservable.getLastProcess();
                if (process >= 0 && process < 0.2f) {
                    doToggleClose();
                } else if (process >= 0.2f && process <= 1f) {
                    doToggleOpen();
                }

                mSwitchViewPagerTouch = true;
                mDoViewPagerTouch = true;
                super.dispatchTouchEvent(event);
            default:
                break;
        }
        return true;
    }

    private void doToggleOpen() {
        if (mAnimingTag || mStreamViewChangedObservable == null)
            return;
        if (mSteamOpenedTag == false) {
            mSteamOpenedTag = true;
            if (mStreamToggledListener != null) {
                mStreamToggledListener.onViewPagerStreamToggled(true);
            }
        }
        if (mStreamViewChangedObservable.getLastProcess() == 1f) {
            return;
        }
        ObjectAnimator openAnim = ObjectAnimator.ofFloat(mStreamViewChangedObservable, "AnimProcess",
                mStreamViewChangedObservable.getLastProcess(), 1f);
        openAnim.setDuration(AnimConstant.ANIM_DURA_SHORT);
        openAnim.addListener(mAnimatorListener);
        openAnim.setInterpolator(new LinearInterpolator());
        openAnim.start();
    }

    private void doToggleClose() {
        if (mAnimingTag || mStreamViewChangedObservable == null)
            return;
        if (mSteamOpenedTag == true) {
            mSteamOpenedTag = false;
            if (mStreamToggledListener != null) {
                mStreamToggledListener.onViewPagerStreamToggled(false);
            }
        }
        if (mStreamViewChangedObservable.getLastProcess() == 0f) {
            return;
        }
        ObjectAnimator closeAnim = ObjectAnimator.ofFloat(mStreamViewChangedObservable, "AnimProcess",
                mStreamViewChangedObservable.getLastProcess(), 0f);
        closeAnim.setDuration(AnimConstant.ANIM_DURA_SHORT);
        closeAnim.addListener(mAnimatorListener);
        closeAnim.start();
    }

    public boolean onBackPressed() {
        LLog.e("MainViewPager onBackPressed()");
        if (mAnimingTag) {
            return true;
        }
        if (mSteamOpenedTag) {
            doToggleClose();
            return true;
        }
        return false;
    }

    @Override
    public void scrollTo(int x, int y) {
        if (!mSteamOpenedTag) {
            super.scrollTo(x, y);
        }
    }

    public boolean isSteamOpened() {
        return mSteamOpenedTag;
    }

    public static interface OnViewPagerStreamToggledListener {
        public void onViewPagerStreamToggled(boolean isOpen);
    }
}
