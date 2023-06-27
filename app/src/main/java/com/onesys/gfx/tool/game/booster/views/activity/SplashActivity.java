package com.onesys.gfx.tool.game.booster.views.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.SkuDetails;
import com.onesys.gfx.tool.game.booster.AppConfig;
import com.onesys.gfx.tool.game.booster.R;
import com.onesys.gfx.tool.game.booster.billing.IAPBilling;

import java.util.List;

public class SplashActivity extends AppCompatActivity implements IAPBilling.BillingErrorHandler, IAPBilling.SkuDetailsListener {

    IAPBilling iapBilling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initBilling();
    }

    private void initBilling(){
        iapBilling = new IAPBilling(SplashActivity.this);
        iapBilling.setmCallback( this, this);
        iapBilling.startConnection();
    }

    @Override
    public void displayErrorMessage(String message) {
        if(message.equalsIgnoreCase("done")){
            iapBilling.isSubscribedToSubscriptionItem(this);
        }
        else{
            AppConfig.isUserPaid = false;
            startActivity(new Intent(SplashActivity.this, ControllerActivity.class));
            finish();
        }
    }

    @Override
    public void subscriptionsDetailList(List<SkuDetails> skuDetailsList) {

    }
}