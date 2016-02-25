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

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.data.MovieHotnessContract;
import com.michaelvescovo.moviehotness.data.MovieInterface;
import com.michaelvescovo.moviehotness.data.MovieRepositories;
import com.michaelvescovo.moviehotness.view_attribution.AttributionActivity;
import com.michaelvescovo.moviehotness.view_movie_details.ViewMovieDetailsActivity;

import java.util.List;

public class ViewMoviesFragment extends Fragment implements ViewMoviesContract.View, LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView.Adapter mAdapter;
    public int mSortBy = -1;
    private ViewMoviesContract.UserActionsListener mActionsListener;
    private  Callback mCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSortBy = -1;
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            mSortBy = savedInstanceState.getInt("sortBy");
        } else {
            if (getArguments() != null) {
                mSortBy = getArguments().getInt("sortBy");
            }
        }

        mActionsListener = new ViewMoviesPresenter(getContext(), MovieRepositories.getMovieRepository(getContext(), mSortBy), this);

        // If viewing favourites create cursor adapter, otherwise create arraylist adapter
        if (mSortBy == getContext().getResources().getInteger(R.integer.favourite)) {
            mAdapter = new PosterCursorAdapter(getContext(), mActionsListener);
        } else {
            mAdapter = new PosterApiAdapter(getContext(), mActionsListener);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_view_movies, container, false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.view_movies_list);
        recyclerView.setAdapter(mAdapter);

        int numColumns;

        if (((ViewMoviesActivity) getActivity()).mTwoPane) {
            numColumns = getContext().getResources().getInteger(R.integer.movie_preview_grid_cols_twopane);
        } else {
            numColumns = getContext().getResources().getInteger(R.integer.movie_preview_grid_cols_onepane);
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numColumns));

        // Pull-to-refresh
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mSortBy != getContext().getResources().getInteger(R.integer.favourite)) {
                    mActionsListener.loadMovies(mSortBy, true);
                } else {
                    setProgressIndicator(false);
                }
            }
        });

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (Callback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement Callback");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);

        // Only use loader when viewing favourites
        if (mSortBy == getContext().getResources().getInteger(R.integer.favourite)) {
            getLoaderManager().initLoader(0, null, this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Only load movies from API if not viewing favourites
        if (mSortBy != getContext().getResources().getInteger(R.integer.favourite)) {
            mActionsListener.loadMovies(mSortBy, false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("sortBy", mSortBy);
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
            ((PosterApiAdapter)mAdapter).updateDataset(movies);
        }
    }

    @Override
    public void showTopMovie(MovieInterface movie) {
        showMovieDetailUi(movie.getId());
    }

    @Override
    public void showMovieDetailUi(String movieId) {
        if (((ViewMoviesActivity) getActivity()).mTwoPane) {
            mCallback.onItemSelected(mSortBy, movieId);
        } else {
            Intent intent = new Intent(getContext(), ViewMovieDetailsActivity.class);
            intent.putExtra("SORT_BY", mSortBy);
            intent.putExtra("MOVIE_ID", movieId);
            startActivity(intent);
        }
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MovieHotnessContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ((PosterCursorAdapter)mAdapter).swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((PosterCursorAdapter)mAdapter).swapCursor(null);
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(int sortBy, String movieId);
    }
}