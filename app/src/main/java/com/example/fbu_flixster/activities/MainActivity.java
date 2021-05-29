package com.example.fbu_flixster.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.fbu_flixster.R;
import com.example.fbu_flixster.adapters.MovieAdapter;
import com.example.fbu_flixster.databinding.ActivityMainBinding;
import com.example.fbu_flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ViewHolder.OnClickListener {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=";
    public static final String TAG = "MainActivity";

    List<Movie> movies;
    MovieAdapter movieAdapter;
    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);
        movies = new ArrayList<>();

        // Create the adapter
        movieAdapter = new MovieAdapter(this, movies, this);
        // Set the adapter on the recycler view
        activityMainBinding.rvMovies.setAdapter(movieAdapter);
        // Set a Layout Manager on the recycler view
        activityMainBinding.rvMovies.setLayoutManager(new LinearLayoutManager(this));


        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL + getString(R.string.MOVIE_API_KEY), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());
                    movies.addAll(Movie.fromJsonArray(results));
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Movies: " + movies.size());
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

    @Override
    public void onMovieClick(int position) {
        Movie movie = movies.get(position);
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("title", movie.getTitle());
        intent.putExtra("posterP", movie.getPosterPath());
        intent.putExtra("rating", movie.getRating());
        intent.putExtra("synopsis", movie.getOverview());
        intent.putExtra("posterL", movie.getBackdropPath());
        intent.putExtra("id", movie.getId());
        startActivity(intent);
    }
}