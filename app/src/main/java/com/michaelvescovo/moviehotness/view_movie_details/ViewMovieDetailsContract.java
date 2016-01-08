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

import com.michaelvescovo.moviehotness.model.MovieInterface;
import com.michaelvescovo.moviehotness.model.MovieTrailerInterface;

import java.util.ArrayList;

/**
 * Created by Michael Vescovo on 6/01/16.
 *
 */
public interface ViewMovieDetailsContract {
    interface View {

        void setProgressIndicator(boolean active);

        void showMovieDetails(MovieInterface movie);

        void showFirstTrailerUi(String youTubeId);

        void showFullPlotUi(String title, String plot);

        void showAllTrailersUi(ArrayList<MovieTrailerInterface> trailers);

        void showAttributionUi();
    }

    interface UserActionsListener {

        void loadMovieDetails(String movieId, boolean forceUpdate);

        void openFirstTrailer(String youTubeId);

        void openFullPlot(String title, String plot);

        void openAllTrailers(ArrayList<MovieTrailerInterface> trailers);

        void openAttribution();
    }
}
