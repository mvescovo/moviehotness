package com.michaelvescovo.moviehotness.view_movies.view;


import com.michaelvescovo.moviehotness.view_movies.entity.MoviePreviewInterface;

/**
 * Created by Michael on 4/12/15.
 *
 */
public interface PresenterInterface {
    void displayMovies(MoviePreviewInterface movie, int sortBy, int resultsSize);
}
