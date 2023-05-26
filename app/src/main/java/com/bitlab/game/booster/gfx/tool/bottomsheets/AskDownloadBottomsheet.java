package com.bitlab.game.booster.gfx.tool.bottomsheets;

import android.content.Context;
import android.transition.Explode;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;

import com.bitlab.game.booster.gfx.tool.R;
import com.bitlab.game.booster.gfx.tool.databinding.BottomsheetAskDownloadBinding;
import com.bitlab.game.booster.gfx.tool.model.FilesFeedModal;
import com.bitlab.game.booster.gfx.tool.utils.DownloadFiles;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class AskDownloadBottomsheet {
    public static BottomSheetDialog dialog;
    public static BottomsheetAskDownloadBinding binding;

    public static void Show(Context context, FilesFeedModal filesFeedModal, int pos) {
        dialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);

        dialog.setCancelable(false);
        dialog.setDismissWithAnimation(true);
        binding = BottomsheetAskDownloadBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());

        binding.downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(binding.mainLayout, new Explode());

                binding.subText.setVisibility(View.INVISIBLE);
                binding.downloadButton.setVisibility(View.INVISIBLE);
                binding.closeButton.setVisibility(View.INVISIBLE);
                binding.progressBar2.setVisibility(View.VISIBLE);
                binding.titleText.setText("0%");
                new DownloadFiles(filesFeedModal.link,
                        filesFeedModal.title,
                        context,
                        pos
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

    private static void dismiss() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

}
