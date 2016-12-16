package com.michaelvescovo.android.moviehotness.view_all_reviews;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.michaelvescovo.android.moviehotness.data.MovieReviewInterface;

import java.util.List;


/**
 * Created by Michael Vescovo on 14/01/16.
 *
 */
public class ReviewViewHolder extends RecyclerView.ViewHolder {

    private ViewAllReviewsFragment mViewAllReviewsFragment;
    private View mView;
    private List<MovieReviewInterface> mAdapter;

    public ReviewViewHolder(ViewAllReviewsFragment viewAllReviewsFragment, final View itemView, List<MovieReviewInterface> adapter) {
        super(itemView);
        mViewAllReviewsFragment = viewAllReviewsFragment;
        mView = itemView;
        mAdapter = adapter;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewAllReviewsFragment.mReviewSelectedCallback.onFullReviewSelected(
                        v,
                        mAdapter.get(getAdapterPosition()).getAuthor(),
                        mAdapter.get(getAdapterPosition()).getContent()
                );
            }
        });
    }

    public View getView() {
        return mView;
    }
}
