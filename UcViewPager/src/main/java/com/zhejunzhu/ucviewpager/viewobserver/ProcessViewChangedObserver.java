package com.zhejunzhu.ucviewpager.viewobserver;

public abstract class ProcessViewChangedObserver extends BaseViewChangedObserver {
    protected static final float DEFAULT_MIN_PROCESS_VALUE = 0f;
    protected float lastProcess = -1;

    public void onProcess(float process) {
        if (lastProcess == process) {
            return;
        }

//        if (isProcessIntervalAvaLiable(process)) {
        lastProcess = process;
        doProcess(process);
//        }
    }

    public abstract void doProcess(float process);

    public boolean isProcessIntervalAvaLiable(float process) {
        if (process == 0f || process == 1f) {
            return true;
        }
        if (getMinProcessAvaliable() > 0 && Math.abs(process - lastProcess) < getMinProcessAvaliable()) {
            return false;
        }
        return true;
    }

    protected float getMinProcessAvaliable() {
        return DEFAULT_MIN_PROCESS_VALUE;
    }
}
