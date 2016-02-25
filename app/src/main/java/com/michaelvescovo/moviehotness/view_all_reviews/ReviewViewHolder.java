package com.michaelvescovo.moviehotness.view_all_reviews;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.michaelvescovo.moviehotness.data.MovieReviewInterface;
import com.michaelvescovo.moviehotness.view_full_review.ViewFullReviewActivity;
import com.michaelvescovo.moviehotness.view_movies.ViewMoviesActivity;

import java.util.ArrayList;

/**
 * Created by Michael Vescovo on 14/01/16.
 *
 */
public class ReviewViewHolder extends RecyclerView.ViewHolder {

    private ViewAllReviewsFragment mViewAllReviewsFragment;
    private View mView;
    private ArrayList<MovieReviewInterface> mAdapter;

    public ReviewViewHolder(ViewAllReviewsFragment viewAllReviewsFragment, final View itemView, ArrayList<MovieReviewInterface> adapter) {
        super(itemView);
        mViewAllReviewsFragment = viewAllReviewsFragment;
        mView = itemView;
        mAdapter = adapter;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ViewMoviesActivity.mTwoPane) {
                    mViewAllReviewsFragment.mReviewSelectedCallback.onFullReviewSelected(mAdapter.get(getAdapterPosition()).getAuthor(), mAdapter.get(getAdapterPosition()).getContent());
                } else {
                    Intent intent = new Intent(v.getContext(), ViewFullReviewActivity.class);
                    intent.putExtra(ViewFullReviewActivity.AUTHOR, mAdapter.get(getAdapterPosition()).getAuthor());
                    intent.putExtra(ViewFullReviewActivity.CONTENT, mAdapter.get(getAdapterPosition()).getContent());
                    v.getContext().startActivity(intent);
                }
            }
        });
    }

    public View getView() {
        return mView;
    }
}
