package com.michaelvescovo.android.moviehotness.view_all_trailers;

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
import com.michaelvescovo.android.moviehotness.data.MovieTrailerInterface;
import com.michaelvescovo.android.moviehotness.view_movies.ViewMoviesActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Michael Vescovo on 22/02/16.
 *
 */
public class ViewAllTrailersFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_view_all_trailers, container, false);
        List<MovieTrailerInterface> trailers = (List<MovieTrailerInterface>) getArguments().getSerializable(ViewAllTrailersActivity.TRAILERS);
        RecyclerView.Adapter adapter = new TrailerAdapter(trailers);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.all_trailers);

        if (!ViewMoviesActivity.mTwoPane) {
            ((ViewAllTrailersActivity)getActivity()).setSupportActionBar(toolbar);
            if (((ViewAllTrailersActivity)getActivity()).getSupportActionBar() != null) {
                ((ViewAllTrailersActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ((ViewAllTrailersActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);
            }
        }
        return root;
    }

    public static ViewAllTrailersFragment newInstance(ArrayList<MovieTrailerInterface> trailers) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(ViewAllTrailersActivity.TRAILERS, trailers);
        ViewAllTrailersFragment fragment = new ViewAllTrailersFragment();
        fragment.setArguments(arguments);
        return fragment;
    }
}
