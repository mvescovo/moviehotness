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

package com.michaelvescovo.moviehotness.view_movie_details;

import android.content.Context;
import android.support.annotation.NonNull;

import com.michaelvescovo.moviehotness.model.MovieInterface;
import com.michaelvescovo.moviehotness.model.MovieRepository;
import com.michaelvescovo.moviehotness.model.MovieReviewInterface;
import com.michaelvescovo.moviehotness.model.MovieTrailerInterface;
import com.michaelvescovo.moviehotness.util.EspressoIdlingResource;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Michael Vescovo on 6/01/16.
 *
 */
public class ViewMovieDetailsPresenter implements ViewMovieDetailsContract.UserActionsListener {
    private Context mContext;
    private final MovieRepository mMovieRepository;
    private final ViewMovieDetailsContract.View mViewMovieDetailsView;

    public ViewMovieDetailsPresenter(@NonNull Context context, @NonNull MovieRepository movieRepository, @NonNull ViewMovieDetailsContract.View viewMovieDetailsView) {
        mContext = context;
        mMovieRepository = checkNotNull(movieRepository, "movieRepository cannot be null");
        mViewMovieDetailsView = checkNotNull(viewMovieDetailsView, "viewMovieDetailsView cannot be null");
    }

    @Override
    public void loadMovieDetails(String movieId, boolean forceUpdate) {
        mViewMovieDetailsView.setProgressIndicator(true);

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        EspressoIdlingResource.increment(); // App is busy until further notice
        mMovieRepository.getMovie(mContext, movieId, new MovieRepository.GetMovieCallback() {
            @Override
            public void onMovieLoaded(MovieInterface movie) {
                EspressoIdlingResource.decrement(); // Set app as idle.
                mViewMovieDetailsView.setProgressIndicator(false);

                if (movie == null) {
                    mViewMovieDetailsView.showMissingMovie();
                } else {
                    mViewMovieDetailsView.showMovieDetails(movie);
                }
            }
        });
    }

    @Override
    public void openFirstTrailer(String youTubeId) {
        mViewMovieDetailsView.showFirstTrailerUi(youTubeId);
    }

    @Override
    public void openFullPlot(String title, String plot) {
        mViewMovieDetailsView.showFullPlotUi(title, plot);
    }

    @Override
    public void openAllTrailers(ArrayList<MovieTrailerInterface> trailers) {
        mViewMovieDetailsView.showAllTrailersUi(trailers);
    }

    @Override
    public void openFullReview(String author, String content) {
        mViewMovieDetailsView.showFullReview(author, content);
    }

    @Override
    public void openAllReviews(ArrayList<MovieReviewInterface> reviews) {
        mViewMovieDetailsView.showAllReviewsUi(reviews);
    }

    @Override
    public void openAttribution() {
        mViewMovieDetailsView.showAttributionUi();
    }
}
