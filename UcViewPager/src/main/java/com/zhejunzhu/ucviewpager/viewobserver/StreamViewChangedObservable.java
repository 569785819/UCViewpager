package com.zhejunzhu.ucviewpager.viewobserver;

import com.zhejunzhu.ucviewpager.MainTitleViewContainer;

public class StreamViewChangedObservable extends BaseViewChangedObservable<ProcessViewChangedObserver> {

    private float mLastProcess;

    public void onViewScrolled(float distance) {
        float process = distance / MainTitleViewContainer.sTitleLayoutHeight;
        if (process > 1f) {
            process = 1f;
        } else if (process < 0) {
            process = 0;
        }

        setAnimProcess(process);
    }

    public float getLastProcess() {
        return mLastProcess;
    }

    public void setAnimProcess(float process) {
//        LLog.e("StreamViewChangedObservable setAnimProcess : " + process);
        for (ProcessViewChangedObserver mObserver : mObservers) {
            mObserver.onProcess(process);
        }
        mLastProcess = process;
    }
}
