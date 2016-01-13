package com.michaelvescovo.moviehotness.view_all_reviews;

import com.michaelvescovo.moviehotness.model.MovieInterface;
import com.michaelvescovo.moviehotness.model.MovieReviewInterface;
import com.michaelvescovo.moviehotness.model.MovieTrailerInterface;

import java.util.ArrayList;

/**
 * Created by Michael Vescovo on 13/01/16.
 *
 */
public interface ViewAllReviewsContract {
    interface View {

        void showMovieDetails(MovieInterface movie);

        void showFirstTrailerUi(String youTubeId);

        void showAllReviewsUi(String title, String plot);

        void showAllTrailersUi(ArrayList<MovieTrailerInterface> trailers);

        void showFullReview(String author, String content);

        void showAllReviewsUi(ArrayList<MovieReviewInterface> reviews);

        void showAttributionUi();

        void showMissingMovie();
    }

    interface UserActionsListener {

        void loadMovieDetails(String movieId, boolean forceUpdate);

        void openFirstTrailer(String youTubeId);

        void openFullPlot(String title, String plot);

        void openAllTrailers(ArrayList<MovieTrailerInterface> trailers);

        void openAttribution();
    }
}
