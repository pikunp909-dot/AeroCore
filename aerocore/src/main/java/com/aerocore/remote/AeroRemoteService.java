package com.aerocore.remote;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Binder;
import com.aerocore.util.AeroLog;

public class AeroRemoteService extends Service {
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public AeroRemoteService getService() {
            return AeroRemoteService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AeroLog.i("AeroRemoteService created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AeroLog.i("AeroRemoteService started");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        AeroLog.i("AeroRemoteService destroyed");
        super.onDestroy();
    }
}
