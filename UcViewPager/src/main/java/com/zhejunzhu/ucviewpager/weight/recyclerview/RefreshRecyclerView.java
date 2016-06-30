package com.zhejunzhu.ucviewpager.weight.recyclerview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.zhejunzhu.ucviewpager.utils.LLog;
import com.zhejunzhu.ucviewpager.utils.PrivateAnimConstant;

public class RefreshRecyclerView extends RecyclerView {
    private RefreshLayoutManager mRefreshLayoutManager;

    private RefreshStateLayout mRefreshStateLayout;

    private boolean mIsOverTop = false;

    private int mOverScrollY = 0;

    private float mLastX = 0;

    private float mLastY = 0;

    private MotionEvent mDownEvent;

    private boolean mTouchDown = false;

    private boolean mIsAniming = false;

    private boolean mIsRefreshing = false;

    private OnDragRefreshListener mOnDragRefreshListener;

    public static interface OnDragRefreshListener {
        public void onDragRefresh();
    }

    public RefreshRecyclerView(Context context) {
        super(context);
        initView();
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        setOverScrollMode(OVER_SCROLL_NEVER);
        mRefreshLayoutManager = new RefreshLayoutManager(getContext());
        mRefreshLayoutManager.setOverScrollListener(mOverScrollListener);
        setLayoutManager(mRefreshLayoutManager);
        setBackgroundColor(Color.parseColor("#ffffff"));

        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                initRefreshStateLayout();
                return false;
            }
        });
    }

    public void setOnDragRefreshListener(OnDragRefreshListener onDragRefreshListener) {
        mOnDragRefreshListener = onDragRefreshListener;
    }

    private void initRefreshStateLayout() {
        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null) {
            mRefreshStateLayout = new RefreshStateLayout(getContext());
            parent.addView(mRefreshStateLayout.getView(), 0);
        }
    }

    public int findLastVisibleItemPosition() {
        return mRefreshLayoutManager.findLastVisibleItemPosition();
    }

    private OverScrollListener mOverScrollListener = new OverScrollListener() {
        @Override
        public void overScrollBy(int dy) {
            if (mIsOverTop == false && mIsRefreshing == false && dy < 0) {
                LLog.i("OverScrollListener overScrollBy");
                mIsOverTop = true;
                mOverScrollY = 0;
            }
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mIsRefreshing || mIsAniming) {
            return super.dispatchTouchEvent(ev);
        }

        float nowX = ev.getRawX();
        float nowY = ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LLog.e("MotionEvent.ACTION_DOWN");
                mTouchDown = true;
                mLastX = nowX;
                mLastY = nowY;
                mDownEvent = MotionEvent.obtain(ev);
                mOverScrollY = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                LLog.e("MotionEvent.ACTION_MOVE");
                float difY = nowY - mLastY;
                mLastX = nowX;
                mLastY = nowY;

                if (mTouchDown == false) {
                    break;
                }

                LLog.v("mOverScrollY : " + mOverScrollY);
                if (mIsOverTop) {
                    if (mDownEvent != null) {
                        mDownEvent.setLocation(mDownEvent.getX(), -1);
                        mDownEvent.setAction(MotionEvent.ACTION_CANCEL);
                        super.dispatchTouchEvent(mDownEvent);
                        mDownEvent = null;
                    }

                    mOverScrollY = (int) (mOverScrollY + difY);
                    if (mOverScrollY < 0) {
                        setOverDragY(0);
                        mIsOverTop = false;
                    } else {
                        setOverDragY(mOverScrollY);
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                LLog.e("MotionEvent.ACTION_UP");
                if (mTouchDown) {
                    mTouchDown = false;
                } else {
                    break;
                }

                if (mIsOverTop && mOverScrollY > 0) {
                    int toggleY = RefreshStateLayout.sHeight;
                    if (mOverScrollY < toggleY) {
                        doToggleToClose();
                    } else {
                        doToggleToRefeshing();
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void doToggleToClose() {
        mIsAniming = true;
        ObjectAnimator end = ObjectAnimator.ofFloat(this, "OverDragY", mOverScrollY, 0);
        end.setDuration(PrivateAnimConstant.ANIM_DURA_SHORT);
        end.addListener(mAnimListener);
        end.start();
    }

    public void doToggleToRefeshing() {
        mIsRefreshing = true;
        mIsAniming = true;
        ObjectAnimator end = ObjectAnimator.ofFloat(this, "OverDragY", mOverScrollY, RefreshStateLayout.sHeight);
        end.setDuration(PrivateAnimConstant.ANIM_DURA_SHORT);
        end.addListener(mAnimListener);
        end.start();
        mRefreshStateLayout.startRefreshAnim();
        if (mOnDragRefreshListener != null) {
            mOnDragRefreshListener.onDragRefresh();
        }
    }

    public void doEndRefreshing() {
        mRefreshStateLayout.endRefreshAnim();
        mIsAniming = true;
        ObjectAnimator end = ObjectAnimator.ofFloat(this, "OverDragY", RefreshStateLayout.sHeight, 0);
        end.setDuration(PrivateAnimConstant.ANIM_DURA_SHORT);
        end.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsAniming = false;
                mRefreshStateLayout.setRefreshState();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mIsAniming = false;
                mRefreshStateLayout.setRefreshState();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        end.start();
    }

    private Animator.AnimatorListener mAnimListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            mIsAniming = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mIsAniming = false;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            mIsAniming = false;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };

    public void setOverDragY(float overDragY) {
        setY(mRefreshStateLayout.setOverDrag((int) overDragY));
    }

    public void setRefreshing(boolean refreshing) {
        if (mIsRefreshing == refreshing) {
            return;
        }

        mIsRefreshing = refreshing;
        if (mIsRefreshing) {
            doToggleToRefeshing();
        } else {
            doEndRefreshing();
        }
    }

    public void setRefreshEnd(int newCount) {
        mIsAniming = true;
        mIsRefreshing = false;
        setOverDragY(RefreshStateLayout.sHeight);
        mRefreshStateLayout.setRefreshCountState(newCount);
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doEndRefreshing();
            }
        }, PrivateAnimConstant.ANIM_DURA_XLONG);
    }
}
