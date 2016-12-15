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

package com.michaelvescovo.android.moviehotness.view_movie_details;

import com.michaelvescovo.android.moviehotness.data.MovieInterface;
import com.michaelvescovo.android.moviehotness.data.MovieReviewInterface;
import com.michaelvescovo.android.moviehotness.data.MovieTrailerInterface;

import java.util.ArrayList;

/**
 * Created by Michael Vescovo on 6/01/16.
 *
 */
public interface ViewMovieDetailsContract {
    interface View {

        void showMovieDetails(MovieInterface movie);

        void showFirstTrailerUi(String youTubeId);

        void showFullPlotUi(android.view.View sharedView, String title, String plot);

        void showAllTrailersUi(ArrayList<MovieTrailerInterface> trailers);

        void showFullReview(android.view.View sharedView, String author, String content);

        void showAllReviewsUi(ArrayList<MovieReviewInterface> reviews);

        void showAttributionUi();

        void showMissingMovie();

        void setFavouriteFab(int imageResource, boolean addFavourite);

        void showSnackbar(int stringResource);
    }

    interface UserActionsListener {

        void loadMovieDetails(String movieId, boolean forceUpdate);

        void playFirstTrailer(String youTubeId);

        void openFullPlot(android.view.View sharedView, String title, String plot);

        void openAllTrailers(ArrayList<MovieTrailerInterface> trailers);

        void openFullReview(android.view.View sharedView, String author, String content);

        void openAllReviews(ArrayList<MovieReviewInterface> reviews);

        void openAttribution();

        void loadFavouriteFab(String movieId);

        void addFavouriteMovie(MovieInterface movie);

        void removeFavouriteMovie(String movieId);
    }
}
