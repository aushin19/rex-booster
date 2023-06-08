package com.onemosys.gfx.tool.game.booster.bottomsheets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.onemosys.gfx.tool.game.booster.Constants;
import com.onemosys.gfx.tool.game.booster.R;
import com.onemosys.gfx.tool.game.booster.views.activity.ControllerActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class SAFBottomsheet {
    static BottomSheetDialog dialog;

    public SAFBottomsheet(Context context){
        dialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
    }

    public static void Show(Context context, String Permission){
        dialog.setCancelable(false);
        dialog.setDismissWithAnimation(true);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.bottomsheet_saf_grant, dialog.findViewById(R.id.bottomSheetSAFContainer));
        dialog.setContentView(dialogView);

        dialog.findViewById(R.id.saf_ok_BTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(Permission.equals(Constants.DATA_PERMISSION))
                    ControllerActivity.askPermission();
                else
                    ControllerActivity.askPermissionOBB();
            }
        });

        dialog.findViewById(R.id.saf_cancel_BTN).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        dialog.show();
    }

    public static void dismiss(){
        if (dialog.isShowing())
            dialog.dismiss();
    }
}
