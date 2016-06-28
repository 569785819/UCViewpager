package com.zhejunzhu.ucviewpager.weight;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class StreamViewpager extends ViewPager {

    private boolean mAllowScorll = false;

    public StreamViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public StreamViewpager(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        try {
            if (mAllowScorll) {
                return super.onInterceptTouchEvent(arg0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mAllowScorll) {
            return super.dispatchTouchEvent(ev);
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (mAllowScorll)
            return super.onTouchEvent(arg0);
        else
            return false;
    }


    public void setAllowScorll(boolean allowScroll) {
        this.mAllowScorll = allowScroll;
    }
}
