package com.michaelvescovo.moviehotness.view_movie_details.data;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.michaelvescovo.moviehotness.BuildConfig;
import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.util.VolleyRequestQueue;
import com.michaelvescovo.moviehotness.view_movie_details.entity.Movie;
import com.michaelvescovo.moviehotness.view_movie_details.entity.MovieInterface;
import com.michaelvescovo.moviehotness.view_movies.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;

/**
 * Created by Michael on 7/12/15.
 *
 */
public class CloudModel extends DataModel {
    private static final String TAG = "CloudModel";
    private Context mContext;

    public CloudModel(Context context) {
        mContext = context;
    }

    @Override
    public void getMovie(String movieId) {
        downloadList("http://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + BuildConfig.THE_MOVIE_DB_API_KEY);
    }

    public void downloadList(final String url) {
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
                            String backdrop = response.getString("backdrop_path");
                            MovieInterface movie = new Movie(id, title, releaseDate, posterUrl, voteAverage, plot, backdrop);
                            getBackdrop(backdropUrl, movie);
                        } catch (JSONException e) {
                            Log.e(TAG, "onResponse: " + e);
                            Log.i(TAG, "onResponse: url: " + url);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "onErrorResponse: " + error);
                    }
                });
        VolleyRequestQueue.getInstance(mContext).addToRequestQueue(jsObjRequest);
    }

    public void getBackdrop(final String url, final MovieInterface movie) {
        final String BASE_URL = "https://image.tmdb.org/t/p";
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(Constants.POSTER_LARGE)
                .build();

        ImageRequest request = new ImageRequest(builtUri.toString() + url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        String FILENAME = "backdrop_" + movie.getId();
                        try{
                            FileOutputStream fos = mContext.openFileOutput(FILENAME, Context.MODE_PRIVATE);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.close();
                        } catch (java.io.IOException e) {
                            Log.i(TAG, "onResponse: error: " + e);
                        }
                        getPoster(movie.getPosterUrl(), movie);
                    }
                }, 0, 0, null, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Bitmap backdrop = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.no_image);
                        String FILENAME = "backdrop_" + movie.getId();
                        try{
                            FileOutputStream fos = mContext.openFileOutput(FILENAME, Context.MODE_PRIVATE);
                            backdrop.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.close();
                        } catch (java.io.IOException e) {
                            Log.i(TAG, "onResponse: error: " + e);
                        }
                        mDataResponseInterface.displayMovie(movie);
                        Log.i(TAG, "onErrorResponse: " + error);
                    }
                });
        VolleyRequestQueue.getInstance(mContext).addToRequestQueue(request);
    }

    public void getPoster(String url, final MovieInterface movie) {
        final String BASE_URL = "https://image.tmdb.org/t/p";
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(Constants.POSTER_LARGE)
                .build();

        ImageRequest request = new ImageRequest(builtUri.toString() + url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        String FILENAME = "detail_poster_" + movie.getId();
                        try{
                            FileOutputStream fos = mContext.openFileOutput(FILENAME, Context.MODE_PRIVATE);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.close();
                        } catch (java.io.IOException e) {
                            Log.i(TAG, "onResponse: error: " + e);
                        }
                        mDataResponseInterface.displayMovie(movie);
                    }
                }, 0, 0, null, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Bitmap detail_poster = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.no_image);
                        String FILENAME = "backdrop_" + movie.getId();
                        try{
                            FileOutputStream fos = mContext.openFileOutput(FILENAME, Context.MODE_PRIVATE);
                            detail_poster.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.close();
                        } catch (java.io.IOException e) {
                            Log.i(TAG, "onResponse: error: " + e);
                        }
                        mDataResponseInterface.displayMovie(movie);
                        Log.i(TAG, "onErrorResponse: " + error);
                    }
                });
        VolleyRequestQueue.getInstance(mContext).addToRequestQueue(request);
    }
}
