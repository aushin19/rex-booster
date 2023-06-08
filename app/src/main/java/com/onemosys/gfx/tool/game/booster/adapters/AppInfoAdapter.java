package com.onemosys.gfx.tool.game.booster.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.onemosys.gfx.tool.game.booster.R;
import com.onemosys.gfx.tool.game.booster.databinding.AddAppsListBinding;
import com.onemosys.gfx.tool.game.booster.model.AppInfo;
import com.onemosys.gfx.tool.game.booster.utils.BoosterApps;
import com.onemosys.gfx.tool.game.booster.views.fragment.Dashboard;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoAdapter.AppInfoViewHolder> {

    @SuppressLint("StaticFieldLeak")
    static Context context;
    static ArrayList<AppInfo> selectAppInfoList = new ArrayList<>();
    ArrayList<AppInfo> appInfoArrayList;
    AddAppsListBinding binding;


    public AppInfoAdapter(Context context, ArrayList<AppInfo> appInfoArrayList) {
        AppInfoAdapter.context = context;
        this.appInfoArrayList = appInfoArrayList;
        if(BoosterApps.getBoosterApps(context) != null){
            selectAppInfoList.clear();
            selectAppInfoList.addAll(BoosterApps.getBoosterApps(context));
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public AppInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = AddAppsListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AppInfoViewHolder(binding);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull AppInfoViewHolder holder, int position) {
        AppInfo appInfo = appInfoArrayList.get(holder.getAdapterPosition());
        binding.appName.setText(appInfo.appName);

        try {
            Glide.with(context)
                    .load(context.getPackageManager().getApplicationIcon(appInfo.packageName))
                    .centerCrop()
                    .into(binding.appIcon);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (appInfo.isAdded) {
            binding.button.setBackground(context.getDrawable(R.drawable.ic_on_switch));
            binding.button.setSelected(true);
        } else {
            binding.button.setSelected(false);
            binding.button.setBackground(context.getDrawable(R.drawable.ic_off_switch));
        }

        binding.button.setOnClickListener(v -> {
            buttonAction(v, appInfo);
        });

    }

    private void buttonAction(View v, AppInfo appInfo){
        if (v.isSelected()) {
            v.setSelected(false);
            v.setBackground(context.getDrawable(R.drawable.ic_off_switch));
            selectAppInfoList.removeIf(n -> (n.packageName.equals(appInfo.packageName)));
        }else{
            v.setSelected(true);
            v.setBackground(context.getDrawable(R.drawable.ic_on_switch));
            selectAppInfoList.add(new AppInfo(appInfo.appName, appInfo.packageName, true));
        }
    }

    public static void onBottomSheetDismiss() {
        //Dashboard.updateList(selectAppInfoList);
        Dashboard.getBoostAppsToDashboard(selectAppInfoList);
    }

    @Override
    public int getItemCount() {
        return appInfoArrayList.size();
    }

    public static class AppInfoViewHolder extends RecyclerView.ViewHolder {
        public AppInfoViewHolder(@NonNull AddAppsListBinding binding) {
            super(binding.getRoot());
        }
    }

}
