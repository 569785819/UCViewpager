package com.zhejunzhu.ucviewpager.stream;

import android.os.Handler;

import com.zhejunzhu.ucviewpager.utils.LLog;
import com.zhejunzhu.ucviewpager.weight.recyclerview.RecyclerModelImp;
import com.zhejunzhu.ucviewpager.weight.recyclerview.TRefreshRecyclerLayout;

import java.util.ArrayList;
import java.util.Date;

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
