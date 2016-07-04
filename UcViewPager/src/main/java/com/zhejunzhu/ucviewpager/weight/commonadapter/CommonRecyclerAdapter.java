package com.zhejunzhu.ucviewpager.weight.commonadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zhejunzhu.ucviewpager.R;
import com.zhejunzhu.ucviewpager.utils.LLog;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonRecyclerAdapter<TData> extends RecyclerView.Adapter {
    private static final int NORMAL_FOOT_LAYOUT_ID = R.layout.layout_loadmore;

    protected LayoutInflater mInflater;
    protected Context mContext;
    protected final List<TData> mDatas = new ArrayList<>();

    protected boolean mHasFootView = true;
    protected boolean mHasHeadView = false;

    public CommonRecyclerAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void addTopDatas(List<TData> newDatas) {
        synchronized (mDatas) {
            mDatas.addAll(0, newDatas);
        }
    }

    public void addBottomDatas(List<TData> newDatas) {
        synchronized (mDatas) {
            mDatas.addAll(newDatas);
        }
    }

    @Override
    public final CommonViewHolder onCreateViewHolder(ViewGroup parent, int layoutId) {
        CommonViewHolder viewHolder = new CommonViewHolder(mContext, parent, layoutId);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        LLog.v("getItemViewType : " + position);
        if (mHasHeadView && position == 0) {
            return getHeadViewId();
        }
        if (mHasFootView && position == getItemCount() - 1) {
            return getFootViewId();
        }
        return getLayoutId(mDatas.get(position), position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommonViewHolder) {
            LLog.e("onBindViewHolder : " + position);
            if (mHasHeadView && position == 0) {
                bindHeadView(holder);
                return;
            }
            if (mHasFootView && position == getItemCount() - 1) {
                bindFootView(holder);
                return;
            }
            bindView((CommonViewHolder) holder, mDatas.get(position), position);
        } else {
            LLog.e("CommonRecyclerAdapter onBindViewHolder error");
        }
    }

    @Override
    public int getItemCount() {
        int count = mDatas.size();
        if (mHasFootView && count != 0) {
            count++;
        }
        if (mHasHeadView && count != 0) {
            count++;
        }
        return count;
    }

    public int getFootViewPosition() {
        if (mHasFootView) {
            return mDatas.size();
        } else {
            return -1;
        }
    }

    public int getFootViewId() {
        return NORMAL_FOOT_LAYOUT_ID;
    }

    public void bindFootView(RecyclerView.ViewHolder holder) {
    }

    public int getHeadViewId() {
        return NORMAL_FOOT_LAYOUT_ID;
    }

    public void bindHeadView(RecyclerView.ViewHolder holder) {
    }

    public abstract int getLayoutId(TData data, int position);

    public abstract void bindView(CommonViewHolder viewHolder, TData data, int position);
}
