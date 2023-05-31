package com.bitlab.game.booster.gfx.tool.views.fragment;

import static android.content.Context.MODE_PRIVATE;
import static android.os.Build.VERSION.SDK_INT;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bitlab.game.booster.gfx.tool.AppConfig;
import com.bitlab.game.booster.gfx.tool.Constants;
import com.bitlab.game.booster.gfx.tool.DocHandling.FileHandling;
import com.bitlab.game.booster.gfx.tool.R;
import com.bitlab.game.booster.gfx.tool.adapters.FeedAdapter;
import com.bitlab.game.booster.gfx.tool.ads.InterstitialAds;
import com.bitlab.game.booster.gfx.tool.bottomsheets.ClearDataBottomsheet;
import com.bitlab.game.booster.gfx.tool.bottomsheets.SelectVersionBottomsheet;
import com.bitlab.game.booster.gfx.tool.databinding.FragmentSecondaryGfxBinding;
import com.bitlab.game.booster.gfx.tool.drawoverapps.Control;
import com.bitlab.game.booster.gfx.tool.drawoverapps.DrawForegroundService;
import com.bitlab.game.booster.gfx.tool.model.SelectedFilesModal;
import com.bitlab.game.booster.gfx.tool.network.GetFilesFeed;
import com.bitlab.game.booster.gfx.tool.utils.WaitingDialog;
import com.bitlab.game.booster.gfx.tool.views.activity.ControllerActivity;

import java.util.ArrayList;

public class SecondaryGFX extends Fragment {

    Context context;
    public static FragmentSecondaryGfxBinding binding;
    SharedPreferences sharedPreferences;
    WaitingDialog waitingDialog;
    public static ArrayList<SelectedFilesModal> selectedFilesArrayList = new ArrayList<>();
    InterstitialAds interstitialAds;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSecondaryGfxBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();

        getFileFeeds();
        getButtonAction();

    }

    private void init(){
        context = getContext();

        interstitialAds = new InterstitialAds(context);

        if(!AppConfig.isUserPaid){
            interstitialAds.loadAd();
        }

        waitingDialog = new WaitingDialog(context);
        sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
    }

    public void getFileFeeds() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.recyclerViewFeed.setLayoutManager(linearLayoutManager);

        new Thread(new Runnable() {
            @Override
            public void run() {
                new GetFilesFeed(context).execute();
            }
        }).start();
    }

    public void getButtonAction() {
        binding.secondaryApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interstitialAds.showAd();
                if(!AppConfig.isUserPaid){
                    interstitialAds.loadAd();
                }
                if (!Constants.GAME_PACKAGE_NAME.equals("none")) {
                    if (!sharedPreferences.getString(Constants.DATA_PERMISSION, "").equals("")) {
                        if (!sharedPreferences.getString(Constants.OBB_PERMISSION, "").equals("")) {
                            applyFiles();
                        } else {
                            ControllerActivity.askPermissionOBB();
                        }
                    } else {
                        ControllerActivity.askPermission();
                    }
                } else {
                    SelectVersionBottomsheet.Show(context);
                }
            }
        });
    }

    private void applyFiles() {
        if(!Settings.canDrawOverlays(context)){
            ControllerActivity.getDrawOverPermission();
        }else{
            if (!Constants.GAME_PACKAGE_NAME.equals("none")) {
                if (!PrimaryGFX.binding.primaryApplyButton.getText().equals("CLEAR DATA")) {
                    if (binding.secondaryApplyButton.getText().equals("APPLY SETTINGS")) {

                        selectedFilesArrayList = FeedAdapter.selectedFilesModalArrayList;

                        if (!selectedFilesArrayList.isEmpty()) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                public void run() {
                                    waitingDialog.show();
                                }
                            });
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i < selectedFilesArrayList.size(); i++) {
                                        if(selectedFilesArrayList.get(i).isBackup){
                                            if(!isMyServiceRunning(DrawForegroundService.class))
                                                Control.start(context);
                                        }
                                        new FileHandling(context, selectedFilesArrayList.get(i).FileName).CopyFilesFromFolder(selectedFilesArrayList.get(i).Title, selectedFilesArrayList.get(i).binding, selectedFilesArrayList.get(i).isBackup);
                                        new FileHandling(context, selectedFilesArrayList.get(i).FileName).CopyFilesFromFolder_InPuffer(selectedFilesArrayList.get(i).Title, selectedFilesArrayList.get(i).binding, selectedFilesArrayList.get(i).isBackup);
                                    }

                                    if (SDK_INT < 33) {
                                        FileHandling.RenameFolderPrimary(context);
                                    }

                                    if(!isMyServiceRunning(DrawForegroundService.class)){
                                        FeedAdapter.selectedFilesModalArrayList.clear();
                                        selectedFilesArrayList.clear();
                                    }

                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        public void run() {
                                            interstitialAds.showAd();
                                            waitingDialog.dismiss();
                                            if (SDK_INT < 33) {
                                                binding.secondaryApplyButton.setText("CLEARING DATA");
                                                new ClearDataBottomsheet(context).Show(context);
                                            } else {
                                                binding.secondaryApplyButton.setText("LAUNCH GAME");
                                                binding.secondaryApplyButton.setTextColor(context.getColor(R.color.primary));
                                            }
                                        }
                                    });
                                }
                            }).start();

                        } else {
                            Toast.makeText(context, "No Files has Selected to Apply", Toast.LENGTH_SHORT).show();
                        }

                    } else if (binding.secondaryApplyButton.getText().equals("LAUNCH GAME")) {
                        String packageName = Constants.GAME_PACKAGE_NAME;
                        startActivity(context.getPackageManager().getLaunchIntentForPackage(packageName));
                    }

                } else {
                    Toast.makeText(context, "Apply Normal Settings First, Properly!", Toast.LENGTH_SHORT).show();
                }
            } else {
                SelectVersionBottomsheet.Show(context);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (!ClearDataBottomsheet.dialog.isShowing() && binding.secondaryApplyButton.getText().equals("CLEARING DATA")) {
                if (SDK_INT < 33) {
                    FileHandling.RenameFolderSecondary(context);
                }
                binding.secondaryApplyButton.setText("LAUNCH GAME");
                binding.secondaryApplyButton.setTextColor(context.getColor(R.color.primary));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}