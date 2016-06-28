package com.zhejunzhu.ucviewpager.weight;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.zhejunzhu.ucviewpager.utils.LLog;
import com.zhejunzhu.ucviewpager.weight.magicadapter.CommonRecyclerAdapter;
import com.zhejunzhu.ucviewpager.weight.magicadapter.CommonViewHolder;

public abstract class TRecyclerViewLayout<TData> extends SwipeRefreshLayout {
    private RecyclerView mRecyclerView;

    private CommonRecyclerAdapter mRecyclerAdapter;

    private LinearLayoutManager mLayoutManager;

    protected boolean mHasMore = true;

    public TRecyclerViewLayout(Context context) {
        super(context);
        initView();
    }

    public TRecyclerViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void initView() {
        LLog.e(this.toString() + "TRecyclerViewLayout initView()");
        setEnabled(true);
        setColorSchemeResources(android.R.color.holo_blue_bright);
        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefreshAndLoad();
            }
        });

        mRecyclerView = new RecyclerView(getContext());
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerAdapter = new CommonRecyclerAdapter<TData>(getContext()) {
            @Override
            public int getLayoutId(TData data, int position) {
                return getItemLayoutId(data, position);
            }

            @Override
            public void bindView(CommonViewHolder viewHolder, TData data, int position) {
                bindItemView(viewHolder, data, position);
            }
        };
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        addView(mRecyclerView, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        protected int mLastVisibleItem;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (mRecyclerView.getAdapter() != null && newState == RecyclerView.SCROLL_STATE_IDLE
                    && mLastVisibleItem == mRecyclerView.getAdapter().getItemCount() - 1 && mHasMore) {
                doLoadMore();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mLastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
        }
    };

    public CommonRecyclerAdapter getRecyclerAdapter() {
        return mRecyclerAdapter;
    }

    public abstract int getItemLayoutId(TData data, int position);

    public abstract void bindItemView(CommonViewHolder viewHolder, TData data, int position);

    public abstract void doRefreshAndLoad();

    public abstract void doLoadMore();
}
