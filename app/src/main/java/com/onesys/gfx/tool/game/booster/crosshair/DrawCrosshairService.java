package com.onesys.gfx.tool.game.booster.crosshair;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DrawCrosshairService extends Service {
    public DrawCrosshairService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        DrawCrosshair drawCrosshair = new DrawCrosshair(this);
        drawCrosshair.open();
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
