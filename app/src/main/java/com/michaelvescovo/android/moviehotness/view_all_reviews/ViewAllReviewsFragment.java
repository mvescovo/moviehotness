package com.michaelvescovo.android.moviehotness.view_all_reviews;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.michaelvescovo.android.moviehotness.R;
import com.michaelvescovo.android.moviehotness.data.MovieReviewInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Michael Vescovo on 22/02/16.
 *
 */
public class ViewAllReviewsFragment extends Fragment {

    public ReviewSelectedCallback mReviewSelectedCallback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_view_all_reviews, container, false);
        List<MovieReviewInterface> reviews = (List<MovieReviewInterface>) getArguments().getSerializable(ViewAllReviewsActivity.REVIEWS);
        RecyclerView.Adapter adapter = new ReviewAdapter(this, reviews);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.all_reviews);
        ((ViewAllReviewsActivity)getActivity()).setSupportActionBar(toolbar);
        if (((ViewAllReviewsActivity)getActivity()).getSupportActionBar() != null) {
            ((ViewAllReviewsActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((ViewAllReviewsActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);
        }

        return root;
    }

    public static ViewAllReviewsFragment newInstance(ArrayList<MovieReviewInterface> reviews) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(ViewAllReviewsActivity.REVIEWS, reviews);
        ViewAllReviewsFragment fragment = new ViewAllReviewsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mReviewSelectedCallback = (ReviewSelectedCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ReviewSelectedCallback");
        }
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of detail clicks.
     */
    public interface ReviewSelectedCallback {
        void onFullReviewSelected(View sharedView, String author, String content);
    }

    private class ReviewAdapter extends RecyclerView.Adapter implements Serializable {

        private List<MovieReviewInterface> mDataset = new ArrayList<>();
        private ViewAllReviewsFragment mViewAllReviewsFragment;

        public ReviewAdapter(ViewAllReviewsFragment viewAllReviewsFragment, List<MovieReviewInterface> dataset) {
            mViewAllReviewsFragment = viewAllReviewsFragment;
            mDataset = dataset;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_card, parent, false);
            return new ReviewViewHolder(mViewAllReviewsFragment, v, mDataset);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (mDataset.size() > position + 1) {
                View border = ((ReviewViewHolder) holder).getView().findViewById(R.id.border);
                border.setVisibility(View.VISIBLE);
            }

            CardView cardView = (CardView) ((ReviewViewHolder)holder).getView();
            ViewCompat.setTransitionName(cardView, mViewAllReviewsFragment.getString(R.string.transition_review));

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

    private class ReviewViewHolder extends RecyclerView.ViewHolder {

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
}
