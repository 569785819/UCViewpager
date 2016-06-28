package com.zhejunzhu.ucviewpager.viewobserver;

import android.support.v4.view.ViewPager;

import com.zhejunzhu.ucviewpager.weight.MainViewPager;
import com.zhejunzhu.ucviewpager.MainViewPagerAdapter;

public class MainViewPagerChangedObservable extends BaseViewChangedObservable<ProcessViewChangedObserver>
        implements ViewPager.OnPageChangeListener {
    private int mCurrentPageIndex = 0;
    private MainViewPager mViewPager;

    public MainViewPagerChangedObservable() {

    }

    public void setup(MainViewPager viewPager) {
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(this);
    }

    public int getCurrentPageIndex() {
        return mCurrentPageIndex;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mViewPager.isSteamOpened()) {
            return;
        }

        float process = positionOffset;
        if (position == MainViewPagerAdapter.MAIN_PAGER_1) {
            process = (1 - positionOffset);
        }

//        LLog.i("MainViewPagerChangedObservable onProcess : " + process);
        for (ProcessViewChangedObserver observer : mObservers) {
            observer.onProcess(process);
        }
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPageIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
