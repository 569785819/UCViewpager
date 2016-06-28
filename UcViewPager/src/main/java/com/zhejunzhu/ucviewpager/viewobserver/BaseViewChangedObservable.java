package com.zhejunzhu.ucviewpager.viewobserver;

import java.util.LinkedList;
import java.util.List;

public abstract class BaseViewChangedObservable<TObserver extends BaseViewChangedObserver> {

    protected final List<TObserver> mObservers = new LinkedList<>();

    public void addObserver(TObserver observable) {
        if (!mObservers.contains(observable)) {
            mObservers.add(observable);
        }
    }

    public void removeObserver(TObserver observable) {
        mObservers.remove(observable);
    }

    final public void clearObservers() {
        mObservers.clear();
    }
}
