package com.onemosys.gfx.tool.game.booster.network;

import android.os.AsyncTask;

import com.onemosys.gfx.tool.game.booster.BuildConfig;
import com.onemosys.gfx.tool.game.booster.Constants;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class GetGameVersion extends AsyncTask<String, String,String> {
    @Override
    protected String doInBackground(String... strings) {

        try {
            Document document = Jsoup.connect(BuildConfig.GAME_VERSION_API_KEY).get();
            Elements element = document.getElementsByClass("game_version");

            JSONObject jsonObject = new JSONObject(Jsoup.parse(element.toString()).text());
            Constants.CURRENT_GAME_VERSION = jsonObject.getString("version");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
