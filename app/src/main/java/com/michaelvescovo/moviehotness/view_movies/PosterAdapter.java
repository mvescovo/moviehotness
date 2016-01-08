package com.michaelvescovo.moviehotness.view_movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.model.MovieInterface;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 25/11/15.
 *
 */
public class PosterAdapter extends RecyclerView.Adapter implements Serializable {

    private ArrayList<MovieInterface> mDataset = new ArrayList<>();
    private Context mContext;
    private ViewMoviesContract.UserActionsListener mActionsListener;

    public PosterAdapter(Context context, ViewMoviesContract.UserActionsListener userActionsListener) {
        mContext = context;
        mActionsListener = userActionsListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_preview_card, parent, false);
        return new MovieViewHolder(v, mDataset, mActionsListener);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageView imageView = (ImageView) ((MovieViewHolder) holder).getView().findViewById(R.id.poster_image);
        Picasso.with(mContext).load("https://image.tmdb.org/t/p/" + mContext.getResources().getString(R.string.poster_large) + mDataset.get(position).getPosterUrl()).into(imageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void updateDataset(List<MovieInterface> movies) {
        mDataset.clear();
        mDataset.addAll(movies);
        notifyDataSetChanged();
    }
}