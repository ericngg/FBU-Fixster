package com.example.fbu_flixster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fbu_flixster.R;
import com.example.fbu_flixster.models.Movie;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    static Context context;
    List<Movie> movies;
    ViewHolder.OnClickListener onClickListener;

    public MovieAdapter (Context context, List<Movie> movies, ViewHolder.OnClickListener onClickListener) {
        this.context = context;
        this.movies = movies;
        this.onClickListener = onClickListener;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView, onClickListener);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the movie at the passed in position
        Movie movie = movies.get(position);
        // Bind the movie data into the VH
        holder.bind(movie);
    }

    // Return the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        OnClickListener onClickListener;

        public ViewHolder(@NonNull View itemView, OnClickListener onClickListener) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            this.onClickListener = onClickListener;

            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageUrl;
            // if phone is in landscape
            // then set imageUrl = backdrop image
            // else imageUrl = poster image
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageUrl = movie.getBackdropPath();
            } else {
                imageUrl = movie.getPosterPath();
            }

            Glide.with(context).load(imageUrl).into(ivPoster);
            //Glide.with(context).load(imageUrl).placeholder(R.mipmap.place_holder).into(ivPoster);
            // placeholder is buggy on my emulator, but works on my physical phone. I will just emit it.
            //Glide.with(context).load(imageUrl).transform(new RoundedCornersTransformation(1, 0)).into(ivPoster);
            // glide transformation is buggy on my emulator, but works on my physical phone. I will just emit it.
        }

        @Override
        public void onClick(View v) {
            onClickListener.onMovieClick(getAdapterPosition());
        }

        public interface OnClickListener {
            void onMovieClick(int position);
        }
    }
}
