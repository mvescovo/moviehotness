package com.michaelvescovo.moviehotness.view_movies;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.michaelvescovo.moviehotness.model.MovieInterface;

import java.util.ArrayList;

/**
 * Created by Michael on 9/12/15.
 *
 */
public class MovieViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "MovieViewHolder";
    private View mView;
    private ViewMoviesContract.UserActionsListener mActionsListener;

    public MovieViewHolder(View itemView, final ArrayList<MovieInterface> movies, ViewMoviesContract.UserActionsListener userActionsListener) {
        super(itemView);
        mView = itemView;
        mActionsListener = userActionsListener;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActionsListener != null)
                    mActionsListener.openMovieDetails(movies.get(getAdapterPosition()));
            }
        });
    }

    public View getView() {
        return mView;
    }
}