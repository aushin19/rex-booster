package com.onesys.gfx.tool.game.booster.bottomsheets;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.onesys.gfx.tool.game.booster.Constants;
import com.onesys.gfx.tool.game.booster.DocHandling.FileHandling;
import com.onesys.gfx.tool.game.booster.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class SelectVersionBottomsheet {

    public static BottomSheetDialog dialog;

    public static void Show(Context context){
        dialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);

        dialog.setCancelable(true);
        dialog.setDismissWithAnimation(true);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.bottomsheet_game_version, dialog.findViewById(R.id.bottomSheetVersionContainer));
        dialog.setContentView(dialogView);

        TextView game_current_version_TV = dialog.findViewById(R.id.game_current_version_TV);

        game_current_version_TV.setText("Now Supports " + Constants.CURRENT_GAME_VERSION);

        dialog.findViewById(R.id.bgmi_RDIO).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAppInstalled(context, Constants.BGMI);
                dismiss();
            }
        });

        dialog.findViewById(R.id.global_RDIO).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAppInstalled(context, Constants.GL);
                dismiss();
            }
        });

        dialog.findViewById(R.id.kr_RDIO).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAppInstalled(context, Constants.KR);
                dismiss();
            }
        });

        dialog.findViewById(R.id.tw_RDIO).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAppInstalled(context, Constants.TW);
                dismiss();
            }
        });

        dialog.findViewById(R.id.vn_RDIO).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAppInstalled(context, Constants.VN);
                dismiss();
            }
        });

        dialog.show();
    }

    private static void dismiss(){
        if (dialog.isShowing())
            dialog.dismiss();
    }

    private static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            Constants.GAME_PACKAGE_NAME = packageName;
            Constants.DATA_PERMISSION = Constants.DATA_PERMISSION + packageName;
            Constants.OBB_PERMISSION = Constants.OBB_PERMISSION + packageName;
            FileHandling.RenameFolderSecondary(context);
            return true;
        }
        catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(context, "Selected game version is not installed!", Toast.LENGTH_SHORT).show();
            //Constants.GAME_PACKAGE_NAME = packageName;
            //FileHandling.RenameFolderSecondary(context);
            return false;
        }
    }

}
