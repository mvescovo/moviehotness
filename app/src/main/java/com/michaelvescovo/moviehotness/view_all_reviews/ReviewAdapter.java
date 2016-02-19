package com.michaelvescovo.moviehotness.view_all_reviews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.data.MovieReviewInterface;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Michael Vescovo on 14/01/16.
 *
 */
public class ReviewAdapter extends RecyclerView.Adapter implements Serializable {

    private ArrayList<MovieReviewInterface> mDataset = new ArrayList<>();

    public ReviewAdapter(ArrayList<MovieReviewInterface> dataset) {
        mDataset = dataset;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_card, parent, false);
        return new ReviewViewHolder(v, mDataset);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        TextView authorLabel = (TextView) ((ReviewViewHolder) holder).getView().findViewById(R.id.review_author_label);
        authorLabel.setVisibility(View.VISIBLE);
        TextView author = (TextView) ((ReviewViewHolder) holder).getView().findViewById(R.id.review_author);
        author.setText(mDataset.get(position).getAuthor());
        author.setVisibility(View.VISIBLE);
        TextView content = (TextView) ((ReviewViewHolder) holder).getView().findViewById(R.id.review_content);
        content.setText(mDataset.get(position).getContent());
        content.setVisibility(View.VISIBLE);
        if (content.length() > ((ReviewViewHolder) holder).getView().getResources().getInteger(R.integer.preview_text_max_chars)) {
            TextView reviewContentReadMore = (TextView) ((ReviewViewHolder) holder).getView().findViewById(R.id.review_content_read_more);
            reviewContentReadMore.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
