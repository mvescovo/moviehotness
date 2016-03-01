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
import android.support.annotation.NonNull;

import com.michaelvescovo.moviehotness.data.MovieInterface;
import com.michaelvescovo.moviehotness.data.MovieRepository;
import com.michaelvescovo.moviehotness.util.EspressoIdlingResource;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Michael Vescovo on 5/01/16.
 *
 */
public class ViewMoviesPresenter implements ViewMoviesContract.UserActionsListener {

    private final MovieRepository mMovieRepository;
    private final ViewMoviesContract.View mViewMoviesView;
    private Context mContext;

    public ViewMoviesPresenter(@NonNull Context context, @NonNull MovieRepository movieRepository, @NonNull ViewMoviesContract.View viewMoviesView) {
        mContext = context;
        mMovieRepository = checkNotNull(movieRepository, "movieRepository cannot be null");
        mViewMoviesView = checkNotNull(viewMoviesView, "viewMoviesView cannot be null");
    }

    @Override
    public void loadMovies(int sortBy, boolean forceUpdate, int page) {
        mViewMoviesView.setProgressIndicator(true);
        if (forceUpdate) {
            mMovieRepository.refreshData();
        }
        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        EspressoIdlingResource.increment(); // App is busy until further notice
        mMovieRepository.getMovies(mContext, sortBy, page, new MovieRepository.LoadMoviesCallback() {
            @Override
            public void onMoviesLoaded(List<MovieInterface> movies) {
                EspressoIdlingResource.decrement(); // Set app as idle.
                mViewMoviesView.setProgressIndicator(false);
                mViewMoviesView.showMovies(movies);
            }
        });
    }

    @Override
    public void openMovieDetails(@NonNull MovieInterface requestedMovie) {
        mViewMoviesView.showMovieDetailUi(requestedMovie.getId());
    }
}
