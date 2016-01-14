package com.michaelvescovo.moviehotness.view_all_reviews;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.michaelvescovo.moviehotness.model.MovieReviewInterface;
import com.michaelvescovo.moviehotness.view_full_review.ViewFullReviewActivity;

import java.util.ArrayList;

/**
 * Created by Michael Vescovo on 14/01/16.
 *
 */
public class ReviewViewHolder extends RecyclerView.ViewHolder {

    private View mView;
    private ArrayList<MovieReviewInterface> mAdapter;

    public ReviewViewHolder(final View itemView, ArrayList<MovieReviewInterface> adapter) {
        super(itemView);
        mView = itemView;
        mAdapter = adapter;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewFullReviewActivity.class);
                intent.putExtra(ViewFullReviewActivity.EXTRA_AUTHOR, mAdapter.get(getAdapterPosition()).getAuthor());
                intent.putExtra(ViewFullReviewActivity.EXTRA_CONTENT, mAdapter.get(getAdapterPosition()).getContent());
                v.getContext().startActivity(intent);
            }
        });
    }

    public View getView() {
        return mView;
    }
}
