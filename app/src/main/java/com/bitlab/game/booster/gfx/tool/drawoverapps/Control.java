package com.bitlab.game.booster.gfx.tool.drawoverapps;

import android.content.Context;
import android.content.Intent;

public class Control {
    public static void start(Context context) {
        context.startForegroundService(new Intent(context, DrawForegroundService.class));
    }
}
