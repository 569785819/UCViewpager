package com.zhejunzhu.ucviewpager.stream;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.zhejunzhu.ucviewpager.R;
import com.zhejunzhu.ucviewpager.weight.commonadapter.CommonViewHolder;
import com.zhejunzhu.ucviewpager.weight.recyclerview.TRefreshRecyclerLayout;

import java.util.Date;

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
//            clickToRefresh();
        }
    };
}
