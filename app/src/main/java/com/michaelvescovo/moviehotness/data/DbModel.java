package com.michaelvescovo.moviehotness.data;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 4/12/15.
 *
 */
public class DbModel extends DataModel {

    private static final String TAG = "DbModel";

    @Override
    public synchronized void getMovies(@NonNull Context context, @NonNull Integer sortBy, @NonNull Integer page, @NonNull LoadMoviesCallback callback) {
        if (successor != null) {
            successor.getMovies(context, sortBy, page, callback);
        } else {
            List<MovieInterface> cachedMovies = new ArrayList<>();

            Cursor cursor = context.getContentResolver().query(MovieHotnessContract.MovieEntry.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int idIndex = cursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_MOVIE_ID);
                    int titleIndex = cursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_TITLE);
                    int releaseDateIndex = cursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_RELEASE_DATE);
                    int posterUrlIndex = cursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_POSTER_URL);
                    int voteAverageIndex = cursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_VOTE_AVERAGE);
                    int plotIndex = cursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_PLOT);
                    int backdropUrlIndex = cursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_BACKDROP_URL);
                    String id = cursor.getString(idIndex);
                    String title = cursor.getString(titleIndex);
                    String releaseDate = cursor.getString(releaseDateIndex);
                    String posterUrl = cursor.getString(posterUrlIndex);
                    String voteAverage = cursor.getString(voteAverageIndex);
                    String plot = cursor.getString(plotIndex);
                    String backdropUrl = cursor.getString(backdropUrlIndex);
                    MovieInterface movie = new Movie(id, title, releaseDate, posterUrl, voteAverage, plot, backdropUrl);
                    cachedMovies.add(movie);
                }
                cursor.close();
            }

            callback.onMoviesLoaded(cachedMovies);
        }
    }

    @Override
    public void getMovie(@NonNull Context context, @NonNull String movieId, @NonNull GetMovieCallback callback) {
        if (successor != null) {
            successor.getMovie(context, movieId, callback);
        } else {
            Cursor cursor = context.getContentResolver().query(MovieHotnessContract.MovieEntry.CONTENT_URI, null, null, null, null);
            MovieInterface movie = null;

            if (cursor != null) {
                int idIndex = cursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_MOVIE_ID);
                int titleIndex = cursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_TITLE);
                int releaseDateIndex = cursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_RELEASE_DATE);
                int posterUrlIndex = cursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_POSTER_URL);
                int voteAverageIndex = cursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_VOTE_AVERAGE);
                int plotIndex = cursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_PLOT);
                int backdropUrlIndex = cursor.getColumnIndex(MovieHotnessContract.MovieEntry.COLUMN_BACKDROP_URL);

                while (cursor.moveToNext()) {
                    String id = cursor.getString(idIndex);

                    if (id.contentEquals(movieId)) {
                        String title = cursor.getString(titleIndex);
                        String releaseDate = cursor.getString(releaseDateIndex);
                        String posterUrl = cursor.getString(posterUrlIndex);
                        String voteAverage = cursor.getString(voteAverageIndex);
                        String plot = cursor.getString(plotIndex);
                        String backdropUrl = cursor.getString(backdropUrlIndex);
                        movie = new Movie(id, title, releaseDate, posterUrl, voteAverage, plot, backdropUrl);

                        // get trailers
                        String trailerSelection = MovieHotnessContract.TrailerEntry.COLUMN_MOVIE_ID + " = ?";
                        String[] trailerArgs = {movieId};
                        Cursor trailerCursor = context.getContentResolver().query(MovieHotnessContract.TrailerEntry.CONTENT_URI, null, trailerSelection, trailerArgs, null);
                        if (trailerCursor != null) {
                            int youtubeidIndex = trailerCursor.getColumnIndex(MovieHotnessContract.TrailerEntry.COLUMN_YOUTUBE_ID);
                            int nameIndex = trailerCursor.getColumnIndex(MovieHotnessContract.TrailerEntry.COLUMN_NAME);

                            while (trailerCursor.moveToNext()) {
                                String youtubeid = trailerCursor.getString(youtubeidIndex);
                                String name = trailerCursor.getString(nameIndex);
                                MovieTrailer trailer = new MovieTrailer(youtubeid, name);
                                Log.i(TAG, "getMovie: trailer name: " + name);
                                movie.addTrailer(trailer);
                            }
                            trailerCursor.close();
                        }


                        // get reviews
                        String reviewSelection = MovieHotnessContract.ReviewEntry.COLUMN_MOVIE_ID + " = ?";
                        String[] reviewArgs = {movieId};
                        Cursor reviewCursor = context.getContentResolver().query(MovieHotnessContract.ReviewEntry.CONTENT_URI, null, reviewSelection, reviewArgs, null);
                        if (reviewCursor != null) {
                            int authorIndex = reviewCursor.getColumnIndex(MovieHotnessContract.ReviewEntry.COLUMN_AUTHOR);
                            int contentIndex = reviewCursor.getColumnIndex(MovieHotnessContract.ReviewEntry.COLUMN_CONTENT);

                            while (reviewCursor.moveToNext()) {
                                String author = reviewCursor.getString(authorIndex);
                                String content = reviewCursor.getString(contentIndex);
                                MovieReview review = new MovieReview(author, content);
                                movie.addReview(review);
                            }
                            reviewCursor.close();
                        }

                        break;
                    }
                }
                cursor.close();
            }
            callback.onMovieLoaded(movie);
        }
    }

    @Override
    public void refreshData() {

    }
}
