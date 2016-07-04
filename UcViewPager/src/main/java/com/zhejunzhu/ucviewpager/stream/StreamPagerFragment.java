package com.zhejunzhu.ucviewpager.stream;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zhejunzhu.ucviewpager.R;
import com.zhejunzhu.ucviewpager.utils.AndroidUtils;
import com.zhejunzhu.ucviewpager.utils.LLog;
import com.zhejunzhu.ucviewpager.weight.commonadapter.CommonViewHolder;
import com.zhejunzhu.ucviewpager.weight.recyclerview.RecyclerModelImp;
import com.zhejunzhu.ucviewpager.weight.recyclerview.TRefreshRecyclerLayout;

import java.util.ArrayList;
import java.util.Date;

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


    public class StreamRefreshRecyclerLayout extends TRefreshRecyclerLayout<Date> {

        public StreamRefreshRecyclerLayout(Context context) {
            super(context);
        }

        @Override
        public int getItemLayoutId(Date date, int position) {
            return R.layout.layout_stream_item;
        }

        @Override
        public void bindItemView(CommonViewHolder viewHolder, Date date, int position) {
            viewHolder.setText(R.id.TextView_content, position + "--" + date.toString());
            View clickView = viewHolder.getView(R.id.View_Item_click);
            CommonViewHolder.setData(clickView, date);
            clickView.setOnClickListener(mClickListener);
        }

        OnClickListener mClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Date data = (Date) CommonViewHolder.getData(v);
                Toast.makeText(v.getContext(), "点击：" + data.toString(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    public class StreamRecyclerMode extends RecyclerModelImp {

        @Override
        public void doRefreshAndLoad(final TRefreshRecyclerLayout refreshRecyclerLayout) {
            LLog.e("StreamRecyclerMode doRefreshAndLoad");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ArrayList<Date> moniDatas = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        moniDatas.add(new Date());
                    }
                    refreshRecyclerLayout.recivedRefreshAndLoadData(moniDatas);
                }
            }, 2000);
        }

        @Override
        public void doLoadMore(final TRefreshRecyclerLayout refreshRecyclerLayout) {
            LLog.e("StreamRecyclerMode doLoadMore");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ArrayList<Date> moniDatas = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        moniDatas.add(new Date());
                    }
                    refreshRecyclerLayout.recivedLoadmoreData(moniDatas);
                }
            }, 2000);
        }
    }
}
