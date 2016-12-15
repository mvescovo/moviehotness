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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.michaelvescovo.android.moviehotness.R;
import com.michaelvescovo.android.moviehotness.data.MovieHotnessContract;
import com.michaelvescovo.android.moviehotness.data.MovieInterface;
import com.michaelvescovo.android.moviehotness.data.MovieRepository;
import com.michaelvescovo.android.moviehotness.data.MovieReviewInterface;
import com.michaelvescovo.android.moviehotness.data.MovieTrailerInterface;
import com.michaelvescovo.android.moviehotness.util.EspressoIdlingResource;
import com.michaelvescovo.android.moviehotness.util.VolleyRequestQueue;

import java.io.FileOutputStream;
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
        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        EspressoIdlingResource.increment(); // App is busy until further notice
        mMovieRepository.getMovie(mContext, movieId, new MovieRepository.GetMovieCallback() {
            @Override
            public void onMovieLoaded(MovieInterface movie) {
                if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement();
                }
                if (movie == null) {
                    mViewMovieDetailsView.showMissingMovie();
                } else {
                    mViewMovieDetailsView.showMovieDetails(movie);
                }
            }
        });
    }

    @Override
    public void playFirstTrailer(String youTubeId) {
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
    public void openFullReview(View sharedView, String author, String content) {
        mViewMovieDetailsView.showFullReview(sharedView, author, content);
    }

    @Override
    public void openAllReviews(ArrayList<MovieReviewInterface> reviews) {
        mViewMovieDetailsView.showAllReviewsUi(reviews);
    }

    @Override
    public void openAttribution() {
        mViewMovieDetailsView.showAttributionUi();
    }

    @Override
    public void loadFavouriteFab(String movieId) {
        String selection = MovieHotnessContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
        String[] selectionArgs = {movieId};
        Cursor cursor = mContext.getContentResolver().query(MovieHotnessContract.MovieEntry.CONTENT_URI, null, selection, selectionArgs, null);

        if (cursor != null) {
            if (cursor.getCount() == 0) {
                mViewMovieDetailsView.setFavouriteFab(R.drawable.ic_favorite_white_24dp, true);
            } else {
                mViewMovieDetailsView.setFavouriteFab(R.drawable.ic_remove_24dp, false);
            }

            cursor.close();
        }
    }

    @Override
    public void addFavouriteMovie(final MovieInterface movie) {
        // Save movie poster to local storage
        final String filename = movie.getId() + ".png";
        ImageRequest request = new ImageRequest("https://image.tmdb.org/t/p/" + mContext.getResources().getString(R.string.poster_large) + movie.getPosterUrl(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        FileOutputStream outputStream;
                        try {
                            outputStream = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                            outputStream.close();
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, 0, 0, null, Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                    }
                });
        VolleyRequestQueue.getInstance(mContext).addToRequestQueue(request);
        // Save movie to local db using local file as the poster url
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieHotnessContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(MovieHotnessContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(MovieHotnessContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(MovieHotnessContract.MovieEntry.COLUMN_POSTER_URL, filename);
        contentValues.put(MovieHotnessContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        contentValues.put(MovieHotnessContract.MovieEntry.COLUMN_PLOT, movie.getPlot());
        contentValues.put(MovieHotnessContract.MovieEntry.COLUMN_BACKDROP_URL, movie.getBackdropUrl());
        mContext.getContentResolver().insert(MovieHotnessContract.MovieEntry.CONTENT_URI, contentValues);

        if (movie.getTrailerCount() > 0) {
            for (int i = 0; i < movie.getTrailerCount(); i++) {
                contentValues = new ContentValues();
                contentValues.put(MovieHotnessContract.TrailerEntry.COLUMN_MOVIE_ID, movie.getId());
                contentValues.put(MovieHotnessContract.TrailerEntry.COLUMN_YOUTUBE_ID, movie.getTrailer(i).getYouTubeId());
                contentValues.put(MovieHotnessContract.TrailerEntry.COLUMN_NAME, movie.getTrailer(i).getName());
                mContext.getContentResolver().insert(MovieHotnessContract.TrailerEntry.CONTENT_URI, contentValues);
            }
        }

        if (movie.getReviewCount() > 0) {
            for (int i = 0; i < movie.getReviewCount(); i++) {
                contentValues = new ContentValues();
                contentValues.put(MovieHotnessContract.ReviewEntry.COLUMN_MOVIE_ID, movie.getId());
                contentValues.put(MovieHotnessContract.ReviewEntry.COLUMN_AUTHOR, movie.getReview(i).getAuthor());
                contentValues.put(MovieHotnessContract.ReviewEntry.COLUMN_CONTENT, movie.getReview(i).getContent());
                mContext.getContentResolver().insert(MovieHotnessContract.ReviewEntry.CONTENT_URI, contentValues);
            }
        }

        mViewMovieDetailsView.showSnackbar(R.string.fragment_detail_favourite_added);
        mViewMovieDetailsView.setFavouriteFab(R.drawable.ic_remove_24dp, false);
    }

    @Override
    public void removeFavouriteMovie(String movieId) {
        // Delete poster from local storage
        mContext.deleteFile(movieId);
        // Delete the local db row
        String selection = MovieHotnessContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
        String[] selectionArgs = {movieId};
        mContext.getContentResolver().delete(MovieHotnessContract.MovieEntry.CONTENT_URI, selection, selectionArgs);
        mViewMovieDetailsView.showSnackbar(R.string.fragment_detail_favourite_removed);
        mViewMovieDetailsView.setFavouriteFab(R.drawable.ic_favorite_white_24dp, true);
    }
}
