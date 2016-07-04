package com.zhejunzhu.ucviewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

public class MainTabActivity extends FragmentActivity {
    MainTab mMainTab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainTab = new MainTab(this, getSupportFragmentManager());
        setContentView(mMainTab.getView());

//        //test fragment stream
//        ViewGroup view = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_main_tab, null);
//        view.removeAllViews();
//        setContentView(view);
//
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction transaction = fm.beginTransaction();
//        StreamPagerFragment page = StreamPagerFragment.newInstance("推荐");
//        transaction.replace(R.id.main_layout, page);
//        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        mMainTab.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainTab.onDestroy();
    }
}
