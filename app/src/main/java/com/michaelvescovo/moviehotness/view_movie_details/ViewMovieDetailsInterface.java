package com.michaelvescovo.moviehotness.view_movie_details;

import com.michaelvescovo.moviehotness.view_movie_details.data.DataRequestInterface;
import com.michaelvescovo.moviehotness.view_movie_details.view.PresenterInterface;
import com.michaelvescovo.moviehotness.view_movies.entity.MoviePreviewInterface;

/**
 * Created by Michael on 6/12/15.
 *
 */
public interface ViewMovieDetailsInterface {
    void setDataRequestInterface(DataRequestInterface dataRequestInterface);

    void getMovie(int movieId);

    void setPresenterInterface(PresenterInterface presenterInterface);

    void displayMovie(MoviePreviewInterface movie);
}
