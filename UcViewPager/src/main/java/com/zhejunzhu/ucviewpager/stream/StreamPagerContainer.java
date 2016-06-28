package com.zhejunzhu.ucviewpager.stream;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.TabPageIndicator;
import com.zhejunzhu.ucviewpager.MainTitleViewContainer;
import com.zhejunzhu.ucviewpager.R;
import com.zhejunzhu.ucviewpager.viewobserver.MainViewPagerChangedObservable;
import com.zhejunzhu.ucviewpager.viewobserver.ProcessViewChangedObserver;
import com.zhejunzhu.ucviewpager.viewobserver.StreamViewChangedObservable;
import com.zhejunzhu.ucviewpager.viewobserver.ViewChangedObservableManager;
import com.zhejunzhu.ucviewpager.weight.MainViewPager;
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
    TabPageIndicator mIndicator;

    private StreamPagerAdapter mStreamPagerAdapter;

    public StreamPagerContainer() {
        initObserver();
    }

    public View createView(Context context) {
        if (mContentView == null) {
            mContentView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout
                    .layout_main_pager_stream, null);
            ButterKnife.bind(this, mContentView);
        }
        return mContentView;
    }

    public void init(FragmentManager fragmentManager) {
        mStreamViewPager.setOffscreenPageLimit(0);
        mStreamViewPager.addOnPageChangeListener(mStreamPageChange);
        mStreamPagerAdapter = new StreamPagerAdapter(mStreamViewPager.getContext(), fragmentManager);
        mStreamViewPager.setAdapter(mStreamPagerAdapter);
        mIndicator.setViewPager(mStreamViewPager);
        mStreamViewChangedObserver.doProcess(0);
    }

    public void bindMainViewPager(MainViewPager mainViewPager) {
        mainViewPager.setStreamToggledListener(mOnViewPagerStreamToggledListener);
        mainViewPager.setStreamToggledListener(new MainViewPager.OnViewPagerStreamToggledListener() {
            @Override
            public void onViewPagerStreamToggled(boolean isOpen) {
                mStreamViewPager.setAllowScorll(isOpen);
            }
        });
    }

    public void initObserver() {
        MainViewPagerChangedObservable mainViewPagerChangedObservable = ViewChangedObservableManager.getInstance(MainViewPagerChangedObservable.class);
        mainViewPagerChangedObservable.addObserver(mMainViewPagerChangedObserver);
        StreamViewChangedObservable streamViewChangedObservable = ViewChangedObservableManager.getInstance(StreamViewChangedObservable.class);
        streamViewChangedObservable.addObserver(mStreamViewChangedObserver);
    }

    private MainViewPager.OnViewPagerStreamToggledListener mOnViewPagerStreamToggledListener = new
            MainViewPager.OnViewPagerStreamToggledListener() {
                @Override
                public void onViewPagerStreamToggled(boolean isOpen) {
                    if (isOpen) {
                        mStreamViewPager.setAllowScorll(false);
                    } else {
                        mStreamViewPager.setAllowScorll(true);
                    }
                }
            };

    private ProcessViewChangedObserver mMainViewPagerChangedObserver = new ProcessViewChangedObserver() {
        @Override
        public void doProcess(float process) {
            if (mContentView == null || MainTitleViewContainer.sTitleLayoutHeight <= 0) {
                return;
            }
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
                    sTitleStreamTopHeight - mIndicator.getHeight()));
            if (process == 0) {
                mIndicator.setVisibility(View.GONE);
            } else {
                mIndicator.setVisibility(View.VISIBLE);
                mIndicator.setY(sTitleLayoutHeight - process * (sTitleLayoutHeight - sTitleStreamTopHeight));
            }
        }
    };

    private ViewPager.OnPageChangeListener mStreamPageChange = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (position > 0 && mStreamViewPager.getOffscreenPageLimit() < 5) {
                mStreamViewPager.setOffscreenPageLimit(3);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };
}
