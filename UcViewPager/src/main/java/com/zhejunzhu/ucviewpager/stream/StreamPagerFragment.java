package com.zhejunzhu.ucviewpager.stream;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zhejunzhu.ucviewpager.R;
import com.zhejunzhu.ucviewpager.utils.AndroidUtils;
import com.zhejunzhu.ucviewpager.utils.LLog;
import com.zhejunzhu.ucviewpager.weight.TRecyclerViewLayout;
import com.zhejunzhu.ucviewpager.weight.magicadapter.CommonViewHolder;

import java.util.ArrayList;
import java.util.Date;

public class StreamPagerFragment extends Fragment {
    private String mStrTab;

    private ViewGroup mContentView;

    private StreamRecyclerLayout mStreamRecyclerLayout;

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
        if (mContentView == null) {
            mContentView = new FrameLayout(getContext());
            mContentView.setPadding(0, 0, 0, AndroidUtils.dip2px(getContext(), 60));
            mStreamRecyclerLayout = new StreamRecyclerLayout(getContext());
            mContentView.addView(mStreamRecyclerLayout);
        }

        if (mContentView.getParent() != null) {
            ((ViewGroup) mContentView.getParent()).removeView(mContentView);
        }
        return mContentView;
    }

    @Override
    public void onResume() {
        LLog.v("StreamPagerFragment onResume : " + mStrTab);
        mStreamRecyclerLayout.doLoadMore();
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    public class StreamRecyclerLayout extends TRecyclerViewLayout<Date> {

        public StreamRecyclerLayout(Context context) {
            super(context);
        }

        @Override
        public int getItemLayoutId(Date date, int position) {
            return R.layout.layout_stream_item;
        }

        @Override
        public void bindItemView(CommonViewHolder viewHolder, Date date, int position) {
            viewHolder.setText(R.id.TextView_time, date.toString());
        }

        @Override
        public void doRefreshAndLoad() {
            LLog.e("StreamRecyclerLayout doRefreshAndLoad()");
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    ArrayList<Date> moniDatas = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        moniDatas.add(new Date());
                    }
                    mStreamRecyclerLayout.getRecyclerAdapter().addTopDatas(moniDatas);
                    mStreamRecyclerLayout.getRecyclerAdapter().notifyDataSetChanged();
                    mStreamRecyclerLayout.setRefreshing(false);
                }
            }, 3000);
        }

        @Override
        public void doLoadMore() {
            LLog.e("StreamRecyclerLayout doLoadMore()");
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    ArrayList<java.lang.Object> moniDatas = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        moniDatas.add(new Date());
                    }
                    mStreamRecyclerLayout.getRecyclerAdapter().addBottomDatas(moniDatas);
                    mStreamRecyclerLayout.getRecyclerAdapter().notifyDataSetChanged();
                }
            }, 2000);
        }
    }
}
