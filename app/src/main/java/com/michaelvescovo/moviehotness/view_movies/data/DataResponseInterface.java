package com.michaelvescovo.moviehotness.view_movies.data;

import com.michaelvescovo.moviehotness.view_movies.entity.MoviePreviewInterface;

/**
 * Created by Michael on 9/12/15.
 *
 */
public interface DataResponseInterface {
    void displayMovies(MoviePreviewInterface movie, int sortBy, int resultsSize);
}
