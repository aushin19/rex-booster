package com.bitlab.game.booster.gfx.tool.bottomsheets;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;

import com.bitlab.game.booster.gfx.tool.Constants;
import com.bitlab.game.booster.gfx.tool.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ClearDataBottomsheet {
    public static BottomSheetDialog dialog;

    public ClearDataBottomsheet(Context context){
        dialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
    }

    public static void Show(Context context){
        dialog.setCancelable(false);
        dialog.setDismissWithAnimation(true);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.bottomsheet_clear_data_dialog, dialog.findViewById(R.id.bottomSheetClearDataContainer));
        dialog.setContentView(dialogView);

        dialog.findViewById(R.id.clear_data_BTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                final Intent i = new Intent();
                i.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                i.setData(Uri.parse("package:" + Constants.GAME_PACKAGE_NAME));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

        dialog.show();
    }

    public static void dismiss(){
        if (dialog.isShowing())
            dialog.dismiss();
    }
}
