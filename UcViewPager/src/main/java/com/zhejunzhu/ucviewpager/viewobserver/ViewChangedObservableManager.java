package com.zhejunzhu.ucviewpager.viewobserver;

import java.util.HashMap;

public class ViewChangedObservableManager {
    private static final HashMap<String, BaseViewChangedObservable> mObservables = new HashMap<>();

    private ViewChangedObservableManager() {

    }

    public static <T extends BaseViewChangedObservable> T getInstance(Class<T> instanceClass) {
        if (instanceClass == null) {
            return null;
        }
        String key = instanceClass.getSimpleName();
        T observable = (T) mObservables.get(key);
        if (observable == null) {
            try {
                observable = instanceClass.newInstance();
                mObservables.put(key, observable);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return observable;
    }

    public static void clear() {
        for (BaseViewChangedObservable baseViewChangedObservable : mObservables.values()) {
            if (baseViewChangedObservable != null) {
                baseViewChangedObservable.clearObservers();
            }
        }
        mObservables.clear();
    }
}
