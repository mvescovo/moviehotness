package com.michaelvescovo.moviehotness.view_movies.view;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.michaelvescovo.moviehotness.view_movie_details.view.DetailActivity;
import com.michaelvescovo.moviehotness.view_movies.entity.MoviePreviewInterface;

import java.util.ArrayList;

/**
 * Created by Michael on 9/12/15.
 *
 */
public class MovieViewHolder extends RecyclerView.ViewHolder {
    private View mView;

    public MovieViewHolder(View itemView, final ArrayList<MoviePreviewInterface> movies) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra("movie_id", movies.get(getAdapterPosition()).getId());
                intent.putExtra("position", getAdapterPosition());
                v.getContext().startActivity(intent);
            }
        });
    }

    public View getView() {
        return mView;
    }
}