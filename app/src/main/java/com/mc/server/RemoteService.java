package com.mc.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class RemoteService extends Service {
    public RemoteService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(this, ServerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return MemoryManagerService.getInstance();
    }
}
