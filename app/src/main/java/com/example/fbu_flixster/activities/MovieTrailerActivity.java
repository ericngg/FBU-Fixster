package com.example.fbu_flixster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.fbu_flixster.R;
import com.example.fbu_flixster.databinding.ActivityMovieTrailerBinding;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    private ActivityMovieTrailerBinding activityMovieTrailerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMovieTrailerBinding = activityMovieTrailerBinding.inflate(getLayoutInflater());
        View view = activityMovieTrailerBinding.getRoot();
        setContentView(view);

        // temporary test video id
        Intent intent = getIntent();
        final String videoId = intent.getStringExtra("key");

        // resolve the player view from the layout

        // initialize with API key stored in secrets.xml
        activityMovieTrailerBinding.player.initialize(getString(R.string.YT_API_KEY), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                // do any work here to cue video, play video, etc.
                youTubePlayer.loadVideo(videoId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                // log the error
                Log.e("MovieTrailerActivity", youTubeInitializationResult.toString());
            }
        });
    }
}