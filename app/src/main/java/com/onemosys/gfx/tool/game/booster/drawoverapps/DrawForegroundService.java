package com.onemosys.gfx.tool.game.booster.drawoverapps;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DrawForegroundService extends Service {
    public DrawForegroundService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Window window = new Window(this);
        window.open();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
