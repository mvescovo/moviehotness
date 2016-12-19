package com.michaelvescovo.android.moviehotness.view_full_review;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.michaelvescovo.android.moviehotness.R;


/**
 * Created by Michael Vescovo on 22/02/16.
 *
 */
public class ViewFullReviewFragment extends Fragment {

    private ViewFullReviewFragment.Callback mCallback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_view_full_review, container, false);
        NestedScrollView nestedScrollView = (NestedScrollView) root.findViewById(R.id.full_review_list);
        ViewCompat.setTransitionName(nestedScrollView, getString(R.string.transition_review));
        TextView author = (TextView) root.findViewById(R.id.full_review_author);
        author.setText(getArguments().getString(ViewFullReviewActivity.AUTHOR));
        TextView content = (TextView) root.findViewById(R.id.full_review_content);
        content.setText(getArguments().getString(ViewFullReviewActivity.CONTENT));

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.full_review);

        mCallback.onSetSupportActionbar(toolbar, true, R.drawable.ic_close_24dp);

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (ViewFullReviewFragment.Callback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ViewFullReviewFragment Callback");
        }
    }

    public static ViewFullReviewFragment newInstance(String author,String content) {
        Bundle arguments = new Bundle();
        arguments.putString(ViewFullReviewActivity.AUTHOR, author);
        arguments.putString(ViewFullReviewActivity.CONTENT, content);
        ViewFullReviewFragment fragment = new ViewFullReviewFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public interface Callback {

        void onSetSupportActionbar(@NonNull Toolbar toolbar, @NonNull Boolean upEnabled,
                                   @Nullable Integer homeAsUpIndicator);

        void onAboutSelected();
    }
}
