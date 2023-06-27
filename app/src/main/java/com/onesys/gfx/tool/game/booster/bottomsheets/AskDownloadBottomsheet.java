package com.onesys.gfx.tool.game.booster.bottomsheets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.transition.Explode;
import androidx.transition.TransitionManager;

import com.onesys.gfx.tool.game.booster.R;
import com.onesys.gfx.tool.game.booster.databinding.BottomsheetAskDownloadBinding;
import com.onesys.gfx.tool.game.booster.model.FilesFeedModal;
import com.onesys.gfx.tool.game.booster.utils.DownloadFiles;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class AskDownloadBottomsheet {
    public static BottomSheetDialog dialog;
    public static BottomsheetAskDownloadBinding binding;
    static String link;

    public static void Show(Context context, FilesFeedModal filesFeedModal, int pos, boolean isBackup) {

        dialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);

        dialog.setCancelable(false);
        dialog.setDismissWithAnimation(true);
        binding = BottomsheetAskDownloadBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());

        if(isBackup){
            link = filesFeedModal.backupLink;
            binding.titleText.setText("Additional Resources");
        }
        else{
            link = filesFeedModal.link;
        }

        binding.downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(binding.mainLayout, new Explode());
                //TransitionManager.beginDelayedTransition(binding.mainLayout, new MaterialSharedAxis(MaterialSharedAxis.Y, true));

                binding.subText.setVisibility(View.INVISIBLE);
                binding.downloadButton.setVisibility(View.INVISIBLE);
                binding.closeButton.setVisibility(View.INVISIBLE);
                binding.progressBar2.setVisibility(View.VISIBLE);
                binding.titleText.setText("0%");

                new DownloadFiles(link,
                        filesFeedModal.title,
                        context,
                        pos,
                        isBackup
                ).execute(filesFeedModal.title);
            }
        });

        binding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        dialog.show();
    }

    public static void dismiss() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

}
