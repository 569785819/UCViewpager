package com.zhejunzhu.ucviewpager.weight.commonadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhejunzhu.ucviewpager.R;

import java.util.List;

/**
 * based CommonRecyclerAdapter ,add Stream list empty option
 */
public abstract class StreamRecyclerAdapter<TData> extends CommonRecyclerAdapter<TData> {
    private static final int DEFAULT_EMPTY_COUNT = 10;

    protected boolean mIsEmptyData;

    private EmptyErrorState mEmptyErrorState = EmptyErrorState.NoError;

    enum EmptyErrorState {
        NoError, NetError, ServiceError
    }

    public StreamRecyclerAdapter(Context context) {
        super(context);
        mHasFootView = false;
        mIsEmptyData = true;
    }

    @Override
    public void addBottomDatas(List<TData> newDatas) {
        mHasFootView = true;
        mIsEmptyData = false;
        super.addBottomDatas(newDatas);
    }

    @Override
    public void addTopDatas(List<TData> newDatas) {
        mHasFootView = true;
        mIsEmptyData = false;
        super.addTopDatas(newDatas);
    }

    @Override
    public int getItemCount() {
        if (mIsEmptyData) {
            return DEFAULT_EMPTY_COUNT;
        }
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (mIsEmptyData) {
            return getEmptyLayoutId(position);
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mIsEmptyData) {
            bindEmptyItemView((CommonViewHolder) holder, position);
            return;
        }
        super.onBindViewHolder(holder, position);
    }

    public int getEmptyLayoutId(int position) {
        return R.layout.layout_stream_item_empty;
    }

    protected void bindEmptyItemView(CommonViewHolder holder, int position) {
        if (position == 0) {
            switch (mEmptyErrorState) {
                case NoError:
                    holder.getView(R.id.text_error).setVisibility(View.GONE);
                    break;
                case NetError:
                    holder.setText(R.id.text_error, R.string.stream_refresh_error_net);
                    holder.getView(R.id.text_error).setVisibility(View.VISIBLE);
                    break;
                case ServiceError:
                    holder.setText(R.id.text_error, R.string.stream_refresh_error_fault);
                    holder.getView(R.id.text_error).setVisibility(View.VISIBLE);
                    break;
            }
        } else {
            holder.getView(R.id.text_error).setVisibility(View.GONE);
        }
    }

    public void setEmptyWithNetError() {
        mEmptyErrorState = EmptyErrorState.NetError;
        notifyItemChanged(0);
    }

    public void setEmptyWithServiceError() {
        mEmptyErrorState = EmptyErrorState.ServiceError;
        notifyItemChanged(0);
    }

    public boolean getIsEmptyData() {
        return mIsEmptyData;
    }
}
