package com.onesys.gfx.tool.game.booster.drawoverapps;

import android.content.Context;
import android.content.Intent;

public class Control {
    public static void start(Context context) {
        Intent intent = new Intent(context, DrawForegroundService.class);
        context.startService(intent);
    }
}
