package com.michaelvescovo.android.moviehotness.data;

import java.util.ArrayList;

/**
 * Created by Michael on 11/12/15.
 *
 */
public interface MovieInterface {
    void setId(String id);

    String getId();

    void setTitle(String title);

    String getTitle();

    void setReleaseDate(String releaseDate);

    String getReleaseDate();

    void setPosterUrl(String posterUrl);

    String getPosterUrl();

    void setVoteAverage(String voteAverage);

    String getVoteAverage();

    void setPlot(String plot);

    String getPlot();

    void setBackdropUrl(String backdropUrl);

    String getBackdropUrl();

    void addTrailer(MovieTrailerInterface trailer);

    MovieTrailerInterface getTrailer(int index);

    ArrayList<MovieTrailerInterface> getTrailers();

    int getTrailerCount();

    void addReview(MovieReviewInterface review);

    MovieReviewInterface getReview(int index);

    ArrayList<MovieReviewInterface> getReviews();

    int getReviewCount();
}
