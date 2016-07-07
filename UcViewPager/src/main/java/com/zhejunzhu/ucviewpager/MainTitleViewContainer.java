package com.zhejunzhu.ucviewpager;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.zhejunzhu.ucviewpager.stream.StreamPagerCenterContainer;
import com.zhejunzhu.ucviewpager.utils.AndroidUtils;
import com.zhejunzhu.ucviewpager.viewobserver.MainViewPagerChangedObservable;
import com.zhejunzhu.ucviewpager.viewobserver.ProcessViewChangedObserver;
import com.zhejunzhu.ucviewpager.viewobserver.StreamViewChangedObservable;
import com.zhejunzhu.ucviewpager.viewobserver.ViewChangedObservableManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainTitleViewContainer {
    public static int sTitleLayoutHeight;
    public static int sTitleStreamTopHeight;
    public static int sTitleBuleTopHeight;
    public static int sTitleBuleBottomHeight;
    public static int sTitleShadowHeight;

    @BindView(R.id.title_content_layout)
    public View mTitleContentLayout;
    @BindView(R.id.title_blue_layout)
    public View mBlueLayout;
    @BindView(R.id.title_top)
    public View mBlueTop;
    @BindView(R.id.title_search_layout)
    public View mSearchLayout;
    @BindView(R.id.title_bottom)
    public View mBlueBottom;
    @BindView(R.id.title_blue_bg)
    public View mBlueLayoutBg;
    @BindView(R.id.title_search_bg)
    public View mSearchLayoutBg;
    @BindView(R.id.title_stream_top)
    public View mSearchStreamTopLayout;
    @BindView(R.id.title_dark_shadow)
    public View mTitleDarkShadow;
    @BindView(R.id.title_shadow_top)
    public View mTitleShadowTop;
    @BindView(R.id.title_shadow_bottom)
    public View mTitleShadowBottom;

    private ViewGroup mContentView;

    private int mSearchViewMargin;

    public MainTitleViewContainer(ViewGroup titleView) {
        ButterKnife.bind(this, titleView);
        mContentView = titleView;
        mSearchViewMargin = AndroidUtils.dip2px(titleView.getContext(), 15f);

        resizeBottomBgHeight();
        initObserver();
    }

    private void resizeBottomBgHeight() {
        mBlueLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int height = mBlueLayout.getHeight();
                if (height > 0 && StreamPagerCenterContainer.sStreamPagerCenterHeight > 0) {
                    mBlueLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                    initFirstValues();

                    MainViewPagerChangedObservable mainViewPagerChangedObservable =
                            ViewChangedObservableManager.getInstance(MainViewPagerChangedObservable.class);
                    mainViewPagerChangedObservable.onPageScrolled(0, 0.1f, 0);
                    mainViewPagerChangedObservable.onPageScrolled(0, 0, 0);
                }
                return true;
            }
        });
    }

    public void initFirstValues() {
        ViewGroup.LayoutParams blueLp = mBlueLayoutBg.getLayoutParams();
        blueLp.height = mBlueLayout.getHeight();
        mBlueLayoutBg.setLayoutParams(blueLp);

        sTitleLayoutHeight = mBlueLayout.getHeight() + StreamPagerCenterContainer.sStreamPagerCenterHeight;

        ViewGroup.LayoutParams darkLp = mTitleDarkShadow.getLayoutParams();
        darkLp.height = sTitleLayoutHeight;
        mTitleDarkShadow.setLayoutParams(darkLp);


        sTitleStreamTopHeight = mSearchStreamTopLayout.getHeight();
        sTitleBuleBottomHeight = mBlueBottom.getHeight();
        sTitleBuleTopHeight = mBlueTop.getHeight();
        sTitleShadowHeight = mTitleShadowTop.getHeight();

        mSearchStreamTopLayout.setY(-sTitleStreamTopHeight);
        mTitleDarkShadow.setVisibility(View.INVISIBLE);
        mTitleShadowBottom.setVisibility(View.GONE);
        mTitleShadowTop.setVisibility(View.GONE);
    }

    public void initObserver() {
        MainViewPagerChangedObservable mainViewPagerChangedObservable = ViewChangedObservableManager
                .getInstance(MainViewPagerChangedObservable.class);
        mainViewPagerChangedObservable.addObserver(mMainPagerChangedObserver);
        StreamViewChangedObservable streamViewChangedObservable = ViewChangedObservableManager.getInstance
                (StreamViewChangedObservable.class);
        streamViewChangedObservable.addObserver(mStreamViewChangedObserver);
    }

    private ProcessViewChangedObserver mStreamViewChangedObserver = new ProcessViewChangedObserver() {
        @Override
        public void doProcess(float process) {
            if (process < 0 || process > 1) {
                return;
            }
            float moveY = sTitleLayoutHeight - sTitleStreamTopHeight;
            mSearchStreamTopLayout.setY((process - 1) * mSearchStreamTopLayout.getHeight());
            mTitleContentLayout.setY(-process * moveY / 3);

            ViewGroup.LayoutParams lp = mContentView.getLayoutParams();
            lp.height = (int) (sTitleLayoutHeight - process * moveY * 2 / 3);
            mContentView.setLayoutParams(lp);

            float scale = 1f - (0.05f * process);
            mBlueTop.setScaleX(scale);
            mSearchLayout.setScaleX(scale);
            mBlueBottom.setScaleX(scale);

            setShadowProcess(process);
        }
    };

    public void setShadowProcess(float process) {
        if (process == 0 || process == 1) {
            mTitleShadowTop.setVisibility(View.GONE);
            mTitleShadowBottom.setVisibility(View.GONE);
            mTitleDarkShadow.setVisibility(View.GONE);
        } else {
            if (mTitleShadowTop.getVisibility() == View.GONE) {
                mTitleShadowTop.setVisibility(View.VISIBLE);
                mTitleShadowBottom.setVisibility(View.VISIBLE);
                mTitleDarkShadow.setVisibility(View.VISIBLE);
            }
            float moveY = sTitleLayoutHeight - sTitleStreamTopHeight;
            mTitleDarkShadow.setAlpha(process);
            mTitleShadowTop.setAlpha(process);
            mTitleShadowBottom.setAlpha(process);
            mTitleDarkShadow.setY(-process * moveY);
            float bottomIndex = sTitleLayoutHeight - process * moveY;
            float bottomShadowY = sTitleLayoutHeight - mTitleShadowBottom.getHeight() - process * moveY;
            float topShadowY = process * sTitleStreamTopHeight;
            topShadowY = topShadowY + sTitleShadowHeight > bottomIndex ? bottomIndex - sTitleShadowHeight :
                    topShadowY;
            mTitleShadowTop.setY(topShadowY);
            mTitleShadowBottom.setY(bottomShadowY);
        }
    }

    private ProcessViewChangedObserver mMainPagerChangedObserver = new ProcessViewChangedObserver() {
        @Override
        public void doProcess(float process) {
            if (process == 1) {
                mContentView.setVisibility(View.GONE);
            } else {
                mContentView.setVisibility(View.VISIBLE);
            }
            mContentView.setY(-process * mBlueTop.getHeight());
            mBlueLayoutBg.setY(-process * mBlueBottom.getHeight());
            mSearchLayoutBg.setAlpha(1 - process);
            mBlueBottom.setAlpha(1 - process * 2.5f);

            int padding = (int) ((1 - process) * mSearchViewMargin);
            mSearchLayout.setPadding(padding, 0, padding, 0);
        }
    };
}
