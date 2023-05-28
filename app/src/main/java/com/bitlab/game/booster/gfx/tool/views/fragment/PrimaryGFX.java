package com.bitlab.game.booster.gfx.tool.views.fragment;

import static android.content.Context.MODE_PRIVATE;
import static android.os.Build.VERSION.SDK_INT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bitlab.game.booster.gfx.tool.Constants;
import com.bitlab.game.booster.gfx.tool.DocHandling.FileHandling;
import com.bitlab.game.booster.gfx.tool.DocHandling.PermissionHandling;
import com.bitlab.game.booster.gfx.tool.R;
import com.bitlab.game.booster.gfx.tool.adapters.SpinnerArray;
import com.bitlab.game.booster.gfx.tool.bottomsheets.ClearDataBottomsheet;
import com.bitlab.game.booster.gfx.tool.bottomsheets.SelectVersionBottomsheet;
import com.bitlab.game.booster.gfx.tool.databinding.FragmentPrimaryGfxBinding;
import com.bitlab.game.booster.gfx.tool.utils.SaveSelectedItems;
import com.bitlab.game.booster.gfx.tool.utils.WaitingDialog;
import com.bitlab.game.booster.gfx.tool.views.activity.ControllerActivity;

import java.util.ArrayList;

public class PrimaryGFX extends Fragment implements AdapterView.OnItemSelectedListener {

    public static FragmentPrimaryGfxBinding binding;
    Context context;
    SharedPreferences sharedPreferences;
    int resolution_int, fps_int, graphics_int, styles_int, sound_int, water_int, shadow_int, detail_int;
    ArrayList<Integer> spinnerItemList = new ArrayList<>();
    WaitingDialog waitingDialog;

