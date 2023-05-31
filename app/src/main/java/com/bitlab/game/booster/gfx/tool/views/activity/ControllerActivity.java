package com.bitlab.game.booster.gfx.tool.views.activity;

import static android.os.Build.VERSION.SDK_INT;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bitlab.game.booster.gfx.tool.AppConfig;
import com.bitlab.game.booster.gfx.tool.Constants;
import com.bitlab.game.booster.gfx.tool.DocHandling.PermissionHandling;
import com.bitlab.game.booster.gfx.tool.R;
import com.bitlab.game.booster.gfx.tool.adapters.MainPageViewAdapter;
import com.bitlab.game.booster.gfx.tool.bottomsheets.SAFBottomsheet;
import com.bitlab.game.booster.gfx.tool.databinding.ActivityControllerBinding;
import com.bitlab.game.booster.gfx.tool.network.GetGameVersion;
import com.bitlab.game.booster.gfx.tool.utils.CheckInternetConnection;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.onesignal.OneSignal;

public class ControllerActivity extends AppCompatActivity {
    static ActivityControllerBinding binding;
    static Context context;
    static Activity activity;
    SharedPreferences sharedPreferences;
    static int DRAW_OVER_REQUEST_CODE = 2048;
    private AppUpdateManager mAppUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityControllerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setNavigation();

