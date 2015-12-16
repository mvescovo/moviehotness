package com.michaelvescovo.moviehotness.view_movies.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.util.BitmapHelper;
import com.michaelvescovo.moviehotness.view_movies.entity.MoviePreviewInterface;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Michael on 25/11/15.
 *
 */
public class PosterAdapter extends RecyclerView.Adapter implements Serializable {
    private static final String TAG = "PosterAdapter";
    public int mSortBy = -1;
    private ArrayList<MoviePreviewInterface> mDataset = new ArrayList<>();
    BitmapHelper mBitmapHelper = new BitmapHelper();

    public PosterAdapter(int sortBy) {
        mSortBy = sortBy;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_grid_card, parent, false);
        return new MovieViewHolder(v, mDataset);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageView imageView = (ImageView) ((MovieViewHolder) holder).getView().findViewById(R.id.poster_image);
        mBitmapHelper.loadBitmap(imageView, imageView.getContext().getFilesDir() + "/" + "preview_poster_" + mDataset.get(position).getId());
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