package com.onesys.gfx.tool.game.booster.DocHandling;

import static android.content.Context.MODE_PRIVATE;
import static android.os.Build.VERSION.SDK_INT;
import static com.onesys.gfx.tool.game.booster.Constants.GAME_PACKAGE_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import androidx.documentfile.provider.DocumentFile;

import com.onesys.gfx.tool.game.booster.Constants;
import com.onesys.gfx.tool.game.booster.databinding.GfxFilesListBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class FileHandling {
    SharedPreferences sharedPreferences;
    DocumentFile documentFile;
    Context context;
    String extension;
    String FileName;
    String fileLocation;

    public FileHandling(Context context, String FileName) {
        try {
            this.context = context;
            this.FileName = FileName;
            extension = FileName.substring(FileName.lastIndexOf(".") + 1, FileName.length());
            sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);

            documentFile = DocumentFile.fromTreeUri(context, Uri.parse(sharedPreferences.getString(Constants.DATA_PERMISSION, "")));

            if (documentFile != null) {
                if (SDK_INT >= 33) {
                    documentFile = documentFile.findFile("files");
                } else {
                    documentFile = documentFile.findFile(GAME_PACKAGE_NAME)
                            .findFile("files");
                }

                documentFile = documentFile.findFile("UE4Game")
                        .findFile("ShadowTrackerExtra")
                        .findFile("ShadowTrackerExtra")
                        .findFile("Saved");


                switch (extension) {
                    case "sav":
                        documentFile = documentFile.findFile("SaveGames");
                        break;
                    case "pak":
                        documentFile = documentFile.findFile("Paks");
                        break;
                    case "ini":
                        documentFile = documentFile.findFile("Config").findFile("Android");
                        break;
                }
            }
        } catch (Exception e) {

        }
    }

    public void CopyRenderQualityFromAssets(String qualityName) {
        DeleteFile();

        OutputStream out = null;
        InputStream in = null;
        AssetManager assetManager = context.getAssets();

        try {
            DocumentFile newFile = documentFile.createFile("text/" + extension, FileName);
            assert newFile != null;
            out = context.getContentResolver().openOutputStream(newFile.getUri());

            in = assetManager.open("files/Render Quality/" + qualityName + "/" + FileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();

        } catch (IOException e) {

        }
    }

    public void DeleteFile() {
        try {
            if (documentFile.findFile(FileName).exists()) {
                documentFile.findFile(FileName).delete();
            }

        } catch (Exception e) {

        }
    }

    public String read_UserCustom() {
        InputStream in = null;
        String str;
        String substr = null;

        try {

            AssetManager assetManager = context.getAssets();
            in = assetManager.open("files/UserCustom.ini");

            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            String read;
            StringBuilder sb = new StringBuilder();
            while ((read = br.readLine()) != null) {
                sb.append(read).append("\n");
            }

            str = sb.toString();
            substr = str.substring(str.indexOf("[UserCustom DeviceProfile]"));

            in.close();

        } catch (Exception e) {

        }

        return substr;
    }

    public String read_BackUpProfile() {
        InputStream in = null;
        String str;
        String subStr = null;

        try {

            in = context.getContentResolver().openInputStream(documentFile.findFile("UserCustom.ini").getUri());

            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            String read;
            StringBuilder sb = new StringBuilder();
            while ((read = br.readLine()) != null) {
                sb.append(read).append("\n");
            }

            str = sb.toString();
            subStr = str.substring(str.indexOf("[BackUp DeviceProfile]"));

            in.close();

        } catch (Exception e) {

        }

        return subStr;
    }

    public void write_UserCustom(int resolution, int fps, int shadow, int detail) {

        OutputStream out = null;
        String text = read_UserCustom() + "\n" +
                UserCustom.getResolution(resolution) + "\n" +
                UserCustom.getShadowQuality(shadow) + "\n" +
                UserCustom.getDetailMode(detail) + "\n" +
                UserCustom.getFPS(fps) + "\n\n" +
                read_BackUpProfile();

        DeleteFile();

        try {

            DocumentFile newFile = documentFile.createFile("text/" + extension, FileName);
            out = context.getContentResolver().openOutputStream(newFile.getUri());

            out.write(text.getBytes());

            out.flush();
            out.close();

        } catch (Exception e) {

        }

    }

    public void write_UserSettings(int sound, int water) {
        OutputStream out = null;
        String text = UserSettings.SOUND_QUALITY + sound + "\n\n" + UserSettings.WATER_QUALITY + water;

        DeleteFile();

        try {

            DocumentFile newFile = documentFile.createFile("text/" + extension, FileName);
            out = context.getContentResolver().openOutputStream(newFile.getUri());

            out.write(text.getBytes());

            out.flush();
            out.close();

        } catch (Exception e) {

        }

    }

    public static void RenameFolderPrimary(Context context) {
        String GAME_PACKAGE_NAME = Constants.GAME_PACKAGE_NAME;
        File file = new File("/storage/emulated/0/Android/data/" + GAME_PACKAGE_NAME);
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);

        try {
            if (file.exists()) {
                DocumentFile documentFile = DocumentFile.fromTreeUri(context, Uri.parse(sharedPreferences.getString(Constants.DATA_PERMISSION, "")));
                documentFile.findFile(GAME_PACKAGE_NAME).renameTo(GAME_PACKAGE_NAME + "1");
            }

            file = new File("/storage/emulated/0/Android/obb/" + GAME_PACKAGE_NAME);
            if (file.exists()) {
                DocumentFile documentFile = DocumentFile.fromTreeUri(context, Uri.parse(sharedPreferences.getString(Constants.OBB_PERMISSION, "")));
                documentFile.findFile(GAME_PACKAGE_NAME).renameTo(GAME_PACKAGE_NAME + "1");
            }
        } catch (Exception e) {

        }

    }

    public static void RenameFolderSecondary(Context context) {
        String GAME_PACKAGE_NAME = Constants.GAME_PACKAGE_NAME;
        File file = new File("/storage/emulated/0/Android/data/" + GAME_PACKAGE_NAME + "1");
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);

        try {
            if (file.exists()) {
                DocumentFile documentFile = DocumentFile.fromTreeUri(context, Uri.parse(sharedPreferences.getString(Constants.DATA_PERMISSION, "")));
                documentFile.findFile(GAME_PACKAGE_NAME + "1").renameTo(GAME_PACKAGE_NAME);
            }

            file = new File("/storage/emulated/0/Android/obb/" + GAME_PACKAGE_NAME + "1");
            if (file.exists()) {
                DocumentFile documentFile = DocumentFile.fromTreeUri(context, Uri.parse(sharedPreferences.getString(Constants.OBB_PERMISSION, "")));
                documentFile.findFile(GAME_PACKAGE_NAME + "1").renameTo(GAME_PACKAGE_NAME);
            }
        } catch (Exception e) {

        }

    }

    public void CopyFilesFromFolder(String FileNameInFolder, GfxFilesListBinding binding, boolean isBackup) {
        DeleteFile();

        DeleteFile();
        OutputStream out = null;
        InputStream in = null;

        try {
            DocumentFile newFile = documentFile.createFile("text/" + extension, FileName);
            out = context.getContentResolver().openOutputStream(newFile.getUri());

            if (isBackup)
                in = new FileInputStream(Constants.DOWNLOAD_PATH + Constants.BACKUP_PATH + FileNameInFolder);
            else
                in = new FileInputStream(Constants.DOWNLOAD_PATH + Constants.SERVICE_FILES_PATH + FileNameInFolder);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    //FeedAdapter.AfterFileApplied(context, binding);
                }
            });


        } catch (Exception e) {

        }
    }

    public void CopyFilesFromFolder_InPuffer(String FileNameInFolder, GfxFilesListBinding binding, boolean isBackup) {

        if (SDK_INT >= 29) {
            documentFile = documentFile.findFile("puffer_temp");
        } else {
            fileLocation = fileLocation + "/puffer_temp";
        }

        DeleteFile();
        OutputStream out = null;
        InputStream in = null;
        boolean isFileExists = false;

        if (extension.equals("pak")) {
            try {
                DocumentFile newFile = documentFile.createFile("text/" + extension, FileName);
                out = context.getContentResolver().openOutputStream(newFile.getUri());

                if (isBackup)
                    in = new FileInputStream(Constants.DOWNLOAD_PATH + Constants.BACKUP_PATH + FileNameInFolder);
                else
                    in = new FileInputStream(Constants.DOWNLOAD_PATH + Constants.SERVICE_FILES_PATH + FileNameInFolder);


                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                out.flush();
                out.close();

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        //FeedAdapter.AfterFileApplied(context, binding);
                    }
                });

            } catch (IOException e) {

            }
        } else {

        }
    }

}
