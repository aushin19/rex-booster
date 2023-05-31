package com.bitlab.game.booster.gfx.tool.views.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.billingclient.api.SkuDetails;
import com.bitlab.game.booster.gfx.tool.R;
import com.bitlab.game.booster.gfx.tool.billing.IAPBilling;
import com.bitlab.game.booster.gfx.tool.utils.WaitingDialog;
import com.bitlab.game.booster.gfx.tool.utils.setBackground;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Subscription extends AppCompatActivity implements IAPBilling.BillingErrorHandler, IAPBilling.SkuDetailsListener {

    private ConstraintLayout oneMonthCard, threeMonthsCard, oneYearCard;
    private TextView oneMonthPayment, threeMonthsPayment, oneYearPayment;
    private TextView one_month_cost_per_month, three_month_cost_per_month, one_year_cost_per_month;
    private IAPBilling billingClass;
    WaitingDialog waitingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        setBackground.setGradientStatusBar(Subscription.this, getDrawable(R.drawable.subscription_background));

        initComponents();

        initBilling();
        //initClickListeners();

        findViewById(R.id.button_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initBilling(){
        billingClass = new IAPBilling(Subscription.this);
        billingClass.setmCallback(this, this);
        billingClass.startConnection();

        waitingDialog = new WaitingDialog(this);
        waitingDialog.show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void initComponents() {
        oneMonthCard = findViewById(R.id.one_month);
        threeMonthsCard = findViewById(R.id.three_month);
        oneYearCard = findViewById(R.id.one_year);

        //        One Month Card Details
        oneMonthPayment = findViewById(R.id.one_month_payment);
        one_month_cost_per_month = findViewById(R.id.one_month_cost_per_month);

        //        Three Months Card Details
        threeMonthsPayment = findViewById(R.id.three_months_payment);
        three_month_cost_per_month = findViewById(R.id.three_month_cost_per_month);

        //        One Year Card Details
        oneYearPayment = findViewById(R.id.one_year_payment);
        one_year_cost_per_month = findViewById(R.id.one_year_cost_per_month);
    }

    @Override
    public void displayErrorMessage(String message) {
        if (message.equals("done")) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    initClickListeners();
                }
            });
        } else if (message.equals("error")) {
            Toast.makeText(Subscription.this, "Error getting billing services", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Subscription.this, "Error getting billing services", Toast.LENGTH_SHORT).show();
        }
    }

    private void initClickListeners() {
        oneMonthCard.setOnClickListener(view -> {
            try {
                billingClass.purchaseSubscriptionItemByPos(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        threeMonthsCard.setOnClickListener(view -> {
            try {
                billingClass.purchaseSubscriptionItemByPos(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        oneYearCard.setOnClickListener(view -> {
            try {
                billingClass.purchaseSubscriptionItemByPos(2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void subscriptionsDetailList(List<SkuDetails> skuDetailsList) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                try{
                    oneMonthPayment.setText("Total Price " + skuDetailsList.get(0).getPrice());
                    one_month_cost_per_month.setText(convertToSimplePrice(skuDetailsList.get(0).getPrice()));

                    threeMonthsPayment.setText("Total Price " + skuDetailsList.get(2).getPrice());
                    three_month_cost_per_month.setText(DecimalFormat((convertToInt(skuDetailsList.get(2).getOriginalPriceAmountMicros()) / 3.00)));

                    oneYearPayment.setText("Total Price " + skuDetailsList.get(1).getPrice());
                    one_year_cost_per_month.setText(DecimalFormat((convertToInt(skuDetailsList.get(1).getOriginalPriceAmountMicros()) / 12.00)));
                }catch (Exception e){
                    waitingDialog.dismiss();
                }
                waitingDialog.dismiss();
            }
        });
    }

    private String convertToSimplePrice(String price){
        String input = price;
        String pattern = "\\d+\\.\\d+";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(input);
        if (m.find()) {
            String result = m.group(0);
            return result;
        } else {
            return "0";
        }
    }

    private double convertToInt(long price){
        long num = price;
        float dnum = (float) (num / 1000000);
        return dnum;
    }

    public String DecimalFormat(double price) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(price);
    }

}