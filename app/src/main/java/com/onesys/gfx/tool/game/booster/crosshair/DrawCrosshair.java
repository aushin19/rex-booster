package com.onesys.gfx.tool.game.booster.crosshair;

import static android.content.Context.WINDOW_SERVICE;
import static android.os.Build.VERSION.SDK_INT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.transition.Explode;
import androidx.transition.TransitionManager;

import com.onesys.gfx.tool.game.booster.DocHandling.FileHandling;
import com.onesys.gfx.tool.game.booster.R;
import com.onesys.gfx.tool.game.booster.adapters.FeedAdapter;
import com.onesys.gfx.tool.game.booster.model.SelectedFilesModal;
import com.onesys.gfx.tool.game.booster.views.fragment.SecondaryGFX;

import java.util.ArrayList;

public class DrawCrosshair {
    private Context context;
    private View mView;
    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private LayoutInflater layoutInflater;
    int initialX, initialY;
    float initialTouchX, initialTouchY;
    public static ArrayList<SelectedFilesModal> selectedFilesArrayList = new ArrayList<>();

    @SuppressLint("InflateParams")
    public DrawCrosshair(Context context){
        this.context = context;

        selectedFilesArrayList = SecondaryGFX.selectedFilesArrayList;

        mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = layoutInflater.inflate(R.layout.draw_over_popup_window, null, false);

        CardView view = mView.findViewById(R.id.parent_layout);

        /*mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        initialX = mParams.x;
                        initialY = mParams.y;

                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();

                        return true;
                    case MotionEvent.ACTION_UP:
                        mParams.x = initialX+(int) (initialTouchX - event.getRawX());
                        mParams.y = initialY+(int) (event.getRawY() - initialTouchY);

                        return true;
                    case MotionEvent.ACTION_MOVE:
                        mParams.x = initialX+(int) (initialTouchX - event.getRawX());
                        mParams.y = initialY+(int) (event.getRawY() - initialTouchY);

                        mWindowManager.updateViewLayout(mView, mParams);

                        return true;
                }

                return false;
            }
        });*/

        mView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

        mView.findViewById(R.id.apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!selectedFilesArrayList.isEmpty()){
                    TransitionManager.beginDelayedTransition(view, new Explode());

                    mView.findViewById(R.id.apply).setVisibility(View.GONE);
                    mView.findViewById(R.id.close).setVisibility(View.GONE);
                    mView.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < selectedFilesArrayList.size(); i++) {
                                new FileHandling(context, selectedFilesArrayList.get(i).FileName).CopyFilesFromFolder(selectedFilesArrayList.get(i).Title, selectedFilesArrayList.get(i).binding, false);
                                new FileHandling(context, selectedFilesArrayList.get(i).FileName).CopyFilesFromFolder_InPuffer(selectedFilesArrayList.get(i).Title, selectedFilesArrayList.get(i).binding, false);
                            }

                            if (SDK_INT < 33) {
                                FileHandling.RenameFolderPrimary(context);
                            }

                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                public void run() {
                                    Toast.makeText(context, "Files Applied!", Toast.LENGTH_SHORT).show();
                                }
                            });

                            FeedAdapter.selectedFilesModalArrayList.clear();
                            SecondaryGFX.selectedFilesArrayList.clear();
                            selectedFilesArrayList.clear();

                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                public void run() {
                                    close();
                                }
                            });
                        }
                    }).start();
                }else{
                    Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    close();
                }

            }
        });

        mParams.gravity = Gravity.TOP|Gravity.RIGHT;
        mParams.x = 0;
        mParams.y = 100;
        mWindowManager = (WindowManager)context.getSystemService(WINDOW_SERVICE);
    }

    public void open() {
        try {
            mWindowManager.addView(mView, mParams);
        } catch (Exception e) {
            Log.d("shivam", e.getMessage());
        }
    }

    public void close() {
        try {
            if(mView != null){
                mWindowManager.removeView(mView);
            }
        } catch (Exception e) {
            Log.d("shivam", e.getMessage());
        }
    }

}
