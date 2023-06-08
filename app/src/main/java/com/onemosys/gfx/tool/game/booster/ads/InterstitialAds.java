package com.onemosys.gfx.tool.game.booster.ads;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.Map;

public class InterstitialAds {
    Context context;
    private String AD_UNIT_ID = Ads_Config.ADMOB_INTERSTITIAL_AD;
    private static InterstitialAd mInterstitialAd;

    public InterstitialAds(Context context) {
        this.context = context;
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                MobileAds.initialize(context, new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {
                        Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                        for (String adapterClass : statusMap.keySet()) {
                            AdapterStatus status = statusMap.get(adapterClass);
                            Log.d("MyApp", String.format("Adapter name: %s, Description: %s, Latency: %d", adapterClass, status.getDescription(), status.getLatency()));
                        }
                    }
                });
            }
        });

    }

    public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(context, AD_UNIT_ID, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;
            }
        });
    }

    public void showAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show((Activity) context);
        }
    }
}
