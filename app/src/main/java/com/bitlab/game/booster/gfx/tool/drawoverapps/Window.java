package com.bitlab.game.booster.gfx.tool.drawoverapps;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.transition.Explode;
import androidx.transition.TransitionManager;

import com.bitlab.game.booster.gfx.tool.DocHandling.FileHandling;
import com.bitlab.game.booster.gfx.tool.R;
import com.bitlab.game.booster.gfx.tool.adapters.FeedAdapter;
import com.bitlab.game.booster.gfx.tool.model.SelectedFilesModal;
import com.bitlab.game.booster.gfx.tool.views.fragment.SecondaryGFX;

import java.util.ArrayList;

import io.github.hyuwah.draggableviewlib.DraggableView;

public class Window implements View.OnTouchListener {
    private Context context;
    private View mView;
    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private LayoutInflater layoutInflater;
    float dX;
    float dY;
    int lastAction;
    public static ArrayList<SelectedFilesModal> selectedFilesArrayList = new ArrayList<>();

    @SuppressLint("InflateParams")
    public Window(Context context){
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

        new DraggableView.Builder<CardView>(view).setAnimated(true).setStickyMode(DraggableView.Mode.STICKY_XY).build();

        //view.setOnTouchListener(this);

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

                            FeedAdapter.selectedFilesModalArrayList.clear();
                            SecondaryGFX.selectedFilesArrayList.clear();
                            selectedFilesArrayList.clear();

                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                public void run() {
                                    Toast.makeText(context, "Files Applied!", Toast.LENGTH_SHORT).show();
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

        mParams.gravity = Gravity.START;
        mWindowManager = (WindowManager)context.getSystemService(WINDOW_SERVICE);

    }

    public void open() {
        try {
            if(mView.getWindowToken()==null) {
                if(mView.getParent()==null) {
                    mWindowManager.addView(mView, mParams);
                }
            }
        } catch (Exception e) {
            Log.d("Error1",e.toString());
        }

    }

    public void close() {
        try {
            ((WindowManager)context.getSystemService(WINDOW_SERVICE)).removeView(mView);
            mView.invalidate();
            ((ViewGroup)mView.getParent()).removeAllViews();
        } catch (Exception e) {
            Log.d("Error2",e.toString());
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dX = v.getX() - event.getRawX();
                dY = v.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                v.setY(event.getRawY() + dY);
                v.setX(event.getRawX() + dX);
                lastAction = MotionEvent.ACTION_MOVE;
                break;

            case MotionEvent.ACTION_UP:
                if (lastAction == MotionEvent.ACTION_DOWN)
                    Toast.makeText(context, "Clicked!", Toast.LENGTH_SHORT).show();
                break;

            default:
                return false;
        }
        return true;
    }
}
