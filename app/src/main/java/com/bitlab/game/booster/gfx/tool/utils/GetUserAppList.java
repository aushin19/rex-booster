package com.bitlab.game.booster.gfx.tool.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import com.bitlab.game.booster.gfx.tool.BuildConfig;
import com.bitlab.game.booster.gfx.tool.model.AppInfo;
import com.bitlab.game.booster.gfx.tool.views.fragment.AddApps;

import java.util.ArrayList;
import java.util.List;

public class GetUserAppList extends AsyncTask<Void, Void, Void> {

    Context context;
    ArrayList<AppInfo> appList = new ArrayList<>();

    public GetUserAppList(Context context){
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo appInfo : installedApps) {
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                if (!appInfo.packageName.equals(BuildConfig.APPLICATION_ID)) {
                    String appName = appInfo.loadLabel(packageManager).toString();
                    String packageName = appInfo.packageName;
                    boolean isAdded = false;

                    if(BoosterApps.getBoosterApps(context) != null) {
                        for (AppInfo appPackageName : BoosterApps.getBoosterApps(context)) {
                            if(appPackageName.packageName.equals(packageName)) {
                                isAdded = true;
                            }
                        }
                    }

                    appList.add(new AppInfo(appName, packageName, isAdded));
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);

        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AddApps.createRecyclerView(appList);
            }
        });
    }
}
