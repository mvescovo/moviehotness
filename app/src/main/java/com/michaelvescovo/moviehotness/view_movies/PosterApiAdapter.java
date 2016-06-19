package com.michaelvescovo.moviehotness.view_movies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.data.MovieInterface;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 25/11/15.
 *
 */
public class PosterApiAdapter extends RecyclerView.Adapter {

    private ArrayList<MovieInterface> mDataset = new ArrayList<>();
    private ViewMoviesContract.UserActionsListener mActionsListener;

    public PosterApiAdapter(ViewMoviesContract.UserActionsListener userActionsListener) {
        mActionsListener = userActionsListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_preview_card, parent, false);
        return new MovieApiViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageView imageView = (ImageView) ((MovieApiViewHolder) holder).getView().findViewById(R.id.poster_image);
        Picasso.with(imageView.getContext()).load("https://image.tmdb.org/t/p/" + imageView.getContext().getResources().getString(R.string.poster_medium) + mDataset.get(position).getPosterUrl()).error(R.drawable.no_image).into(imageView, new Callback() {
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

    public class MovieApiViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        public MovieApiViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mActionsListener != null)
                        mActionsListener.openMovieDetails(mDataset.get(getAdapterPosition()));
                }
            });
        }

        public View getView() {
            return mView;
        }
    }
}