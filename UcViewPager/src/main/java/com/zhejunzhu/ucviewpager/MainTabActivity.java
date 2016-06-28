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
