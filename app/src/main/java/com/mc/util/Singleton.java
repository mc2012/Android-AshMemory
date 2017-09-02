package com.mc.util;

/**
 * Created by Administrator on 2017/8/8.
 */

public abstract class Singleton<T, M> {
    private T mInstance;

    public T get(M m) {
        synchronized (this) {
            if (mInstance == null) {
                mInstance = create(m);
            }
        }
        return mInstance;
    }

    public abstract T create(M m);
}
