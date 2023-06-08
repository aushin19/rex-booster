package com.onemosys.gfx.tool.game.booster.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.transition.Explode;
import androidx.transition.TransitionManager;

import com.onemosys.gfx.tool.game.booster.BuildConfig;
import com.onemosys.gfx.tool.game.booster.Constants;
import com.onemosys.gfx.tool.game.booster.adapters.FeedAdapter;
import com.onemosys.gfx.tool.game.booster.model.FilesFeedModal;
import com.onemosys.gfx.tool.game.booster.views.fragment.SecondaryGFX;

import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class GetFilesFeed extends AsyncTask<Void, Void, Void> {
    Context context;
    ArrayList<FilesFeedModal> filesFeedModalArrayList = new ArrayList<>();
    static FeedAdapter fileFeedAdapter;
    String content;

    public GetFilesFeed(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Document document = Jsoup.connect(BuildConfig.FILES_API_KEY).get();
            Elements element = document.getElementsByClass(Constants.FILE_FEED_VERSION);

            content = Jsoup.parse(String.valueOf(element)).text();

            if (content.length() != 0) {
                try {
                    JSONArray jsonArray = new JSONArray(content);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        filesFeedModalArrayList.add(new FilesFeedModal(
                                jsonArray.getJSONObject(i).getString("title"),
                                jsonArray.getJSONObject(i).getString("des"),
                                jsonArray.getJSONObject(i).getString("extension"),
                                jsonArray.getJSONObject(i).getString("link"),
                                jsonArray.getJSONObject(i).getString("backup_link"),
                                jsonArray.getJSONObject(i).getString("file_name"),
                                jsonArray.getJSONObject(i).getString("game_version"),
                                jsonArray.getJSONObject(i).getString("type")
                        ));
                    }
                } catch (JSONException e) {

                }
            }else{
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Something Went Wrong!", Toast.LENGTH_LONG).show();
                    }
                });
            }

        } catch (IOException e) {

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                if (filesFeedModalArrayList != null) {
                    fileFeedAdapter = new FeedAdapter(context, filesFeedModalArrayList);
                    fileFeedAdapter.setHasStableIds(false);

                    SecondaryGFX.binding.recyclerViewFeed.setItemViewCacheSize(filesFeedModalArrayList.size());
                    SecondaryGFX.binding.recyclerViewFeed.setAdapter(fileFeedAdapter);

                    TransitionManager.beginDelayedTransition(SecondaryGFX.binding.mainLayout, new Explode());
                    SecondaryGFX.binding.shimmerGfxFiles.setVisibility(View.INVISIBLE);
                    SecondaryGFX.binding.recyclerViewFeed.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(context, "Something Went Wrong!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public static void notifyChanges(int position) {
        //fileFeedAdapter.notifyDataSetChanged();
    }

}
