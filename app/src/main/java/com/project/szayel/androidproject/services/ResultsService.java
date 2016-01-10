package com.project.szayel.androidproject.services;

import android.os.AsyncTask;
import android.util.Log;

import com.project.szayel.androidproject.models.Anime;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ResultsService extends AsyncTask<String, Void, List<Anime>> {
    private final String TAG = "ResultsService";

    private AsyncResultListener mAsyncResultListener;

    public ResultsService(AsyncResultListener listener) {
        super();

        mAsyncResultListener = listener;
    }

    @Override
    protected void onPostExecute(List<Anime> animes) {
        super.onPostExecute(animes);

        mAsyncResultListener.getSearchResult(animes);
    }

    @Override
    protected List<Anime> doInBackground(String... urls) {
        try {
            String result = downloadUrl(urls[0]);
            JSONArray jsonArray = new JSONArray(result);
            List<Anime> animeSearchResult = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                Anime anime = parse(item);

                animeSearchResult.add(anime);
            }

            return animeSearchResult;
        } catch(Exception e) {
            e.printStackTrace();
            Log.d(TAG, "doInBackground: some exception");
            return null;
        }
    }

    private String downloadUrl(String url) {
        String responseString = null;

        try {
            OkHttpClient client = new OkHttpClient();
            Log.d(TAG, url);
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("X-Client-Id", "52496766a07198685b41")
                    .build();

            Response response = client.newCall(request).execute();
            responseString = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseString;
    }

    public Anime parse(JSONObject animeData) {
        Anime anime = new Anime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Object check;

        try {
            anime.setId((int) animeData.get("id"));
            anime.setStatus((String) animeData.get("status"));
            anime.setTitle((String) animeData.get("title"));

            check = animeData.opt("episode_count");
            if (check.toString() != "null") {
                anime.setEpisodes((int) check);
            }

            check = animeData.opt("episode_length");
            if (check.toString() != "null") {
                anime.setDuration((int) check);
            }

            anime.setImageUrl((String) animeData.get("cover_image"));

            check = animeData.opt("started_airing");
            if (check.toString() != "null") {
                java.util.Date started = formatter.parse((String) check);
                anime.setStarted_airing(new Date(started.getTime()));
            }

            check = animeData.opt("finished_airing");
            if (check.toString() != "null") {
                java.util.Date finished = formatter.parse((String) check);
                anime.setFinished_airing(new Date(finished.getTime()));
            }


            check = animeData.opt("show_type");
            if (check.toString() != "null") {
                anime.setShowType((String) check);
            } else {
                anime.setShowType("");
            }


            anime.setRating((double) animeData.get("community_rating"));

            check = animeData.opt("age_rating");
            if (check.toString() != "null") {
                anime.setAgeRating((String) check);
            }

            List<String> genres = new ArrayList<String>();
            JSONArray jsonGenres = animeData.getJSONArray("genres");
            for (int g = 0; g < jsonGenres.length(); g++) {
                JSONObject genre = jsonGenres.getJSONObject(g);
                genres.add((String) genre.get("name"));
            }
            anime.setGenres(genres);

            check = animeData.opt("synopsis");
            if (check.toString() != "null") {
                anime.setSynopsis((String) check);
            }

            anime.setHummigbirdUrl((String) animeData.get("url"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return anime;
    }

    public interface AsyncResultListener {
        void getSearchResult(List<Anime> animes);
    }
}