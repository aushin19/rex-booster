package com.onesys.gfx.tool.game.booster.bottomsheets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.onesys.gfx.tool.game.booster.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class PurchaseSuccessBottomsheet {
    public static BottomSheetDialog dialog;

    public PurchaseSuccessBottomsheet(Context context){
        dialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
    }

    public static void Show(Context context){
        dialog.setCancelable(false);
        dialog.setDismissWithAnimation(true);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.bottomsheet_purchased_successfull, dialog.findViewById(R.id.bottomSheetClearDataContainer));
        dialog.setContentView(dialogView);

        dialog.show();
    }

    public static void dismiss(){
        if (dialog.isShowing())
            dialog.dismiss();
    }
}
