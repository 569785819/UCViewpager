package com.zhejunzhu.ucviewpager.stream;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhejunzhu.ucviewpager.R;
import com.zhejunzhu.ucviewpager.utils.LLog;
import com.zhejunzhu.ucviewpager.viewobserver.MainViewPagerChangedObservable;
import com.zhejunzhu.ucviewpager.viewobserver.ProcessViewChangedObserver;
import com.zhejunzhu.ucviewpager.viewobserver.StreamViewChangedObservable;
import com.zhejunzhu.ucviewpager.viewobserver.ViewChangedObservableManager;
import com.zhejunzhu.ucviewpager.weight.MainViewPager;
import com.zhejunzhu.ucviewpager.weight.StreamTabIndicator;
import com.zhejunzhu.ucviewpager.weight.StreamViewpager;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zhejunzhu.ucviewpager.MainTitleViewContainer.sTitleBuleBottomHeight;
import static com.zhejunzhu.ucviewpager.MainTitleViewContainer.sTitleBuleTopHeight;
import static com.zhejunzhu.ucviewpager.MainTitleViewContainer.sTitleLayoutHeight;
import static com.zhejunzhu.ucviewpager.MainTitleViewContainer.sTitleStreamTopHeight;

public class StreamPagerContainer {
    public ViewGroup mContentView;
    @BindView(R.id.streamViewPager)
    StreamViewpager mStreamViewPager;
    @BindView(R.id.streamIndicator)
    StreamTabIndicator mIndicator;
    @BindView(R.id.streamIndicator_layout)
    ViewGroup mIndicatorLayout;

    private StreamPagerAdapter mStreamPagerAdapter;

    private StreamPagerCenterContainer mStreamPagerCenterContainer;

    public StreamPagerContainer() {
        initObserver();
    }

    public View createView(Context context, FragmentManager fragmentManager) {
        if (mContentView == null) {
            mContentView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout
                    .layout_main_pager_stream, null);
            ButterKnife.bind(this, mContentView);
            mStreamPagerCenterContainer = new StreamPagerCenterContainer(mContentView);
            initView(fragmentManager);
        }
        return mContentView;
    }

    public void initView(FragmentManager fragmentManager) {
        mStreamViewPager.setOffscreenPageLimit(0);
        mStreamViewPager.addOnPageChangeListener(mStreamPageChange);
        mStreamPagerAdapter = new StreamPagerAdapter(mStreamViewPager.getContext(), fragmentManager);
        mStreamViewPager.setAdapter(mStreamPagerAdapter);
        mIndicator.setViewPager(mStreamViewPager);
    }

    public void bindMainViewPager(MainViewPager mainViewPager) {
        mainViewPager.setStreamToggledListener(mOnViewPagerStreamToggledListener);
    }

    public void initObserver() {
        MainViewPagerChangedObservable mainViewPagerChangedObservable = ViewChangedObservableManager
                .getInstance(MainViewPagerChangedObservable.class);
        mainViewPagerChangedObservable.addObserver(mMainViewPagerChangedObserver);
        StreamViewChangedObservable streamViewChangedObservable = ViewChangedObservableManager.getInstance
                (StreamViewChangedObservable.class);
        streamViewChangedObservable.addObserver(mStreamViewChangedObserver);
    }

    private MainViewPager.OnViewPagerStreamToggledListener mOnViewPagerStreamToggledListener = new
            MainViewPager.OnViewPagerStreamToggledListener() {
                @Override
                public void onViewPagerStreamToggled(boolean isOpen) {
                    LLog.e("onViewPagerStreamToggled : " + isOpen);
                    mStreamViewPager.setAllowScorll(isOpen);
                    mIndicator.setCanClickable(isOpen);
                    if (isOpen == false) {
                        Fragment fragment = mStreamPagerAdapter.getItem(mStreamViewPager.getCurrentItem());
                        if (fragment instanceof StreamPagerFragment) {
                            ((StreamPagerFragment) fragment).toggleCloseStream();
                        }
                    }
                }
            };

    private ProcessViewChangedObserver mMainViewPagerChangedObserver = new ProcessViewChangedObserver() {
        @Override
        public void doProcess(float process) {
            mStreamViewPager.setY(sTitleLayoutHeight - process * (sTitleBuleTopHeight +
                    sTitleBuleBottomHeight));
        }
    };

    private ProcessViewChangedObserver mStreamViewChangedObserver = new ProcessViewChangedObserver() {
        @Override
        public void doProcess(float process) {
            if (process < 0 || process > 1) {
                return;
            }
            mStreamViewPager.setY(sTitleLayoutHeight - process * (sTitleLayoutHeight -
                    sTitleStreamTopHeight - mIndicatorLayout.getHeight()));
            if (process == 0) {
                mIndicatorLayout.setVisibility(View.GONE);
            } else {
                mIndicatorLayout.setVisibility(View.VISIBLE);
                mIndicatorLayout.setY(sTitleLayoutHeight - process * (sTitleLayoutHeight -
                        sTitleStreamTopHeight));
            }
        }
    };

    private ViewPager.OnPageChangeListener mStreamPageChange = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };
}
