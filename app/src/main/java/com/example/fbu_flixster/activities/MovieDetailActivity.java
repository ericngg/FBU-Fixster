package com.example.fbu_flixster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.fbu_flixster.R;
import com.example.fbu_flixster.databinding.ActivityMainBinding;
import com.example.fbu_flixster.databinding.ActivityMovieDetailBinding;
import com.example.fbu_flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieDetailActivity extends AppCompatActivity {

    private ActivityMovieDetailBinding activityMovieDetailBinding;
    public static final String VIDEO_URL_1 = "https://api.themoviedb.org/3/movie/";
    public static final String VIDEO_URL_2 = "/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US";
    public static final String TAG = "MovieDetailActivity";
    private Map<Integer, String> key_map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMovieDetailBinding = ActivityMovieDetailBinding.inflate(getLayoutInflater());
        View view = activityMovieDetailBinding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        // Poster picture
        String imageUrl;
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageUrl = intent.getStringExtra("posterL");
        } else {
            imageUrl = intent.getStringExtra("posterP");
        }
        Glide.with(this).load(imageUrl).into(activityMovieDetailBinding.ivMoviePoster);

        activityMovieDetailBinding.tvMovieTitle.setText(intent.getStringExtra("title"));
        activityMovieDetailBinding.tvSynopsis.setText(intent.getStringExtra("synopsis"));

        Double rating = intent.getDoubleExtra("rating", 1d);

        activityMovieDetailBinding.rbRating.setRating((float) (rating / 2));

        activityMovieDetailBinding.rlPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = intent.getIntExtra("id", 1);
                if (key_map.keySet().contains(id)) {
                    Intent intent = new Intent(MovieDetailActivity.this, MovieTrailerActivity.class);
                    intent.putExtra("key", key_map.get(id));
                    startActivity(intent);
                } else {
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.get(VIDEO_URL_1 + id + VIDEO_URL_2, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Headers headers, JSON json) {
                            Log.d(TAG, "onSuccess");
                            JSONObject jsonObject = json.jsonObject;
                            try {
                                JSONArray results = jsonObject.getJSONArray("results");
                                for (int j = 0; j < results.length(); j++) {
                                    if (results.getJSONObject(j).getString("site").equals("YouTube")) {
                                        String key = results.getJSONObject(0).getString("key");
                                        key_map.put(id, key);
                                        Intent intent = new Intent(MovieDetailActivity.this, MovieTrailerActivity.class);
                                        intent.putExtra("key", key);
                                        startActivity(intent);
                                    }
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, "Hit json exception", e);
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                            Log.d(TAG, "onFailure");
                        }
                    });
                }
            }
        });
    }
}