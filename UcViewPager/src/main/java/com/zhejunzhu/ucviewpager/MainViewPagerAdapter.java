package com.zhejunzhu.ucviewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhejunzhu.ucviewpager.stream.StreamPagerContainer;
import com.zhejunzhu.ucviewpager.weight.MainViewPager;

import java.util.HashMap;

public class MainViewPagerAdapter extends PagerAdapter {
    public static final int MAIN_PAGER_COUNT = 2;
    public static final int MAIN_PAGER_0 = 0;
    public static final int MAIN_PAGER_1 = 1;
    public StreamPagerContainer mStreamPagerContainer;
    private Context mContext;
    private HashMap<Integer, View> mPagers = new HashMap<>();

    public MainViewPagerAdapter(Context context) {
        mContext = context;
        mStreamPagerContainer = new StreamPagerContainer();
    }

    @Override
    public int getCount() {
        return MAIN_PAGER_COUNT;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup view, int position, Object object) {
        if (mPagers.get(position) != null) {
            view.removeView(mPagers.get(position));
            mPagers.remove(position);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int position) {
        View childView = null;
        if (mPagers.get(position) == null) {
            childView = generatePager(position, viewGroup);
        } else {
            childView = mPagers.get(position);
        }
        mPagers.put(position, childView);
        viewGroup.addView(childView);
        return childView;
    }

    private View generatePager(int position, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = null;
        if (position == MAIN_PAGER_0) {
            view = mStreamPagerContainer.createView(mContext);
            if (viewGroup instanceof MainViewPager) {
                mStreamPagerContainer.bindMainViewPager((MainViewPager) viewGroup);
            }
        } else if (position == MAIN_PAGER_1) {
            view = inflater.inflate(R.layout.layout_main_pager_grid, null);
        }
        return view;
    }
}
