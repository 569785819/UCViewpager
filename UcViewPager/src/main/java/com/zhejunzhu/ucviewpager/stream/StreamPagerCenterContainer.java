package com.zhejunzhu.ucviewpager.stream;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.zhejunzhu.ucviewpager.R;
import com.zhejunzhu.ucviewpager.viewobserver.MainViewPagerChangedObservable;
import com.zhejunzhu.ucviewpager.viewobserver.ProcessViewChangedObserver;
import com.zhejunzhu.ucviewpager.viewobserver.StreamViewChangedObservable;
import com.zhejunzhu.ucviewpager.viewobserver.ViewChangedObservableManager;

import static com.zhejunzhu.ucviewpager.MainTitleViewContainer.sTitleBuleBottomHeight;
import static com.zhejunzhu.ucviewpager.MainTitleViewContainer.sTitleBuleTopHeight;
import static com.zhejunzhu.ucviewpager.MainTitleViewContainer.sTitleLayoutHeight;
import static com.zhejunzhu.ucviewpager.MainTitleViewContainer.sTitleStreamTopHeight;

public class StreamPagerCenterContainer {
    public static int sStreamPagerCenterHeight;

    private ViewGroup mView;

    public StreamPagerCenterContainer(ViewGroup parent) {
        mView = (ViewGroup) parent.findViewById(R.id.LayoutCenter);

        initObserver();

        mView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                sStreamPagerCenterHeight = mView.getHeight();
                if (sStreamPagerCenterHeight > 0) {
                    mView.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                return false;
            }
        });

        mView.findViewById(R.id.Button1).setOnClickListener(mOnClickListener);
        mView.findViewById(R.id.Button2).setOnClickListener(mOnClickListener);
        mView.findViewById(R.id.Button3).setOnClickListener(mOnClickListener);
        mView.findViewById(R.id.Button4).setOnClickListener(mOnClickListener);
        mView.findViewById(R.id.Button5).setOnClickListener(mOnClickListener);
        mView.findViewById(R.id.Button6).setOnClickListener(mOnClickListener);
    }

    public void initObserver() {
        MainViewPagerChangedObservable mainViewPagerChangedObservable = ViewChangedObservableManager
                .getInstance(MainViewPagerChangedObservable.class);
        mainViewPagerChangedObservable.addObserver(mMainPagerChangedObserver);
        StreamViewChangedObservable streamViewChangedObservable = ViewChangedObservableManager.getInstance
                (StreamViewChangedObservable.class);
        streamViewChangedObservable.addObserver(mStreamViewChangedObserver);
    }

    private ProcessViewChangedObserver mMainPagerChangedObserver = new ProcessViewChangedObserver() {
        @Override
        public void doProcess(float process) {
            mView.setY(sTitleLayoutHeight - sStreamPagerCenterHeight - process * (sTitleBuleTopHeight +
                    sTitleBuleBottomHeight));
        }
    };

    private ProcessViewChangedObserver mStreamViewChangedObserver = new ProcessViewChangedObserver() {
        @Override
        public void doProcess(float process) {
            if (process < 0 || process > 1) {
                return;
            }
            float moveY = sTitleLayoutHeight - sTitleStreamTopHeight;
            mView.setY(sTitleLayoutHeight - sStreamPagerCenterHeight - process * moveY / 3);
            float scale = 1f - (0.05f * process);
            mView.setScaleX(scale);
        }
    };

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), view.getTag().toString(), Toast.LENGTH_SHORT).show();
        }
    };

    public View getView() {
        return mView;
    }
}
