package com.michaelvescovo.moviehotness.view_all_trailers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.michaelvescovo.moviehotness.BuildConfig;
import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.model.MovieTrailerInterface;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Michael on 17/12/15.
 *
 */
public class TrailerAdapter extends RecyclerView.Adapter implements Serializable {

    private ArrayList<MovieTrailerInterface> mDataset = new ArrayList<>();

    public TrailerAdapter(ArrayList<MovieTrailerInterface> dataset) {
        mDataset = dataset;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_card, parent, false);
        return new TrailerViewHolder(v, mDataset);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        TextView textViewName = (TextView) ((TrailerViewHolder) holder).getView().findViewById(R.id.trailer_name);
        YouTubeThumbnailView youTubeThumbnailView = (YouTubeThumbnailView) ((TrailerViewHolder) holder).getView().findViewById(R.id.youTube_thumbnail_trailer);

        if (textViewName != null) {
            textViewName.setText(mDataset.get(position).getName());
        }

        if (youTubeThumbnailView != null) {
            youTubeThumbnailView.initialize(BuildConfig.YOU_TUBE_DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    youTubeThumbnailLoader.setVideo(mDataset.get(position).getYouTubeId());
                    if (youTubeThumbnailLoader.hasPrevious()) {
                        youTubeThumbnailLoader.release();
                    }
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}