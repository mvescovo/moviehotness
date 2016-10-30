package com.michaelvescovo.moviehotness.view_full_review;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.view_movies.ViewMoviesActivity;

/**
 * Created by Michael Vescovo on 22/02/16.
 *
 */
public class ViewFullReviewFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_view_full_review, container, false);
        TextView author = (TextView) root.findViewById(R.id.full_review_author);
        author.setText(getArguments().getString(ViewFullReviewActivity.AUTHOR));
        TextView content = (TextView) root.findViewById(R.id.full_review_content);
        content.setText(getArguments().getString(ViewFullReviewActivity.CONTENT));

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.full_review);

        if (!ViewMoviesActivity.mTwoPane) {
            ((ViewFullReviewActivity)getActivity()).setSupportActionBar(toolbar);
            if (((ViewFullReviewActivity)getActivity()).getSupportActionBar() != null) {
                ((ViewFullReviewActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ((ViewFullReviewActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);
            }
        }
        return root;
    }

    public static ViewFullReviewFragment newInstance(String author,String content) {
        Bundle arguments = new Bundle();
        arguments.putString(ViewFullReviewActivity.AUTHOR, author);
        arguments.putString(ViewFullReviewActivity.CONTENT, content);
        ViewFullReviewFragment fragment = new ViewFullReviewFragment();
        fragment.setArguments(arguments);
        return fragment;
    }
}