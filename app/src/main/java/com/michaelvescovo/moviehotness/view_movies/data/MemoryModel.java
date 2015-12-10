package com.michaelvescovo.moviehotness.view_movies.data;

import com.michaelvescovo.moviehotness.view_movies.Constants;
import com.michaelvescovo.moviehotness.view_movies.entity.MovieInterface;

import java.util.ArrayList;

/**
 * Created by Michael on 4/12/15.
 *
 */
public class MemoryModel extends DataModel {
    private ArrayList<MovieInterface> mPopularMovies;
    private ArrayList<MovieInterface> mHighestRatedMovies;

    @Override
    public void getMovies(int sortBy) {
        switch (sortBy) {
            case Constants.POPULAR:
                if (mPopularMovies != null) {
                    for (int i = 0; i < mPopularMovies.size(); i++) {
                        mDataResponseInterface.displayMovies(mPopularMovies.get(i), sortBy);
                    }
                } else if (successor != null) {
                    successor.getMovies(sortBy);
                }
                break;
            case Constants.HIGHEST_RATED:
                if (mHighestRatedMovies != null) {
                    for (int i = 0; i < mHighestRatedMovies.size(); i++) {
                        mDataResponseInterface.displayMovies(mPopularMovies.get(i), sortBy);
                    }
                } else if (successor != null) {
                    successor.getMovies(sortBy);
                }
        }
    }
}
