package com.zhejunzhu.ucviewpager.weight.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.zhejunzhu.ucviewpager.weight.commonadapter.CommonRecyclerAdapter;
import com.zhejunzhu.ucviewpager.weight.commonadapter.CommonViewHolder;
import com.zhejunzhu.ucviewpager.weight.commonadapter.StreamRecyclerAdapter;

import java.util.List;

public abstract class TRefreshRecyclerLayout<TData> extends FrameLayout {
    private StreamRecyclerAdapter mRecyclerAdapter;

    private RecyclerModelImp mRecyclerModel;

    private RefreshRecyclerView mRefreshRecyclerView;

    protected boolean mHasMore = true;

    public TRefreshRecyclerLayout(Context context) {
        super(context);
        initView();
    }

    public TRefreshRecyclerLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void setRecyclerMode(RecyclerModelImp recyclerMode) {
        recyclerMode.bindRefreshRecyclerView(this);
        mRecyclerModel = recyclerMode;
    }

    private void initView() {
        mRefreshRecyclerView = new RefreshRecyclerView(getContext());
        mRecyclerAdapter = new StreamRecyclerAdapter<TData>(getContext()) {
            @Override
            public int getLayoutId(TData data, int position) {
                return getItemLayoutId(data, position);
            }

            @Override
            public void bindView(CommonViewHolder viewHolder, TData data, int position) {
                bindItemView(viewHolder, data, position);
            }
        };
        mRefreshRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRefreshRecyclerView.setAdapter(mRecyclerAdapter);
        mRefreshRecyclerView.addOnScrollListener(mOnScrollListener);
        mRefreshRecyclerView.setOnDragRefreshListener(mOnDragRefreshListener);
        addView(mRefreshRecyclerView);
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        protected int mLastVisibleItem;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (mRecyclerAdapter != null && newState == RecyclerView.SCROLL_STATE_IDLE && mRecyclerAdapter
                    .getFootViewPosition() > 0
                    && mLastVisibleItem == mRecyclerAdapter.getFootViewPosition() && mHasMore) {
                if (mRecyclerModel != null) {
                    mRecyclerModel.loadMore();
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mLastVisibleItem = mRefreshRecyclerView.findLastVisibleItemPosition();
        }
    };

    private RefreshRecyclerView.OnDragRefreshListener mOnDragRefreshListener = new RefreshRecyclerView
            .OnDragRefreshListener() {
        @Override
        public void onDragRefresh() {
            mRecyclerModel.refreshAndLoad();
        }
    };

    public void recivedRefreshAndLoadData(List<TData> newDatas) {
        mRecyclerAdapter.addTopDatas(newDatas);
        mRecyclerAdapter.notifyDataSetChanged();
        mRecyclerModel.endRefreshAndLoading();
        mRefreshRecyclerView.setRefreshEnd(newDatas == null ? 0 : newDatas.size());
    }

    public void recivedLoadmoreData(List<TData> newDatas) {
        mRecyclerModel.endLoadingMore();
        mRecyclerAdapter.addBottomDatas(newDatas);
        mRecyclerAdapter.notifyDataSetChanged();
    }

    public void recivedDataNetError() {
        if (mRecyclerAdapter.getIsEmptyData()) {
            mRecyclerAdapter.setEmptyWithNetError();
        }
        //set View result
    }

    public void recivedServiceError() {
        if (mRecyclerAdapter.getIsEmptyData()) {
            mRecyclerAdapter.setEmptyWithServiceError();
        }
        //set View result
    }

    public void clickToRefresh() {
        mRefreshRecyclerView.scrollToPosition(0);
        mRefreshRecyclerView.doToggleToRefeshing();
    }

    public void goneRefreshStateView() {
        mRefreshRecyclerView.doEndRefreshing();
    }

    public CommonRecyclerAdapter getRecyclerAdapter() {
        return mRecyclerAdapter;
    }

    public abstract int getItemLayoutId(TData data, int position);

    public abstract void bindItemView(CommonViewHolder viewHolder, TData data, int position);
}
