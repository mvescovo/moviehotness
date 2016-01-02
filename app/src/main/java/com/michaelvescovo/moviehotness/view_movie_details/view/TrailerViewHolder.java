package com.michaelvescovo.moviehotness.view_movie_details.view;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.youtube.player.YouTubeIntents;
import com.michaelvescovo.moviehotness.view_movie_details.entity.MovieTrailerInterface;

import java.util.ArrayList;

/**
 * Created by Michael on 17/12/15.
 *
 */
public class TrailerViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "TrailerViewHolder";
    private View mView;
    private ArrayList<MovieTrailerInterface> mAdapter;

    public TrailerViewHolder(View itemView, ArrayList<MovieTrailerInterface> adapter) {
        super(itemView);
        mView = itemView;
        mAdapter = adapter;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String youTubeId = mAdapter.get(getAdapterPosition()).getYouTubeId();
                Intent intent = YouTubeIntents.createPlayVideoIntentWithOptions(v.getContext().getApplicationContext(), youTubeId, true, true);
                v.getContext().startActivity(intent);
            }
        });
    }

    public View getView() {
        return mView;
    }
}