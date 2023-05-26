package com.bitlab.game.booster.gfx.tool.adapters;

import static android.content.Context.MODE_PRIVATE;
import static android.os.Build.VERSION.SDK_INT;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitlab.game.booster.gfx.tool.AppConfig;
import com.bitlab.game.booster.gfx.tool.BuildConfig;
import com.bitlab.game.booster.gfx.tool.Constants;
import com.bitlab.game.booster.gfx.tool.DocHandling.PermissionHandling;
import com.bitlab.game.booster.gfx.tool.R;
import com.bitlab.game.booster.gfx.tool.bottomsheets.AskDownloadBottomsheet;
import com.bitlab.game.booster.gfx.tool.bottomsheets.SelectVersionBottomsheet;
import com.bitlab.game.booster.gfx.tool.databinding.GfxFilesListBinding;
import com.bitlab.game.booster.gfx.tool.model.FilesFeedModal;
import com.bitlab.game.booster.gfx.tool.model.SelectedFilesModal;
import com.bitlab.game.booster.gfx.tool.utils.CheckInternetConnection;
import com.bitlab.game.booster.gfx.tool.views.activity.ControllerActivity;
import com.bitlab.game.booster.gfx.tool.views.activity.Subscription;

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
        FilesFeedModal filesFeedModal = filesFeedModalArrayList.get(holder.getAdapterPosition());
        binding.fileTitle.setText(filesFeedModal.title);
        binding.fileDescription.setText(filesFeedModal.description);
        binding.fileExtension.setText(("." + filesFeedModal.extension).toUpperCase());
        binding.gameCompatible.setText(filesFeedModal.gameVersion);

        /*if(isExists(filesFeedModal.title))
            binding.imageButton.setBackground(context.getDrawable(R.drawable.ic_on_switch));*/

        binding.imageButton.setOnClickListener(new View.OnClickListener() {
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

    public static class FeedViewHolder extends RecyclerView.ViewHolder {
        public FeedViewHolder(@NonNull GfxFilesListBinding filesListBinding) {
            super(filesListBinding.getRoot());
        }
    }

    private void buttonAction(View v, FilesFeedModal filesFeedModal, int pos, FeedAdapter.FeedViewHolder holder) {
        if (!AppConfig.isUserPaid) { //Check for Premium Users
            if (isExists(filesFeedModal.title)) { //Check if required file exists
                if (buttonState) {
                    selectedFilesModalArrayList.removeIf(n -> (n.Title.equals(filesFeedModal.title)));
                    v.setBackground(context.getDrawable(R.drawable.ic_off_switch));
                    buttonState = false;
                } else {
                    selectedFilesModalArrayList.add(new SelectedFilesModal(filesFeedModal.fileName, filesFeedModal.title, holder));
                    v.setBackground(context.getDrawable(R.drawable.ic_on_switch));
                    buttonState = true;
                }
            } else {
                downloadFile(filesFeedModal, pos);
            }
        } else {
            context.startActivity(new Intent(context, Subscription.class));
            ((Activity) context).overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        }
    }

    private void downloadFile(FilesFeedModal filesFeedModal, int pos){
        if(CheckInternetConnection.isNetworkConnected(context)){
            AskDownloadBottomsheet.Show(context, filesFeedModal, pos);
        }else{
            Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isExists(String fileName) {
        File file = new File(Environment.getExternalStorageDirectory(), "/Android/data/" + BuildConfig.APPLICATION_ID + "/files/system/" + fileName);
        return file.exists();
    }

}
