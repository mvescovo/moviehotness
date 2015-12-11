package com.michaelvescovo.moviehotness.view_movies.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.view_movies.entity.MoviePreviewInterface;

import java.util.ArrayList;

/**
 * Created by Michael on 25/11/15.
 *
 */
public class PosterAdapter extends RecyclerView.Adapter {
    private Context mContext;
    public int mSortBy = -1;
    private ArrayList<MoviePreviewInterface> mDataset = new ArrayList<>();

    public PosterAdapter(Context c, int sortBy) {
        mContext = c;
        mSortBy = sortBy;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_grid_card, parent, false);
        return new MovieViewHolder(mContext, v, mDataset);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageView imageView = (ImageView) ((MovieViewHolder) holder).getView().findViewById(R.id.poster_image);
        imageView.setImageBitmap(mDataset.get(position).getPoster());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void updateDataset(MoviePreviewInterface movie) {
        mDataset.add(movie);
        notifyItemInserted(getItemCount() - 1);
    }
}