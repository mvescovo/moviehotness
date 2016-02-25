package com.michaelvescovo.moviehotness.view_all_trailers;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeIntents;
import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.data.MovieTrailerInterface;

import java.util.ArrayList;

/**
 * Created by Michael on 17/12/15.
 *
 */
public class TrailerViewHolder extends RecyclerView.ViewHolder {

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

                if (YouTubeIntents.isYouTubeInstalled(v.getContext())) {
                    Intent intent = YouTubeIntents.createPlayVideoIntentWithOptions(v.getContext().getApplicationContext(), youTubeId, true, true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                } else {
                    Snackbar snackbar = Snackbar.make(v, v.getContext().getResources().getString(R.string.youtube_not_installed), Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    int snackbarTextId = android.support.design.R.id.snackbar_text;
                    TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        textView.setTextColor(v.getContext().getResources().getColor(R.color.black, v.getContext().getResources().newTheme()));
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        snackbarView.setBackgroundColor(v.getContext().getResources().getColor(R.color.white, v.getContext().getResources().newTheme()));
                    }
                    snackbar.show();
                }
            }
        });
    }

    public View getView() {
        return mView;
    }
}