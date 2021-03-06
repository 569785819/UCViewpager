package com.zhejunzhu.ucviewpager.weight.commonadapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CommonViewHolder extends RecyclerView.ViewHolder {
    private static final int KEY_DATA = "key_data".hashCode();
    private final SparseArray<View> mViews;
    private View mConvertView;

    CommonViewHolder(Context context, ViewGroup parent, int layoutId) {
        super(LayoutInflater.from(context).inflate(layoutId, parent, false));
        this.mViews = new SparseArray<>();
        mConvertView = itemView;
        mConvertView.setTag(this);
    }

    public static CommonViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId) {
        if (convertView == null) {
            return new CommonViewHolder(context, parent, layoutId);
        }
        return (CommonViewHolder) convertView.getTag();
    }

    public View getConvertView() {
        return mConvertView;
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public CommonViewHolder setText(int viewId, int strRes) {
        TextView view = getView(viewId);
        view.setText(view.getContext().getResources().getString(strRes));
        return this;
    }

    public CommonViewHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    public CommonViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }

    public CommonViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    public CommonViewHolder setOnClickListener(int viewId, View.OnClickListener onClickListener) {
        View view = getView(viewId);
        view.setOnClickListener(onClickListener);
        return this;
    }

    public static void setData(View view, Object data) {
        view.setTag(KEY_DATA, data);
    }

    public static Object getData(View view) {
        Object data = view.getTag(KEY_DATA);
        return data;
    }

//    public CommonViewHolder setImageByUrl(int viewId, String url) {
//        return this;
//    }
}
