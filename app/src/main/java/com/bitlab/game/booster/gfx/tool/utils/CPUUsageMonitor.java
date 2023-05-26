package com.bitlab.game.booster.gfx.tool.utils;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CPUUsageMonitor {
    private static final int INTERVAL = 1000; // Update interval in milliseconds

    private HandlerThread handlerThread;
    private Handler handler;
    private Runnable runnable;

    public void startMonitoring(final Context context, final CPUUsageListener listener) {
        handlerThread = new HandlerThread("CPUUsageThread");
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                double cpuUsage = getCpuUsage();
                listener.onCPUUsageUpdated(cpuUsage);
                handler.postDelayed(this, INTERVAL);
            }

            private double getCpuUsage() {
                double totalCpuUsage = 0.0;
                try {
                    ProcessBuilder processBuilder = new ProcessBuilder("/system/bin/top", "-n", "1");
                    Process process = processBuilder.start();
                    process.waitFor();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    int lineCount = 0;
                    while ((line = reader.readLine()) != null) {
                        lineCount++;
                        if (lineCount > 2) { // Skip the first two lines
                            String[] parts = line.split(" ");
                            totalCpuUsage += Double.parseDouble(parts[0]);
                        }
                    }
                    reader.close();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                return totalCpuUsage;
            }
        };

        handler.post(runnable);
    }

    public void stopMonitoring() {
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        if (handlerThread != null) {
            handlerThread.quit();
        }
    }

    public interface CPUUsageListener {
        void onCPUUsageUpdated(double cpuUsage);
    }
}