        checkInAppUpdate();
        initOneSignal();
        initFirebaseAnalytics();
    }

    private void init() {
        context = this;
        activity = this;
        sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (CheckInternetConnection.isNetworkConnected(context)) {
                    new GetGameVersion().execute();
                }
            }
        }).start();

        binding.premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ControllerActivity.this, Subscription.class));
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });
    }

    private void checkInAppUpdate() {
        mAppUpdateManager = AppUpdateManagerFactory.create(this);
        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    try {
                        mAppUpdateManager.startUpdateFlowForResult(result,AppUpdateType.IMMEDIATE, ControllerActivity.this,100);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void initFirebaseAnalytics() {
        FirebaseAnalytics.getInstance(this);
    }

    private void initOneSignal() {
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(ControllerActivity.this);
        OneSignal.setAppId(AppConfig.ONE_SIGNAL_ID);
    }

    private void setNavigation() {

        binding.mainPageView.setAdapter(new MainPageViewAdapter(getSupportFragmentManager()));
        binding.mainPageView.setOffscreenPageLimit(3);

        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        binding.mainPageView.setCurrentItem(0);
                        break;
                    case R.id.primaryGFX:
                        binding.mainPageView.setCurrentItem(1);
                        break;
                    case R.id.secondaryGFX:
                        binding.mainPageView.setCurrentItem(2);
                        break;/*
                    case R.id.stats:
                        binding.mainPageView.setCurrentItem(3);
                        break;*/
                }
                return true;
            }
        });

        binding.mainPageView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        runTextAnim("Welcome");
                        break;
                    case 1:
                        runTextAnim("Game Settings");
                        break;
                    case 2:
                        runTextAnim("Premium Files");
                        break;/*
                    case 3:
                        runTextAnim("Stats");
                        break;*/
                }
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        binding.bottomNav.getMenu().findItem(R.id.dashboard).setChecked(true);
                        break;
                    case 1:
                        binding.bottomNav.getMenu().findItem(R.id.primaryGFX).setChecked(true);
                        break;
                    case 2:
                        binding.bottomNav.getMenu().findItem(R.id.secondaryGFX).setChecked(true);
                        break;/*
                    case 3:
                        binding.bottomNav.getMenu().findItem(R.id.stats).setChecked(true);
                        break;*/
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    void runTextAnim(String text) {
        binding.appbarMainText.setText(text);
    }

    public static void askPermission() {
        StorageManager storageManager;
        if (PermissionHandling.isGranted(context)) {
            if (PermissionHandling.checkAndRequestPermissions(context)) {
                storageManager = (StorageManager) context.getSystemService(STORAGE_SERVICE);
                Intent intent = null;
                String targetDirectory = null;
                if (SDK_INT >= 33) {
                    intent = storageManager.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
                    targetDirectory = "Android%2Fdata%2F" + Constants.GAME_PACKAGE_NAME;
                } else {
                    intent = storageManager.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
                    targetDirectory = "Android%2Fdata";
                }

                Uri uri = intent.getParcelableExtra("android.provider.extra.INITIAL_URI");
                String scheme = uri.toString();
                scheme = scheme.replace("/root/", "/document/");
                scheme += "%3A" + targetDirectory;
                uri = Uri.parse(scheme);

                intent.putExtra("android.provider.extra.INITIAL_URI", uri);
                ((Activity) context).startActivityForResult(intent, PermissionHandling.REQUEST_ACTION_OPEN_DOCUMENT_TREE_DATA);
            }
        } else if (PermissionHandling.isGrantedOBB(context)) {
            askPermissionOBB();
        }
    }

    public static void askPermissionOBB() {
        StorageManager storageManager = (StorageManager) context.getSystemService(STORAGE_SERVICE);
        Intent intent = null;
        String targetDirectory = null;

        if (SDK_INT >= 33) {
            intent = storageManager.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
            targetDirectory = "Android%2Fobb%2F" + Constants.GAME_PACKAGE_NAME;
        } else {
            intent = storageManager.getPrimaryStorageVolume().createOpenDocumentTreeIntent();
            targetDirectory = "Android%2Fobb";
        }

        Uri uri = intent.getParcelableExtra("android.provider.extra.INITIAL_URI");
        String scheme = uri.toString();
        scheme = scheme.replace("/root/", "/document/");
        scheme += "%3A" + targetDirectory;
        uri = Uri.parse(scheme);

        intent.putExtra("android.provider.extra.INITIAL_URI", uri);
        ((Activity) context).startActivityForResult(intent, PermissionHandling.REQUEST_ACTION_OPEN_DOCUMENT_TREE_OBB);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == PermissionHandling.REQUEST_ACTION_OPEN_DOCUMENT_TREE_DATA && resultCode == -1) {
            Uri data = intent.getData();

            String dataPath = null;

            if (SDK_INT >= 33) {
                dataPath = "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata%2F" + Constants.GAME_PACKAGE_NAME;
            } else {
                dataPath = "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata";
            }

            if (data.toString().equals(dataPath)) {

                new SAFBottomsheet(context).dismiss();

                try {
                    getContentResolver().takePersistableUriPermission(data, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.DATA_PERMISSION, data.toString());
                editor.apply();
                askPermissionOBB();
            } else {
                new SAFBottomsheet(context).Show(this, Constants.DATA_PERMISSION);
            }
        } else if (requestCode == PermissionHandling.REQUEST_ACTION_OPEN_DOCUMENT_TREE_OBB && resultCode == -1) {
            Uri data = intent.getData();

            String dataPath = null;

            if (SDK_INT >= 33) {
                dataPath = "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fobb%2F" + Constants.GAME_PACKAGE_NAME;
            } else {
                dataPath = "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fobb";
            }

            if (data.toString().equals(dataPath)) {

                new SAFBottomsheet(context).dismiss();

                try {
                    getContentResolver().takePersistableUriPermission(data, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.OBB_PERMISSION, data.toString());
                editor.apply();
            } else {
                new SAFBottomsheet(context).Show(this, Constants.OBB_PERMISSION);
            }
        } else if (requestCode == DRAW_OVER_REQUEST_CODE && resultCode == RESULT_OK) {
            Toast.makeText(this, "Permitted", Toast.LENGTH_SHORT).show();
        }
    }

    public static void getDrawOverPermission() {
        ((Activity) context).startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName())), DRAW_OVER_REQUEST_CODE);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if (result.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    try {
                        mAppUpdateManager.startUpdateFlowForResult(result,AppUpdateType.IMMEDIATE, ControllerActivity.this,100);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
