package com.zhejunzhu.ucviewpager;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.zhejunzhu.ucviewpager.viewobserver.ViewChangedObservableManager;
import com.zhejunzhu.ucviewpager.weight.MainViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

public class MainTab {
    @BindView(R.id.main_Viewpager)
    public MainViewPager mMainPager;

    @BindView(R.id.main_title_layout)
    public ViewGroup mTitleLayout;

    @BindView(R.id.indicator)
    public CircleIndicator mIndicator;

    private MainTitleViewContainer mTitleViewContainer;

    private MainViewPagerAdapter mMainViewPagerAdapter;

    private FragmentManager mFragmentManager;

    private Context mContext;

    private ViewGroup mLayout;

    public MainTab(Context context, FragmentManager fragmentManager) {
        mContext = context;
        mFragmentManager = fragmentManager;
        mLayout = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.activity_main_tab, null);

        ButterKnife.bind(this, mLayout);
        initView();
    }

    public View getView() {
        return mLayout;
    }

    private void initView() {
        mMainViewPagerAdapter = new MainViewPagerAdapter(mContext);
        mMainPager.setAdapter(mMainViewPagerAdapter);
        mIndicator.configureIndicator(-1, -1, -1, 0, 0, R.color.theme_blue_dark, R.color.theme_blue);
        mIndicator.setViewPager(mMainPager);

        mTitleViewContainer = new MainTitleViewContainer(mTitleLayout);
        mTitleViewContainer.addLayoutPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (MainTitleViewContainer.sTitleBottomGridHeight > 0) {
                    mTitleViewContainer.removeLayoutPreDrawListener(this);
                    mMainViewPagerAdapter.mStreamPagerContainer.init(mFragmentManager);
                }
                return false;
            }
        });
    }

    public void onBackPressed() {
        if (mMainPager != null && mMainPager.onBackPressed()) {
            return;
        }
    }

    protected void onDestroy() {
        ViewChangedObservableManager.clear();
    }
}
