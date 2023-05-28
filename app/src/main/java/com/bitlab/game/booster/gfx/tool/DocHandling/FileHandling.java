package com.bitlab.game.booster.gfx.tool.DocHandling;

import static android.content.Context.MODE_PRIVATE;
import static android.os.Build.VERSION.SDK_INT;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.documentfile.provider.DocumentFile;

import com.bitlab.game.booster.gfx.tool.Constants;
import com.bitlab.game.booster.gfx.tool.databinding.GfxFilesListBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
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

    public FileHandling(Context context, String FileName){
        this.context = context;
        this.FileName = FileName;
        extension = FileName.substring(FileName.lastIndexOf(".") + 1, FileName.length());
        sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);

        if (SDK_INT >= 33) {
            documentFile = DocumentFile.fromTreeUri(context, Uri.parse(sharedPreferences.getString(Constants.DATA_PERMISSION, "")));
            try {
                documentFile = documentFile.findFile("files")
                        .findFile("UE4Game")
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (SDK_INT >= 29) {
            documentFile = DocumentFile.fromTreeUri(context, Uri.parse(sharedPreferences.getString(Constants.DATA_PERMISSION, "")));
            try {
                documentFile = documentFile.findFile(Constants.GAME_PACKAGE_NAME)
                        .findFile("files")
                        .findFile("UE4Game")
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            fileLocation = Environment.getExternalStorageDirectory() + "/Android/data/" + Constants.GAME_PACKAGE_NAME + "/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/";

            switch (extension) {
                case "sav":
                    fileLocation = fileLocation + "SaveGames";
                    break;
                case "pak":
                    fileLocation = fileLocation + "Paks";
                    break;
                case "ini":
                    fileLocation = fileLocation + "Config/Android";
                    break;
            }
        }
    }

    public void CopyRenderQualityFromAssets(String qualityName){
        DeleteFile();

        OutputStream out = null;
        InputStream in = null;
        AssetManager assetManager = context.getAssets();

        try {

            if(SDK_INT >= 29){
                documentFile = documentFile.createFile("text/" + extension, FileName);
                out = context.getContentResolver().openOutputStream(documentFile.getUri());
            }
            else
                out = new FileOutputStream(fileLocation  + "/" + FileName);

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

    public void DeleteFile(){
        try {
            if(SDK_INT >= 29){
                if(documentFile.findFile(FileName).exists()){
                    documentFile.findFile(FileName).delete();
                }
            }else{
                File file = new File(fileLocation + "/" + FileName);
                if(file.exists()){
                    file.delete();
                }
            }

        } catch (Exception e) {

        }
    }

    public String read_UserCustom(){
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

        }catch (Exception e) {

        }

        return substr;
    }

    public String read_BackUpProfile(){
        InputStream in = null;
        String str;
        String subStr = null;

        try {

            if(SDK_INT >= 29){
                in = context.getContentResolver().openInputStream(documentFile.findFile("UserCustom.ini").getUri());
            }else{
                File des = new File(fileLocation + "/UserCustom.ini");
                Uri fileDes = Uri.fromFile(des);
                in = context.getContentResolver().openInputStream(fileDes);
            }

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

        }catch (Exception e) {

        }

        return subStr;
    }

    public void write_UserCustom(int resolution, int fps, int shadow, int detail){

        OutputStream out = null;
        String text = read_UserCustom() + "\n" +
                UserCustom.getResolution(resolution) + "\n" +
                UserCustom.getShadowQuality(shadow) + "\n" +
                UserCustom.getDetailMode(detail) + "\n" +
                UserCustom.getFPS(fps) + "\n\n" +
                read_BackUpProfile();

        DeleteFile();

        if(SDK_INT >= 29){
            try {

                DocumentFile newFile = documentFile.createFile("text/" + extension, FileName);
                out = context.getContentResolver().openOutputStream(newFile.getUri());

                out.write(text.getBytes());

                out.flush();
                out.close();

            }catch (Exception e) {

            }
        }else{
            try {
                File root = new File(fileLocation);
                if (!root.exists()) {
                    root.mkdirs();
                }
                File file = new File(root, "UserCustom.ini");
                FileWriter writer = new FileWriter(file);
                writer.append(text);
                writer.flush();
                writer.close();
            } catch (IOException e) {

            }
        }

    }

    public void write_UserSettings(int sound, int water){
        OutputStream out = null;
        String text = UserSettings.SOUND_QUALITY + sound + "\n\n" + UserSettings.WATER_QUALITY + water;

        DeleteFile();

        if(SDK_INT >= 29){
            try {

                DocumentFile newFile = documentFile.createFile("text/" + extension, FileName);
                out = context.getContentResolver().openOutputStream(newFile.getUri());

                out.write(text.getBytes());

                out.flush();
                out.close();

            }catch (Exception e) {

            }
        }else{
            try {
                File root = new File(fileLocation);
                if (!root.exists()) {
                    root.mkdirs();
                }
                File file = new File(root, "UserSettings.ini");
                FileWriter writer = new FileWriter(file);
                writer.append(text);
                writer.flush();
                writer.close();
            } catch (IOException e) {

            }
        }

    }

    public static void RenameFolderPrimary(Context context){
        String GAME_PACKAGE_NAME = Constants.GAME_PACKAGE_NAME;
        File file = new File("/storage/emulated/0/Android/data/" + GAME_PACKAGE_NAME);
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref",MODE_PRIVATE);

        if(SDK_INT >= 29){
            try {
                if(file.exists()){
                    DocumentFile documentFile = DocumentFile.fromTreeUri(context, Uri.parse(sharedPreferences.getString(Constants.DATA_PERMISSION,"")));
                    documentFile.findFile(GAME_PACKAGE_NAME).renameTo(GAME_PACKAGE_NAME + "1");
                }

                file = new File("/storage/emulated/0/Android/obb/" + GAME_PACKAGE_NAME);
                if(file.exists()){
                    DocumentFile documentFile = DocumentFile.fromTreeUri(context, Uri.parse(sharedPreferences.getString(Constants.OBB_PERMISSION,"")));
                    documentFile.findFile(GAME_PACKAGE_NAME).renameTo(GAME_PACKAGE_NAME + "1");
                }
            } catch (Exception e) {

            }
        }else{

            if(file.exists()){
                file.renameTo(new File(file + "1"));
                Log.d("Sunny", file.toString());
            }

            file = new File("/storage/emulated/0/Android/obb/" + GAME_PACKAGE_NAME);
            if(file.exists()){
                file.renameTo(new File(file + "1"));
                Log.d("Sunny", file.toString());
            }
        }

    }

    public static void RenameFolderSecondary(Context context){
        String GAME_PACKAGE_NAME = Constants.GAME_PACKAGE_NAME;
        File file = new File("/storage/emulated/0/Android/data/" + GAME_PACKAGE_NAME + "1");
        File rename = new File("/storage/emulated/0/Android/data/" + GAME_PACKAGE_NAME);
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref",MODE_PRIVATE);

        if(SDK_INT >= 29){
            try {
                if(file.exists()){
                    DocumentFile documentFile = DocumentFile.fromTreeUri(context, Uri.parse(sharedPreferences.getString(Constants.DATA_PERMISSION,"")));
                    documentFile.findFile(GAME_PACKAGE_NAME + "1").renameTo(GAME_PACKAGE_NAME);
                }

                file = new File("/storage/emulated/0/Android/obb/" + GAME_PACKAGE_NAME + "1");
                if(file.exists()){
                    DocumentFile documentFile = DocumentFile.fromTreeUri(context, Uri.parse(sharedPreferences.getString(Constants.OBB_PERMISSION,"")));
                    documentFile.findFile(GAME_PACKAGE_NAME + "1").renameTo(GAME_PACKAGE_NAME);
                }
            } catch (Exception e) {

            }
        }else{
            if(file.exists()){
                file.renameTo(new File(rename + ""));
            }

            file = new File("/storage/emulated/0/Android/obb/" + GAME_PACKAGE_NAME + "1");
            rename = new File("/storage/emulated/0/Android/obb/" + GAME_PACKAGE_NAME);
            if(file.exists()){
                file.renameTo(new File(rename + ""));
            }
        }

    }

    public void CopyFilesFromFolder(String FileNameInFolder, GfxFilesListBinding binding, boolean isBackup){
        DeleteFile();

        DeleteFile();
        OutputStream out = null;
        InputStream in = null;

        try {
            if (SDK_INT >= 29) {
                documentFile = documentFile.createFile("text/" + extension, FileName);
                out = context.getContentResolver().openOutputStream(documentFile.getUri());
            }else{
                out = new FileOutputStream(fileLocation + "/" + FileName);
            }

            if(isBackup)
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


        }catch (Exception e){

        }
    }

    public void CopyFilesFromFolder_InPuffer(String FileNameInFolder, GfxFilesListBinding binding, boolean isBackup){

        if(SDK_INT >= 29){
            documentFile = documentFile.findFile("puffer_temp");
        }else{
            fileLocation = fileLocation + "/puffer_temp";
        }

        DeleteFile();
        OutputStream out = null;
        InputStream in = null;
        boolean isFileExists = false;

        if(extension.equals("pak")) {
            try {
                if (SDK_INT >= 29) {
                    documentFile = documentFile.createFile("text/" + extension, FileName);
                    out = context.getContentResolver().openOutputStream(documentFile.getUri());
                } else {
                    isFileExists = new File(fileLocation + "/" + FileName).exists();
                    if(isFileExists){
                        out = new FileOutputStream(fileLocation + "/" + FileName);
                    }else
                        return;
                }

                if(isBackup)
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
        }else {

        }
    }

    public void CopyPak(String FileLocation){
        OutputStream out = null;
        InputStream in = null;

        DeleteFile();

        try {
            if(SDK_INT >= 29){
                documentFile = documentFile.createFile("text/" + extension, FileName);
                out = context.getContentResolver().openOutputStream(documentFile.getUri());
            }
            else
                out = new FileOutputStream(fileLocation + "/" + FileName);

            AssetManager assetManager = context.getAssets();

            in = assetManager.open(FileLocation + "/" + FileName);

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

}
