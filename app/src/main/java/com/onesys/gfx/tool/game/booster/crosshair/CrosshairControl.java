package com.onesys.gfx.tool.game.booster.crosshair;

import android.content.Context;
import android.content.Intent;

public class CrosshairControl {
    public static void start(Context context) {
        Intent intent = new Intent(context, DrawCrosshairService.class);
        context.startService(intent);
    }
}
