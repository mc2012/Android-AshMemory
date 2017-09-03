package com.mc.server;

import android.content.Intent;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.mc.manager.MemoryManagerNative;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/8/8.
 */

public class MemoryManagerService extends MemoryManagerNative {
    private static class MemoryManagerServiceHolder {
        public static MemoryManagerService service = new MemoryManagerService();
    }

    public static MemoryManagerService getInstance() {
        return MemoryManagerServiceHolder.service;
    }

    public MemoryFileRecord getParcelFileDescriptor(String fileName) {
        synchronized (mFileMap) {
            return mFileMap.get(fileName);
        }
    }

    private final ArrayMap<String, MemoryFileRecord> mFileMap = new ArrayMap<>();

    @Override
    public void setFile(ParcelFileDescriptor file, String fileName, int len) throws RemoteException {
        synchronized (mFileMap) {
            mFileMap.put(fileName, new MemoryFileRecord(file, fileName, len));
        }
    }

    @Override
    public void startRemoteActivity() throws RemoteException {
        Intent intent = new Intent(RemoteService.getService(), ServerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        RemoteService.getService().startActivity(intent);
    }

    public static class MemoryFileRecord {
        public ParcelFileDescriptor descriptor;
        public String fileName;
        public int len;

        public MemoryFileRecord(ParcelFileDescriptor descriptor, String fileName, int len) {
            this.descriptor = descriptor;
            this.fileName = fileName;
            this.len = len;
        }

        @Override
        public String toString() {
            return "MemoryFileRecord{" +
                    "descriptor=" + descriptor +
                    ", fileName='" + fileName + '\'' +
                    ", len=" + len +
                    '}';
        }
    }
}
