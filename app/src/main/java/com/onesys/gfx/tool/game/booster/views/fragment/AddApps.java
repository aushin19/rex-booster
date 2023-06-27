package com.onesys.gfx.tool.game.booster.views.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.transition.TransitionManager;

import com.onesys.gfx.tool.game.booster.R;
import com.onesys.gfx.tool.game.booster.adapters.AppInfoAdapter;
import com.onesys.gfx.tool.game.booster.databinding.FragmentAddAppsBinding;
import com.onesys.gfx.tool.game.booster.model.AppInfo;
import com.onesys.gfx.tool.game.booster.utils.setBackground;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.transition.MaterialSharedAxis;

import java.util.ArrayList;

public class AddApps extends BottomSheetDialogFragment {

    public static ArrayList<AppInfo> appInfoArrayList = new ArrayList<>();
    static Context context;
    static FragmentAddAppsBinding binding;

    public static AddApps newInstance() {
        AddApps fragment = new AddApps();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddAppsBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();
        setBackground.setTransparent((Activity) context);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(!appInfoArrayList.isEmpty())
            AppInfoAdapter.onBottomSheetDismiss();
    }

    public static void createRecyclerView(ArrayList<AppInfo> appList){
        appInfoArrayList = appList;

        binding.addAppsRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        binding.addAppsRecyclerView.setItemViewCacheSize(appList.size());

        binding.addAppsRecyclerView.setAdapter(new AppInfoAdapter(context, appList));
        TransitionManager.beginDelayedTransition(AddApps.binding.mainLayout, new MaterialSharedAxis(MaterialSharedAxis.Y, true));
        AddApps.binding.shimmerAddApps.setVisibility(View.INVISIBLE);
        AddApps.binding.addAppsRecyclerView.setVisibility(View.VISIBLE);
    }
}