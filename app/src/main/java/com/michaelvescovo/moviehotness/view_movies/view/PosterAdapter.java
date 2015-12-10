package com.michaelvescovo.moviehotness.view_movies.view;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.view_movies.entity.MovieInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Michael on 25/11/15.
 *
 */
public class PosterAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<MovieInterface> mDataset = new ArrayList<>();

    public PosterAdapter(Context c) {
        mContext = c;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_grid_card, parent, false);
        return new MovieViewHolder(mContext, v, mDataset);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageView imageView = (ImageView) ((MovieViewHolder) holder).getView().findViewById(R.id.poster_image);
        final String BASE_URL = "https://image.tmdb.org/t/p";

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath("w185")
                .build();

        Picasso.with(mContext)
                .load(builtUri.toString() + mDataset.get(position).getPosterUrl())
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void updateDataset(MovieInterface movie) {
        mDataset.add(movie);
        notifyItemInserted(getItemCount());
    }
}