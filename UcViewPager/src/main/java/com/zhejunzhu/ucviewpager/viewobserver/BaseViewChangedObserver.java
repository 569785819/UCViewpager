package com.zhejunzhu.ucviewpager.viewobserver;

public abstract class BaseViewChangedObserver {

    public void registerObservable(BaseViewChangedObservable observable) {
        observable.addObserver(this);
    }

    public void unregisterObservable(BaseViewChangedObservable observable) {
        observable.removeObserver(this);
    }
}
