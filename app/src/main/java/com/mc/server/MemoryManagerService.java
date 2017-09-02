package com.mc.server;

import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
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
        Log.d("qmc", "getParcelFileDescriptor " + fileName + mFileMap);
        synchronized (mFileMap) {
            return mFileMap.get(fileName);
        }
    }

    private HashMap<String, MemoryFileRecord> mFileMap = new HashMap<>();

    @Override
    public void setFile(ParcelFileDescriptor file, String fileName, int len) throws RemoteException {
        Log.d("qmc", "setFile " + fileName);
        synchronized (mFileMap) {
            mFileMap.put(fileName, new MemoryFileRecord(file, fileName, len));
        }
        Log.d("qmc", "mFileMap " + mFileMap);
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
