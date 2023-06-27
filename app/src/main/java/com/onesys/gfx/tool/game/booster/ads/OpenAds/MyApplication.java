package com.onesys.gfx.tool.game.booster.ads.OpenAds;

import android.app.Application;

import com.onesys.gfx.tool.game.booster.AppConfig;
import com.onesys.gfx.tool.game.booster.ads.Ads_Config;
import com.google.android.gms.ads.MobileAds;

import papaya.in.admobopenads.AppOpenManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (!AppConfig.isUserPaid) {
            MobileAds.initialize(this);
            new AppOpenManager(this, Ads_Config.ADMOB_OPEN_AD);
        }

    }
}
