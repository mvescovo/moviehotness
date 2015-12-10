package com.michaelvescovo.moviehotness.view_movies;

import com.michaelvescovo.moviehotness.view_movies.data.DataRequestInterface;
import com.michaelvescovo.moviehotness.view_movies.data.DataResponseInterface;
import com.michaelvescovo.moviehotness.view_movies.entity.MovieInterface;
import com.michaelvescovo.moviehotness.view_movies.view.PresenterInterface;

/**
 * Created by Michael on 4/12/15.
 *
 */
public class ViewMovies implements ViewMoviesInterface, DataResponseInterface {
    private static final String TAG = "ViewMovies";
    private PresenterInterface mPresenterInterface;
    private DataRequestInterface mDataRequestInterface;
    private int mSortBy;

    public ViewMovies(PresenterInterface presenterInterface, DataRequestInterface dataRequestInterface, int sortBy) {
        setPresenterInterface(presenterInterface);
        setDataRequestInterface(dataRequestInterface);
        setSortBy(sortBy);
    }

    @Override
    public void setSortBy(int sortBy) {

    }

    @Override
    public void setDataRequestInterface(DataRequestInterface dataRequestInterface) {
        mDataRequestInterface = dataRequestInterface;
    }

    @Override
    public void setPresenterInterface(PresenterInterface presenterInterface) {
        mPresenterInterface = presenterInterface;
    }

    @Override
    public void getMovies(int sortBy) {
        mDataRequestInterface.getMovies(sortBy);
    }

    @Override
    public void displayMovies(MovieInterface movie, int sortBy) {
        mPresenterInterface.displayMovies(movie, sortBy);

        // TODO maybe here call to save to memory model and db model
    }
}
