package com.zhejunzhu.ucviewpager.weight.magicadapter;

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

    public CommonRecyclerAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void addTopDatas(List<TData> newDatas) {
        mDatas.addAll(0, newDatas);
    }

    public void addBottomDatas(List<TData> newDatas) {
        mDatas.addAll(newDatas);
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int layoutId) {
        CommonViewHolder viewHolder = new CommonViewHolder(mContext, parent, layoutId);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        LLog.e("getItemViewType : " + position);
        if (mHasFootView && position == mDatas.size()) {
            return getFootViewId();
        } else {
            return getLayoutId(mDatas.get(position), position);
        }
    }

    public abstract int getLayoutId(TData data, int position);

    public abstract void bindView(CommonViewHolder viewHolder, TData data, int position);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommonViewHolder) {
            LLog.e("onBindViewHolder : " + position);
            if (mHasFootView && position == mDatas.size()) {
                bindFootView(holder);
            } else {
                bindView((CommonViewHolder) holder, mDatas.get(position), position);
            }
        } else {
            LLog.e("CommonRecyclerAdapter onBindViewHolder error");
        }
    }

    @Override
    public int getItemCount() {
        if (mHasFootView) {
            return mDatas.size() == 0 ? 0 : mDatas.size() + 1;
        } else {
            return mDatas.size();
        }
    }

    public boolean isHasFootView() {
        return mHasFootView;
    }

    public void setHasFootView(boolean hasFootView) {
        this.mHasFootView = hasFootView;
    }

    protected int getFootViewPosition() {
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
}
