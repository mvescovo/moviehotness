package com.michaelvescovo.moviehotness.data;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.michaelvescovo.moviehotness.BuildConfig;
import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.util.VolleyRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 4/12/15.
 *
 */
public class CloudModel extends DataModel {

    private Context mContext;
    private int mResultsSize;
    private int mDownloaded;

    private interface DownloadMovieCallback {
        void onMovieReceived(MovieInterface movie);
        void onFail();
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public synchronized void getMovies(@NonNull Context context, @NonNull Integer sortBy, @NonNull final LoadMoviesCallback callback) {
        mContext = context;
        mResultsSize = 0;
        mDownloaded = 0;
        final List<MovieInterface> movies = new ArrayList<>();
        String url = "";

        if (sortBy.equals(mContext.getResources().getInteger(R.integer.popular))) {
            url = "https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=" + BuildConfig.THE_MOVIE_DB_API_KEY;
        } else if (sortBy.equals(mContext.getResources().getInteger(R.integer.highest_rated))) {
            url = "https://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=" + BuildConfig.THE_MOVIE_DB_API_KEY;
        }

        if (isOnline()) {
            downloadMovies(url, new DownloadMovieCallback() {
                @Override
                public void onMovieReceived(MovieInterface movie) {
                    movies.add(movie);
                    mDownloaded++;
                    if (mResultsSize == mDownloaded) {
                        callback.onMoviesLoaded(movies);
                    }
                }

                @Override
                public void onFail() {
                    callback.onMoviesLoaded(null);
                }
            });
        } else {
            Snackbar snackbar = Snackbar.make(((Activity)mContext).findViewById(R.id.toolbar), mContext.getResources().getString(R.string.network_not_connected), Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            int snackbarTextId = android.support.design.R.id.snackbar_text;
            TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView.setTextColor(mContext.getResources().getColor(R.color.black, mContext.getResources().newTheme()));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                snackbarView.setBackgroundColor(mContext.getResources().getColor(R.color.white, mContext.getResources().newTheme()));
            }
            snackbar.show();

            callback.onMoviesLoaded(null);
        }
    }

    public void downloadMovies(String url, final DownloadMovieCallback downloadMovieCallback) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        final JSONArray results;

                        try {
                            results = response.getJSONArray("results");
                            mResultsSize = results.length();

                            for (int i = 0; i < results.length(); i++) {
                                String id = results.getJSONObject(i).getString("id");
                                String title = results.getJSONObject(i).getString("original_title");
                                String releaseDate = results.getJSONObject(i).getString("release_date");
                                String posterUrl = results.getJSONObject(i).getString("poster_path");
                                String backdropUrl = results.getJSONObject(i).getString("backdrop_path");
                                String voteAverage = results.getJSONObject(i).getString("vote_average");
                                String plot = results.getJSONObject(i).getString("overview");
                                MovieInterface movie = new Movie(id, title, releaseDate, posterUrl, voteAverage, plot, backdropUrl);
                                getTrailers(movie, downloadMovieCallback);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        downloadMovieCallback.onFail();
                        error.printStackTrace();
                    }
                });
        VolleyRequestQueue.getInstance(mContext).addToRequestQueue(jsObjRequest);
    }

    @Override
    public void getMovie(@NonNull Context context, @NonNull String movieId, @NonNull final GetMovieCallback callback) {
        mContext = context;

        downloadMovie("http://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + BuildConfig.THE_MOVIE_DB_API_KEY, new DownloadMovieCallback() {
            @Override
            public void onMovieReceived(MovieInterface movie) {
                callback.onMovieLoaded(movie);
            }

            @Override
            public void onFail() {
                callback.onMovieLoaded(null);
            }
        });
    }

    public void downloadMovie(String url, final DownloadMovieCallback downloadMovieCallback) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String id = response.getString("id");
                            String title = response.getString("original_title");
                            String releaseDate = response.getString("release_date");
                            String posterUrl = response.getString("poster_path");
                            String backdropUrl = response.getString("backdrop_path");
                            String voteAverage = response.getString("vote_average");
                            String plot = response.getString("overview");
                            MovieInterface movie = new Movie(id, title, releaseDate, posterUrl, voteAverage, plot, backdropUrl);
                            getTrailers(movie, downloadMovieCallback);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        downloadMovieCallback.onFail();
                        error.printStackTrace();
                    }
                });
        VolleyRequestQueue.getInstance(mContext).addToRequestQueue(jsObjRequest);
    }

    public void getTrailers(final MovieInterface movie, final DownloadMovieCallback downloadMovieCallback) {
        String url = "http://api.themoviedb.org/3/movie/" + movie.getId() + "/videos" + "?api_key=" + BuildConfig.THE_MOVIE_DB_API_KEY;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final JSONArray results;

                try {
                    results = response.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        String trailerYouTubeId = results.getJSONObject(i).getString("key");
                        String trailerName = results.getJSONObject(i).getString("name");
                        MovieTrailerInterface movieTrailer = new MovieTrailer(trailerYouTubeId, trailerName);
                        movie.addTrailer(movieTrailer);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getReviews(movie, downloadMovieCallback);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                getReviews(movie, downloadMovieCallback);
            }
        });
        VolleyRequestQueue.getInstance(mContext).addToRequestQueue(jsObjRequest);
    }

    public void getReviews(final MovieInterface movie, final DownloadMovieCallback downloadMovieCallback) {
        String url = "http://api.themoviedb.org/3/movie/" + movie.getId() + "/reviews" + "?api_key=" + BuildConfig.THE_MOVIE_DB_API_KEY;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final JSONArray results;

                try {
                    results = response.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        String author = results.getJSONObject(i).getString("author");
                        String content = results.getJSONObject(i).getString("content");
                        MovieReviewInterface movieReview = new MovieReview(author, content);
                        movie.addReview(movieReview);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                downloadMovieCallback.onMovieReceived(movie);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                downloadMovieCallback.onMovieReceived(movie);
            }
        });
        VolleyRequestQueue.getInstance(mContext).addToRequestQueue(jsObjRequest);
    }

    @Override
    public void refreshData() {
        // nothing to do
    }
}