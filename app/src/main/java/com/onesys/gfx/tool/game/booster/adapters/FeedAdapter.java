package com.onesys.gfx.tool.game.booster.adapters;

import static android.content.Context.MODE_PRIVATE;
import static android.os.Build.VERSION.SDK_INT;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.onesys.gfx.tool.game.booster.AppConfig;
import com.onesys.gfx.tool.game.booster.Constants;
import com.onesys.gfx.tool.game.booster.DocHandling.PermissionHandling;
import com.onesys.gfx.tool.game.booster.R;
import com.onesys.gfx.tool.game.booster.bottomsheets.AskDownloadBottomsheet;
import com.onesys.gfx.tool.game.booster.bottomsheets.SelectVersionBottomsheet;
import com.onesys.gfx.tool.game.booster.databinding.GfxFilesListBinding;
import com.onesys.gfx.tool.game.booster.model.FilesFeedModal;
import com.onesys.gfx.tool.game.booster.model.SelectedFilesModal;
import com.onesys.gfx.tool.game.booster.utils.CheckInternetConnection;
import com.onesys.gfx.tool.game.booster.views.activity.ControllerActivity;
import com.onesys.gfx.tool.game.booster.views.activity.Subscription;

import java.io.File;
import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {
    Context context;
    ArrayList<FilesFeedModal> filesFeedModalArrayList;
    GfxFilesListBinding binding;
    boolean buttonState = false;
    SharedPreferences sharedPreferences;
    public static ArrayList<SelectedFilesModal> selectedFilesModalArrayList;

    public FeedAdapter(Context context, ArrayList<FilesFeedModal> filesFeedModalArrayList) {
        this.context = context;
        this.filesFeedModalArrayList = filesFeedModalArrayList;
        selectedFilesModalArrayList = new ArrayList<>();
        sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = GfxFilesListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FeedViewHolder(binding);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        FilesFeedModal filesFeedModal = filesFeedModalArrayList.get(position);
        binding.fileTitle.setText(filesFeedModal.title);
        binding.fileDescription.setText(filesFeedModal.description);
        binding.fileExtension.setText(("." + filesFeedModal.extension).toUpperCase());
        binding.gameCompatible.setText(filesFeedModal.gameVersion);


        if (!filesFeedModal.backupLink.equals("null")) {
            if (!isBackupFileExists(filesFeedModal.title)) {
                binding.additionRequirements.setVisibility(View.VISIBLE);
            }
        }


        binding.applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Constants.GAME_PACKAGE_NAME.equals("none")) {
                    if (SDK_INT >= 29) {
                        if (!sharedPreferences.getString(Constants.DATA_PERMISSION, "").equals("")) {
                            if (!sharedPreferences.getString(Constants.OBB_PERMISSION, "").equals("")) {
                                buttonAction(v, filesFeedModal, holder.getAdapterPosition(), holder);
                            } else {
                                ControllerActivity.askPermissionOBB();
                            }
                        } else {
                            ControllerActivity.askPermission();
                        }
                    } else {
                        if (PermissionHandling.checkAndRequestPermissions(context)) {
                            buttonAction(v, filesFeedModal, holder.getAdapterPosition(), holder);
                        }
                    }
                } else {
                    SelectVersionBottomsheet.Show(context);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filesFeedModalArrayList.size();
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder {
        public FeedViewHolder(@NonNull GfxFilesListBinding filesListBinding) {
            super(filesListBinding.getRoot());
        }
    }

    private void buttonAction(View v, FilesFeedModal filesFeedModal, int pos, FeedAdapter.FeedViewHolder holder) {
        if (!AppConfig.isUserPaid) {
            if (isServiceFileExists(filesFeedModal.title)) {
                if (!filesFeedModal.backupLink.equals("null")) {
                    if (isBackupFileExists(filesFeedModal.title)) {
                        if (buttonState) {
                            selectedFilesModalArrayList.removeIf(n -> (n.Title.equals(filesFeedModal.title)));
                            v.setBackground(context.getDrawable(R.drawable.ic_off_switch));
                            buttonState = false;
                        } else {
                            selectedFilesModalArrayList.add(new SelectedFilesModal(filesFeedModal.fileName, filesFeedModal.title, true, binding));
                            v.setBackground(context.getDrawable(R.drawable.ic_on_switch));
                            buttonState = true;
                        }
                    } else {
                        downloadFile(filesFeedModal, pos, true);
                    }
                } else {
                    if (buttonState) {
                        selectedFilesModalArrayList.removeIf(n -> (n.Title.equals(filesFeedModal.title)));
                        v.setBackground(context.getDrawable(R.drawable.ic_off_switch));
                        buttonState = false;
                    } else {
                        selectedFilesModalArrayList.add(new SelectedFilesModal(filesFeedModal.fileName, filesFeedModal.title, false, binding));
                        v.setBackground(context.getDrawable(R.drawable.ic_on_switch));
                        buttonState = true;
                    }
                }

            } else {
                downloadFile(filesFeedModal, pos, false);
            }
        } else {
            context.startActivity(new Intent(context, Subscription.class));
            ((Activity) context).overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        }
    }

    private void downloadFile(FilesFeedModal filesFeedModal, int pos, boolean isBackup) {
        if (CheckInternetConnection.isNetworkConnected(context)) {
            AskDownloadBottomsheet.Show(context, filesFeedModal, pos, isBackup);
        } else {
            Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isServiceFileExists(String fileName) {
        File file = new File(Constants.DOWNLOAD_PATH + Constants.SERVICE_FILES_PATH + fileName);
        return file.exists();
    }

    private boolean isBackupFileExists(String fileName) {
        File file = new File(Constants.DOWNLOAD_PATH + Constants.BACKUP_PATH + fileName);
        return file.exists();
    }

}
