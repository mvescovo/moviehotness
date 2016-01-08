/*
 * Copyright 2015, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * I've changed the code but it's similar to the example I'm following.
 * Possibly the best Android example I've ever seen so far. Really nice design.
 *
 * https://codelabs.developers.google.com/codelabs/android-testing
 *
 */

package com.michaelvescovo.moviehotness.view_movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.michaelvescovo.moviehotness.Injection;
import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.model.MovieInterface;
import com.michaelvescovo.moviehotness.view_attribution.AttributionActivity;
import com.michaelvescovo.moviehotness.view_movie_details.ViewMovieDetailsActivity;

import java.util.List;

public class ViewMoviesFragment extends Fragment implements ViewMoviesContract.View {

    private RecyclerView.Adapter mAdapter;
    private int mSortBy = -1;
    private ViewMoviesContract.UserActionsListener mActionsListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            mSortBy = savedInstanceState.getInt("sortBy");
            mAdapter = (RecyclerView.Adapter) savedInstanceState.getSerializable("adapter");
        } else {
            if (getArguments() != null) {
                mSortBy = getArguments().getInt("sortBy");
            }

            mActionsListener = new ViewMoviesPresenter(getContext(), Injection.provideMovieRepository(getContext(), mSortBy), this);
            mAdapter = new PosterAdapter(getContext(), mActionsListener);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_view_movies, container, false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.view_movies_list);
        recyclerView.setAdapter(mAdapter);

        int numColumns = getContext().getResources().getInteger(R.integer.movie_preview_grid_cols);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numColumns));

        // Set up floating action button
//        FloatingActionButton fab =
//                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_notes);
//
//        fab.setImageResource(R.drawable.ic_add);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mActionsListener.addNewNote();
//            }
//        });
//
        // Pull-to-refresh
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mActionsListener.loadMovies(mSortBy, true);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mActionsListener.loadMovies(mSortBy, false);
    }

    @Override
    public void setProgressIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl = (SwipeRefreshLayout)getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showMovies(List<MovieInterface> movies) {
        if (mAdapter != null) {
            ((PosterAdapter)mAdapter).updateDataset(movies);
        }
    }

    @Override
    public void showMovieDetailUi(String movieId) {
        Intent intent = new Intent(getContext(), ViewMovieDetailsActivity.class);
        intent.putExtra("SORT_BY", mSortBy);
        intent.putExtra("MOVIE_ID", movieId);
        startActivity(intent);
    }

    @Override
    public void showAttributionUi() {
        Intent intent = new Intent(getContext(), AttributionActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            showAttributionUi();
        }

        return super.onOptionsItemSelected(item);
    }
}