package com.mc.manager;

import android.os.IBinder;
import android.os.IInterface;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;

/**
 * Created by Administrator on 2017/8/8.
 */

public interface IMemoryManager extends IInterface {
    public void setFile(ParcelFileDescriptor file, String fileName, int len) throws RemoteException;

    String descriptor = "com.mc.IMemoryManager";
    static final int SET_FILE_TRANSACTION = IBinder.FIRST_CALL_TRANSACTION;
}
