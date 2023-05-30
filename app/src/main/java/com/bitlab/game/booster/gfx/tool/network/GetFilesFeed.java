package com.bitlab.game.booster.gfx.tool.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.transition.Explode;
import androidx.transition.TransitionManager;

import com.bitlab.game.booster.gfx.tool.BuildConfig;
import com.bitlab.game.booster.gfx.tool.Constants;
import com.bitlab.game.booster.gfx.tool.adapters.FeedAdapter;
import com.bitlab.game.booster.gfx.tool.model.FilesFeedModal;
import com.bitlab.game.booster.gfx.tool.views.fragment.SecondaryGFX;

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
        } catch (IOException e) {

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                if(filesFeedModalArrayList != null){
                    fileFeedAdapter = new FeedAdapter(context, filesFeedModalArrayList);
                    fileFeedAdapter.setHasStableIds(false);

                    SecondaryGFX.binding.recyclerViewFeed.setItemViewCacheSize(filesFeedModalArrayList.size());
                    SecondaryGFX.binding.recyclerViewFeed.setAdapter(fileFeedAdapter);

                    TransitionManager.beginDelayedTransition(SecondaryGFX.binding.mainLayout, new Explode());
                    SecondaryGFX.binding.shimmerGfxFiles.setVisibility(View.INVISIBLE);
                    SecondaryGFX.binding.recyclerViewFeed.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(context, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public static void notifyChanges(int position){
        //fileFeedAdapter.notifyDataSetChanged();
    }

}
