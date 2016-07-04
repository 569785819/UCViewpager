package com.zhejunzhu.ucviewpager.weight.recyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class RefreshLayoutManager extends LinearLayoutManager {

    private OverScrollListener mListener;

    public interface OverScrollListener {
        void overScrollBy(int dy);
    }

    public RefreshLayoutManager(Context context) {
        super(context);
    }

    public RefreshLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public RefreshLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return null;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int scrollRange = super.scrollVerticallyBy(dy, recycler, state);

        mListener.overScrollBy(dy - scrollRange);

        return scrollRange;
    }

    public void setOverScrollListener(OverScrollListener listener) {
        mListener = listener;
    }
}
