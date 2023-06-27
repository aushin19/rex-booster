package com.onesys.gfx.tool.game.booster.adapters;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.onesys.gfx.tool.game.booster.databinding.BoosterAppsListBinding;
import com.onesys.gfx.tool.game.booster.model.AppInfo;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BoosterAppsAdapter extends RecyclerView.Adapter<BoosterAppsAdapter.BoosterAppsViewHolder> {

    Context context;
    ArrayList<AppInfo> selectAppInfoList;
    BoosterAppsListBinding binding;
    public BoosterAppsAdapter(Context context, ArrayList<AppInfo> selectAppInfoList) {
        this.context = context;
        this.selectAppInfoList = selectAppInfoList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public BoosterAppsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = BoosterAppsListBinding.inflate(LayoutInflater.from(context), parent, false);
        return new BoosterAppsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BoosterAppsViewHolder holder, int position) {
        AppInfo appInfo = selectAppInfoList.get(position);
        binding.appName.setText(appInfo.appName);

        try {
            Glide.with(context)
                    .load(context.getPackageManager().getApplicationIcon(appInfo.packageName))
                    .centerCrop()
                    .into(binding.appIcon);
        } catch (PackageManager.NameNotFoundException e) {

        }

        binding.parentMaterialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String packageName = appInfo.packageName;
                    context.startActivity(context.getPackageManager().getLaunchIntentForPackage(packageName));
                }catch(Exception e){

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return selectAppInfoList == null ? 0 : selectAppInfoList.size();
    }

    public class BoosterAppsViewHolder extends RecyclerView.ViewHolder {
        public BoosterAppsViewHolder(@NonNull BoosterAppsListBinding binding) {
            super(binding.getRoot());
        }
    }
}
