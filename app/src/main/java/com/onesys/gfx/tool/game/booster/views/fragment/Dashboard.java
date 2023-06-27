package com.onesys.gfx.tool.game.booster.views.fragment;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.google.android.material.transition.MaterialSharedAxis;
import com.onesys.gfx.tool.game.booster.adapters.BoosterAppsAdapter;
import com.onesys.gfx.tool.game.booster.databinding.FragmentDashboardBinding;
import com.onesys.gfx.tool.game.booster.model.AppInfo;
import com.onesys.gfx.tool.game.booster.utils.BoosterApps;
import com.onesys.gfx.tool.game.booster.utils.GetUserAppList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Dashboard extends Fragment{
    public static FragmentDashboardBinding binding;
    static Context context;
    public static BoosterAppsAdapter boosterAppsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();

        new Thread(new Runnable() {
            @Override
            public void run() {
                updateInfo();
            }
        }).start();
    }

    private void init(){
        context = getContext();
        getBoostAppsToDashboard(BoosterApps.getBoosterApps(context));

        binding.boostAppRecyclerView.setLayoutManager(new GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false));

        binding.boostAppRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy<0 && !binding.addAppsFAB.isShown() && !binding.boostAppsFAB.isShown()){
                    binding.addAppsFAB.show();
                    //binding.boostAppsFAB.show();
                }
                else if(dy>0 && binding.addAppsFAB.isShown() && binding.boostAppsFAB.isShown()){
                    binding.addAppsFAB.hide();
                    //binding.boostAppsFAB.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        binding.addAppsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetUserAppList(context).execute();
                AddApps myBottomSheet = AddApps.newInstance();
                myBottomSheet.show(requireActivity().getSupportFragmentManager(), myBottomSheet.getTag());
            }
        });
    }

    public static void getBoostAppsToDashboard(ArrayList<AppInfo> appInfoArrayList){
        if (appInfoArrayList != null) {
            if(!appInfoArrayList.isEmpty()){
                for(AppInfo appInfo : appInfoArrayList){
                    if(!isPackageInstalled(appInfo.packageName))
                        appInfoArrayList.removeIf(n -> (n.packageName.equals(appInfo.packageName)));
                }
                BoosterApps.setBoosterApps(context, appInfoArrayList);

                TransitionManager.beginDelayedTransition(binding.mainLayout, new MaterialSharedAxis(MaterialSharedAxis.Y, true));
                binding.fabTooltip.setVisibility(View.INVISIBLE);
                binding.illustration.setVisibility(View.INVISIBLE);
                binding.boostAppRecyclerView.setVisibility(View.VISIBLE);
                boosterAppsAdapter = new BoosterAppsAdapter(context, appInfoArrayList);
                binding.boostAppRecyclerView.setItemViewCacheSize(appInfoArrayList.size());
                binding.boostAppRecyclerView.setAdapter(boosterAppsAdapter);
            }else{
                BoosterApps.setBoosterApps(context, null);
                binding.boostAppRecyclerView.setVisibility(View.INVISIBLE);
                TransitionManager.beginDelayedTransition(binding.mainLayout, new MaterialSharedAxis(MaterialSharedAxis.Y, true));
                binding.fabTooltip.setVisibility(View.VISIBLE);
                binding.illustration.setVisibility(View.VISIBLE);
            }
        }
    }

    private static boolean isPackageInstalled(String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void updateInfo() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                updateRamUsage();
                getCpuTemperature();
            }
        });
    }

    private void updateRamUsage() {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);

        long availableMegs = memoryInfo.availMem / 0x100000L;
        long totalMegs = memoryInfo.totalMem / 0x100000L;
        long usedMegs = totalMegs - availableMegs;

        int usedPercentage = Math.toIntExact(Math.round((double) usedMegs / totalMegs * 100));

        double totalGigs = (double) totalMegs / 1024;
        double usedGigs = (double) usedMegs / 1024;

        String ramUsage = usedPercentage + "%";
        binding.totalRam.setText(String.format("%.2f", totalGigs) + " GB");
        binding.usedRam.setText(String.format("%.2f", usedGigs) + " GB");

        binding.ramUsagePercentage.setText(ramUsage);
        binding.ramUsageProgressbar.setProgress(usedPercentage, true);

        new Handler().postDelayed(this::updateInfo, 5000);
    }

    public void getCpuTemperature() {
        Process process;
        try {
            process = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone0/temp");
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            if (line != null) {
                double temp = (Float.parseFloat(line)) / 1000.0f;
                //Log.d("shivam",  (int) temp + "");
            } else {
                //return 0.0f;
            }
        } catch (Exception e) {
            e.printStackTrace();
            //return 0.0f;
        }

        new Handler().postDelayed(this::getCpuTemperature, 5000);
    }

    @Override
    public void onResume() {
        super.onResume();
        //cpuUsageMonitor.startMonitoring(context, this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}