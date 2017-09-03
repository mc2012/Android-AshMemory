package com.mc.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class RemoteService extends Service {
    private static RemoteService mService;

    public RemoteService() {
        mService = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return MemoryManagerService.getInstance();
    }

    public static RemoteService getService() {
        return mService;
    }
}
