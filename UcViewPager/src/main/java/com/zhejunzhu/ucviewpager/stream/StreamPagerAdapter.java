package com.zhejunzhu.ucviewpager.stream;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.HashMap;



public class StreamPagerAdapter extends FragmentPagerAdapter {
    public static final String[] TABS = new String[]{"推荐", "视频", "热点", "娱乐", "体育", "深圳", "财经", "科技", "汽车",
            "社会", "搞笑", "军事"};
    private Context mContext;
    private HashMap<Integer, Fragment> mPagers = new HashMap<>();

    public StreamPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public int getCount() {
        return TABS.length;
    }

    @Override
    public Fragment getItem(int position) {
        if (mPagers.get(position) == null) {
            StreamPagerFragment fragment = new StreamPagerFragment().newInstance(TABS[position]);
            mPagers.put(position, fragment);
            return fragment;
        } else {
            return mPagers.get(position);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TABS[position];
    }
}
