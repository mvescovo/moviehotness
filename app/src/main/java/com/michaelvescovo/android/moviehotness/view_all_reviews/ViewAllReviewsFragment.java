package com.michaelvescovo.android.moviehotness.view_all_reviews;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.michaelvescovo.android.moviehotness.R;
import com.michaelvescovo.android.moviehotness.data.MovieReviewInterface;

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
}
