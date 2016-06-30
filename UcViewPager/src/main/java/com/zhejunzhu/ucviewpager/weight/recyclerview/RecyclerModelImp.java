package com.zhejunzhu.ucviewpager.weight.recyclerview;

public abstract class RecyclerModelImp {
    protected TRefreshRecyclerLayout mRefreshRecyclerLayout;

    protected boolean mIsRefreshAndLoading = false;
    protected boolean mIsLoadingMore = false;

    public RecyclerModelImp() {
    }

    public void bindRefreshRecyclerView(TRefreshRecyclerLayout recyclerView) {
        mRefreshRecyclerLayout = recyclerView;
    }

    public final boolean refreshAndLoad() {
        if (mIsRefreshAndLoading) {
            return false;
        }
        mIsRefreshAndLoading = true;
        doRefreshAndLoad(mRefreshRecyclerLayout);
        return true;
    }

    public final boolean loadMore() {
        if (mIsLoadingMore) {
            return false;
        }
        mIsLoadingMore = true;
        doLoadMore(mRefreshRecyclerLayout);
        return true;
    }

    public void endRefreshAndLoading() {
        mIsRefreshAndLoading = false;
    }

    public void endLoadingMore() {
        mIsLoadingMore = false;
    }

    public abstract void doRefreshAndLoad(TRefreshRecyclerLayout refreshRecyclerLayout);

    public abstract void doLoadMore(TRefreshRecyclerLayout refreshRecyclerLayout);
}
