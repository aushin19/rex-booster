package com.bitlab.game.booster.gfx.tool.utils;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.bitlab.game.booster.gfx.tool.Constants;
import com.bitlab.game.booster.gfx.tool.bottomsheets.AskDownloadBottomsheet;
import com.bitlab.game.booster.gfx.tool.network.GetFilesFeed;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;

public class DownloadFiles extends AsyncTask<String, Integer, String> {

    public String URL;
    public static String outputFileName;
    Context context;
    int position;
    File file;
    public static int dl_progress;
    boolean isBackupFile;
    String fileType;

    public DownloadFiles(String url, String outputFileName, Context context, int position, boolean isBackupFile) {
        this.URL = url;
        this.outputFileName = outputFileName;
        this.context = context;
        this.position = position;
        this.isBackupFile = isBackupFile;
    }

    @SuppressLint("Range")
    @Override
    protected String doInBackground(String... strings) {
        Element element = null;
        try {
            Document document = Jsoup.connect(URL)
                    .userAgent("Chrome")
                    .get();
            element = document.getElementById("downloadButton");

            if(isBackupFile){
                fileType = Constants.BACKUP_PATH;

            }
            else{
                fileType = Constants.SERVICE_FILES_PATH;
            }

            file = new File(Constants.DOWNLOAD_PATH + fileType + "corrupt");

            if (file.exists()) {
                file.delete();
            }

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(Jsoup.parse(element.toString()).select("a").attr("href")));
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            request.allowScanningByMediaScanner();
            request.setDestinationInExternalFilesDir(context, fileType, "corrupt");
            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            final long downloadId = manager.enqueue(request);
            boolean downloading = true;
            while (downloading) {

                DownloadManager.Query q = new DownloadManager.Query();
                q.setFilterById(downloadId);

                Cursor cursor = manager.query(q);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    @SuppressLint("Range") int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    @SuppressLint("Range") int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false;
                    }

                    try {
                        dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);
                    } catch (Exception e) {
                    }

                    cursor.close();
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        if (dl_progress == 100) {

                            if (file.exists()) {
                                file.renameTo(new File(Constants.DOWNLOAD_PATH + fileType + outputFileName));
                            }

                            GetFilesFeed.notifyChanges(position);
                        } else {
                            AskDownloadBottomsheet.binding.titleText.setText(dl_progress+"%");
                        }
                    }
                });

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        AskDownloadBottomsheet.dismiss();
    }
}