    public PrimaryGFX() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPrimaryGfxBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        getButton();
    }

    private void init(){
        context = getContext();

        waitingDialog = new WaitingDialog(context);
        sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                setSpinners();
            }
        }).start();
    }

    private void setSpinners() {
        binding.resolutionSpinner.setAdapter(SpinnerArray.spinnerAttach(context, SpinnerArray.resolution));
        binding.resolutionSpinner.setOnItemSelectedListener(this);

        binding.fpsSpinner.setAdapter(SpinnerArray.spinnerAttach(context, SpinnerArray.fps));
        binding.fpsSpinner.setOnItemSelectedListener(this);

        binding.graphicsSpinner.setAdapter(SpinnerArray.spinnerAttach(context, SpinnerArray.graphics));
        binding.graphicsSpinner.setOnItemSelectedListener(this);

        binding.stylesSpinner.setAdapter(SpinnerArray.spinnerAttach(context, SpinnerArray.styles));
        binding.stylesSpinner.setOnItemSelectedListener(this);

        binding.soundSpinner.setAdapter(SpinnerArray.spinnerAttach(context, SpinnerArray.sound));
        binding.soundSpinner.setOnItemSelectedListener(this);

        binding.waterSpinner.setAdapter(SpinnerArray.spinnerAttach(context, SpinnerArray.water));
        binding.waterSpinner.setOnItemSelectedListener(this);

        binding.shadowSpinner.setAdapter(SpinnerArray.spinnerAttach(context, SpinnerArray.shadow));
        binding.shadowSpinner.setOnItemSelectedListener(this);

        binding.detailSpinner.setAdapter(SpinnerArray.spinnerAttach(context, SpinnerArray.detail));
        binding.detailSpinner.setOnItemSelectedListener(this);

        setSpinnerItems();
    }

    public void setSpinnerItems() {
        ArrayList<Integer> list = SaveSelectedItems.getGameSpinnerItems(context);
        if (!list.isEmpty()) {
            binding.resolutionSpinner.setSelection(Integer.parseInt(list.get(0) + ""));
            binding.fpsSpinner.setSelection(Integer.parseInt(list.get(1) + ""));
            binding.graphicsSpinner.setSelection(Integer.parseInt(list.get(2) + ""));
            binding.stylesSpinner.setSelection(Integer.parseInt(list.get(3) + ""));
            binding.soundSpinner.setSelection(Integer.parseInt(list.get(4) + ""));
            binding.waterSpinner.setSelection(Integer.parseInt(list.get(5) + ""));
            binding.shadowSpinner.setSelection(Integer.parseInt(list.get(6) + ""));
            binding.detailSpinner.setSelection(Integer.parseInt(list.get(7) + ""));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.resolution_spinner:
                resolution_int = binding.resolutionSpinner.getSelectedItemPosition();
                break;
            case R.id.fps_spinner:
                fps_int = binding.fpsSpinner.getSelectedItemPosition();
                break;
            case R.id.graphics_spinner:
                graphics_int = binding.graphicsSpinner.getSelectedItemPosition();
                break;
            case R.id.styles_spinner:
                styles_int = binding.stylesSpinner.getSelectedItemPosition();
                break;
            case R.id.sound_spinner:
                sound_int = binding.soundSpinner.getSelectedItemPosition();
                break;
            case R.id.water_spinner:
                water_int = binding.waterSpinner.getSelectedItemPosition();
                break;
            case R.id.shadow_spinner:
                shadow_int = binding.shadowSpinner.getSelectedItemPosition();
                break;
            case R.id.detail_spinner:
                detail_int = binding.detailSpinner.getSelectedItemPosition();
                break;
        }

        spinnerItemList.add(0, binding.resolutionSpinner.getSelectedItemPosition());
        spinnerItemList.add(1, binding.fpsSpinner.getSelectedItemPosition());
        spinnerItemList.add(2, binding.graphicsSpinner.getSelectedItemPosition());
        spinnerItemList.add(3, binding.stylesSpinner.getSelectedItemPosition());
        spinnerItemList.add(4, binding.soundSpinner.getSelectedItemPosition());
        spinnerItemList.add(5, binding.waterSpinner.getSelectedItemPosition());
        spinnerItemList.add(6, binding.shadowSpinner.getSelectedItemPosition());
        spinnerItemList.add(7, binding.detailSpinner.getSelectedItemPosition());

        SaveSelectedItems.setGameSpinnerItems(context, spinnerItemList);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getButton() {
        binding.primaryApplyButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (!Constants.GAME_PACKAGE_NAME.equals("none")) {
                    if (SDK_INT >= 29) {
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
                        if (PermissionHandling.checkAndRequestPermissions(context)) {
                            applyFiles();
                        }
                    }
                } else {
                    SelectVersionBottomsheet.Show(context);
                }
            }
        });

    }

    private void applyFiles() {
        if (!SecondaryGFX.binding.secondaryApplyButton.getText().equals("CLEAR DATA")) {
            if (binding.primaryApplyButton.getText().equals("APPLY SETTINGS")) {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        waitingDialog.show();
                    }
                });

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new FileHandling(context, Constants.USER_CUSTOM_FILE).write_UserCustom(resolution_int, fps_int, shadow_int, detail_int);
                        new FileHandling(context, Constants.USER_SETTINGS_FILE).write_UserSettings(sound_int, water_int);

                        if (!binding.graphicsSpinner.getSelectedItem().equals("Default"))
                            new FileHandling(context, Constants.ACTIVE_SAV_FILE).CopyRenderQualityFromAssets(binding.graphicsSpinner.getSelectedItem().toString());

                        if (SDK_INT < 33) {
                            FileHandling.RenameFolderPrimary(context);
                        }

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            public void run() {
                                waitingDialog.dismiss();
                                if (SDK_INT < 33) {
                                    binding.primaryApplyButton.setText("CLEARING DATA");
                                    new ClearDataBottomsheet(context).Show(context);
                                } else {
                                    binding.primaryApplyButton.setText("LAUNCH GAME");
                                    binding.primaryApplyButton.setTextColor(context.getColor(R.color.primary));
                                }
                            }
                        });
                    }
                }).start();

            } else if (binding.primaryApplyButton.getText().equals("LAUNCH GAME")) {
                startActivity(context.getPackageManager().getLaunchIntentForPackage(Constants.GAME_PACKAGE_NAME));
            }
        } else {
            //Alerter.create((Activity) context).setText("Apply Advanced Settings First, Properly!").setBackgroundColorRes(R.color.ready).setIcon(R.drawable.ic_warn).show();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (!ClearDataBottomsheet.dialog.isShowing() && binding.primaryApplyButton.getText().equals("CLEARING DATA")) {
                if (SDK_INT < 33) {
                    FileHandling.RenameFolderSecondary(context);
                }
                binding.primaryApplyButton.setText("LAUNCH GAME");
                binding.primaryApplyButton.setTextColor(context.getColor(R.color.primary));
            }
        } catch (Exception e) {

        }
    }

}