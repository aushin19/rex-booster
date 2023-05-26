package com.bitlab.game.booster.gfx.tool.model;

public class AppInfo {
    public String appName;
    public String packageName;
    public boolean isAdded;

    public AppInfo(String appName, String packageName, boolean isAdded) {
        this.appName = appName;
        this.packageName = packageName;
        this.isAdded = isAdded;
    }
}
