package com.zhejunzhu.ucviewpager.stream;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhejunzhu.ucviewpager.utils.AndroidUtils;
import com.zhejunzhu.ucviewpager.utils.LLog;

public class StreamPagerFragment extends Fragment {
    private String mStrTab;

    private StreamRefreshRecyclerLayout mRefreshRecyclerLayout;

    private StreamRecyclerMode mStreamRecyclerMode;

    public static StreamPagerFragment newInstance(String tab) {
        StreamPagerFragment fragment = new StreamPagerFragment();
        fragment.mStrTab = tab;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LLog.v("StreamPagerFragment onCreateView : " + mStrTab);
        if (mRefreshRecyclerLayout == null) {
            mRefreshRecyclerLayout = new StreamRefreshRecyclerLayout(getContext());
            mStreamRecyclerMode = new StreamRecyclerMode();
            mRefreshRecyclerLayout.setRecyclerMode(mStreamRecyclerMode);
            //
            mRefreshRecyclerLayout.setPadding(0, 0, 0, AndroidUtils.dip2px(getContext(), 60));
        }

        if (mRefreshRecyclerLayout.getParent() != null) {
            ((ViewGroup) mRefreshRecyclerLayout.getParent()).removeView(mRefreshRecyclerLayout);
        }
        return mRefreshRecyclerLayout;
    }

    @Override
    public void onResume() {
        LLog.v("StreamPagerFragment onResume : " + mStrTab);
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshRecyclerLayout.recivedDataNetError();
            }
        }, 2000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshRecyclerLayout.recivedServiceError();
            }
        }, 12000);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void toggleCloseStream() {
        mRefreshRecyclerLayout.goneRefreshStateView();
    }
}
