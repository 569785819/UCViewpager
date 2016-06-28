package com.zhejunzhu.ucviewpager.viewobserver;

import com.zhejunzhu.ucviewpager.MainTitleViewContainer;
import com.zhejunzhu.ucviewpager.utils.LLog;

public class StreamViewChangedObservable extends BaseViewChangedObservable<ProcessViewChangedObserver> {

    private float mLastProcess;

    public void onViewScrolled(float distance) {
        float process = distance / MainTitleViewContainer.sTitleLayoutHeight;
        if (process > 1f) {
            process = 1f;
        }

        setAnimProcess(process);
    }

    public float getLastProcess() {
        return mLastProcess;
    }

    public void setAnimProcess(float process) {
        LLog.e("StreamViewChangedObservable setAnimProcess : " + process);
        for (ProcessViewChangedObserver mObserver : mObservers) {
            mObserver.onProcess(process);
        }
        mLastProcess = process;
    }
}
