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

package com.michaelvescovo.android.moviehotness.view_movies;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.michaelvescovo.android.moviehotness.R;
import com.michaelvescovo.android.moviehotness.data.Movie;
import com.michaelvescovo.android.moviehotness.data.MovieHotnessContract;
import com.michaelvescovo.android.moviehotness.data.MovieInterface;
import com.michaelvescovo.android.moviehotness.data.MovieRepositories;
import com.michaelvescovo.android.moviehotness.util.EspressoIdlingResource;
import com.michaelvescovo.android.moviehotness.util.VolleyRequestQueue;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ViewMoviesFragment extends Fragment implements ViewMoviesContract.View, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String SORT_BY = "SORT_BY";
    public static final String CURRENT_PAGE = "CURRENT_PAGE";
    public static final String NEXT_PAGE = "NEXT_PAGE";
    public static final String PREVIOUS_TOTAL = "PREVIOUS_TOTAL";
    public static final String TOTAL_ITEM_COUNT = "TOTAL_ITEM_COUNT";
    public static final String LOADING = "LOADING";
    public static final String SHOWN_TOP_MOVIE = "SHOWN_TOP_MOVIE";
    public static final String CONNECTED = "CONNECTED";
    private ViewMoviesContract.UserActionsListener mActionsListener;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private Callback mCallback;
    public int mSortBy = -1;
    private int mCurrentPage = 0;
    private int mNextPage = 1;
    private int mPreviousTotal = 0;
    private int mTotalItemCount = 0;
    private int mLastVisibleItem;
    private boolean mLoading = true;
    private boolean mShownTopMovie = false;
    private boolean mConnected = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSortBy = -1;
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            mSortBy = savedInstanceState.getInt(SORT_BY);
            mCurrentPage = savedInstanceState.getInt(CURRENT_PAGE);
            mNextPage = savedInstanceState.getInt(NEXT_PAGE);
            mPreviousTotal = savedInstanceState.getInt(PREVIOUS_TOTAL);
            mTotalItemCount = savedInstanceState.getInt(TOTAL_ITEM_COUNT);
            mLoading = savedInstanceState.getBoolean(LOADING);
            mShownTopMovie = savedInstanceState.getBoolean(SHOWN_TOP_MOVIE);
            mConnected = savedInstanceState.getBoolean(CONNECTED);

        } else {
            if (getArguments() != null) {
                mSortBy = getArguments().getInt("sortBy");
            }
        }

        mActionsListener = new ViewMoviesPresenter(getContext(), MovieRepositories.getMovieRepository(getContext(), mSortBy), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // If viewing favourites create cursor adapter, otherwise create arraylist adapter
        if (mSortBy == getContext().getResources().getInteger(R.integer.favourite)) {
            mAdapter = new PosterCursorAdapter();
        } else {
            mAdapter = new PosterApiAdapter();
        }

        View root = inflater.inflate(R.layout.fragment_view_movies, container, false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.view_movies_list);
        recyclerView.setAdapter(mAdapter);

        int numColumns = getContext().getResources().getInteger(R.integer.movie_preview_grid_cols);

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), numColumns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    // user scrolled down
                    mLastVisibleItem = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                    if (mLastVisibleItem == mTotalItemCount - 1) {
                        // user scrolled to the bottom
                        if (!mLoading) {
                            loadMovies(mNextPage);
                        }
                    }
                }
            }
        });

        // Pull-to-refresh
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mSortBy != getResources().getInteger(R.integer.favourite)) {
                    mCurrentPage = 0;
                    mNextPage = 1;
                    mTotalItemCount = 0;
                    mPreviousTotal = 0;
                    mActionsListener.loadMovies(mSortBy, true, mNextPage);

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

//        Log.i(TAG, "onResume: sortBy: " + mSortBy);
//        Log.i(TAG, "onResume: currentPage: " + mCurrentPage);
//        Log.i(TAG, "onResume: nextPage: " + mNextPage);
//        Log.i(TAG, "onResume: previousTotal: " + mPreviousTotal);
//        Log.i(TAG, "onResume: totalItemCount: " + mTotalItemCount);
//        Log.i(TAG, "onResume: loading: " + mLoading);

        if (mLoading) {
            loadMovies(mNextPage);
        } else {
            loadMovies(mCurrentPage);
        }
    }

    public void loadMovies(int page) {
        mLoading = true;
        // Only load movies from API if not viewing favourites
        if ((mSortBy != -1) && (mSortBy != getContext().getResources().getInteger(R.integer.favourite))) {
            if (!isOnline()) {
                showSnackbar(getResources().getString(R.string.network_not_connected));
            } else {
            }
            mActionsListener.loadMovies(mSortBy, false, page);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SORT_BY, mSortBy);
        outState.putInt(CURRENT_PAGE, mCurrentPage);
        outState.putInt(NEXT_PAGE, mNextPage);
        outState.putInt(PREVIOUS_TOTAL, mPreviousTotal);
        outState.putInt(TOTAL_ITEM_COUNT, mTotalItemCount);
        outState.putBoolean(LOADING, mLoading);
        outState.putBoolean(SHOWN_TOP_MOVIE, mShownTopMovie);
        outState.putBoolean(CONNECTED, mConnected);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mLoading) {
            if (mSortBy != getContext().getResources().getInteger(R.integer.favourite)) {
                setProgressIndicator(false);
                VolleyRequestQueue.getInstance(getContext()).getRequestQueue().cancelAll(mSortBy);
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement();
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Required to avoid frozen old recyclerview on top of the new one.
        // Happens when using ViewPager after fragment is destroyed and recreated.
        mLayoutManager.removeAllViews();
    }

    @Override
    public void setProgressIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }

        final SwipeRefreshLayout srl = (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

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
            ((PosterApiAdapter) mAdapter).updateDataset(movies);
        }
        mLoading = false;
        mTotalItemCount = mLayoutManager.getItemCount();
        if (mTotalItemCount > mPreviousTotal) {
            // new paged was loaded
            mPreviousTotal = mTotalItemCount;
            mCurrentPage++;
            mNextPage++;
        }

        if (getResources().getBoolean(R.bool.two_pane) && (mSortBy == getContext().getResources().getInteger(R.integer.popular))) {
            if (movies.size() != 0) {
                showTopMovie(movies.get(0));
            }
        }
    }

    @Override
    public void showTopMovie(MovieInterface movie) {
        if (!mShownTopMovie) {
            showMovieDetailUi(null, movie.getId());
            mShownTopMovie = true;
        }
    }

    @Override
    public void showMovieDetailUi(View sharedView, String movieId) {
        mCallback.onItemSelected(sharedView, mSortBy, movieId);
    }

    @Override
    public void showAttributionUi() {
        mCallback.onAboutSelected();
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
        ((PosterCursorAdapter) mAdapter).swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ((PosterCursorAdapter) mAdapter).swapCursor(null);
    }

    public void networkChanged(boolean connected) {
        if (connected) {
            if (!mConnected) {
                mConnected = true;
                if (mLoading) {
                    loadMovies(mNextPage);
                }
            }
        } else {
            showSnackbar(getResources().getString(R.string.network_not_connected));
            mConnected = false;
            if (mSortBy != getContext().getResources().getInteger(R.integer.favourite)) {
                setProgressIndicator(false);
                VolleyRequestQueue.getInstance(getContext()).getRequestQueue().cancelAll(mSortBy);
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement();
                }
            }
        }
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        int snackbarTextId = android.support.design.R.id.snackbar_text;
        TextView textView = (TextView) snackbarView.findViewById(snackbarTextId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextColor(getResources().getColor(R.color.black, getResources().newTheme()));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            snackbarView.setBackgroundColor(getResources().getColor(R.color.white, getResources().newTheme()));
        }
        snackbar.show();
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
        void onItemSelected(View sharedView, int sortBy, String movieId);

        void onAboutSelected();
    }

    private class PosterApiAdapter extends RecyclerView.Adapter {

        private List<MovieInterface> mDataset = new ArrayList<>();

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_preview_card,
                    parent, false);
            return new MovieApiViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ImageView imageView = (ImageView) ((MovieApiViewHolder) holder).getView()
                    .findViewById(R.id.poster_image);
            Picasso.with(imageView.getContext()).load("https://image.tmdb.org/t/p/"
                    + imageView.getContext().getResources().getString(R.string.poster_large)
                    + mDataset.get(position).getPosterUrl()).error(R.drawable.no_image)
                    .into(imageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });
            ViewCompat.setTransitionName(
                    imageView,
                    imageView.getResources().getString(R.string.transition_poster)
            );
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        void updateDataset(List<MovieInterface> movies) {
            mDataset.clear();
            mDataset.addAll(movies);
            notifyDataSetChanged();
        }

        class MovieApiViewHolder extends RecyclerView.ViewHolder {

            private View mView;

            MovieApiViewHolder(View itemView) {
                super(itemView);
                mView = itemView;

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mActionsListener != null)
                            mActionsListener.openMovieDetails(v.findViewById(R.id.poster_image),
                                    mDataset.get(getAdapterPosition()));
                    }
                });
            }

            public View getView() {
                return mView;
            }
        }
    }

    private class PosterCursorAdapter extends RecyclerView.Adapter {

        private Cursor mCursor;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_preview_card, parent, false);
            return new MovieCursorViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            // Posters are from local storage when getting details from local db. PosterUrl holds the full filename.
            ImageView imageView = (ImageView) ((MovieCursorViewHolder) holder).getView().findViewById(R.id.poster_image);
            int posterUrlColumnIndex = mCursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_POSTER_URL);
            String posterUrl;

            if (mCursor != null) {
                mCursor.moveToPosition(position);
                posterUrl = mCursor.getString(posterUrlColumnIndex);

                String filename = posterUrl;
                File file = new File(getContext().getFilesDir(), filename);
                Picasso.with(getContext()).load(file).error(R.drawable.no_image).into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });
            }
            ViewCompat.setTransitionName(
                    imageView,
                    imageView.getResources().getString(R.string.transition_poster)
            );
        }

        @Override
        public int getItemCount() {
            if (mCursor != null) {
                return mCursor.getCount();
            } else {
                return 0;
            }
        }

        void swapCursor(Cursor movies) {
            mCursor = movies;
            notifyDataSetChanged();
        }

        class MovieCursorViewHolder extends RecyclerView.ViewHolder {

            private View mView;

            MovieCursorViewHolder(View itemView) {
                super(itemView);
                mView = itemView;

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int idIndex = mCursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_MOVIE_ID);
                        int titleIndex = mCursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_TITLE);
                        int releaseDateIndex = mCursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_RELEASE_DATE);
                        int posterUrlIndex = mCursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_POSTER_URL);
                        int voteAverageIndex = mCursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_VOTE_AVERAGE);
                        int plotIndex = mCursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_PLOT);
                        int backdropUrlIndex = mCursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_BACKDROP_URL);

                        mCursor.moveToPosition(getAdapterPosition());
                        String id = mCursor.getString(idIndex);
                        String title = mCursor.getString(titleIndex);
                        String releaseDate = mCursor.getString(releaseDateIndex);
                        String posterUrl = mCursor.getString(posterUrlIndex);
                        String voteAverage = mCursor.getString(voteAverageIndex);
                        String plot = mCursor.getString(plotIndex);
                        String backdropUrl = mCursor.getString(backdropUrlIndex);
                        Movie movie = new Movie(id, title, releaseDate, posterUrl, voteAverage, plot, backdropUrl);

                        if (mActionsListener != null) {
                            mActionsListener.openMovieDetails(v.findViewById(R.id.poster_image), movie);
                        }
                    }
                });
            }

            public View getView() {
                return mView;
            }
        }
    }
}