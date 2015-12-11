package com.michaelvescovo.moviehotness.view_movies.data;

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
import com.michaelvescovo.moviehotness.view_movies.Constants;
import com.michaelvescovo.moviehotness.view_movies.entity.Movie;
import com.michaelvescovo.moviehotness.view_movies.entity.MovieInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Michael on 4/12/15.
 *
 */
public class CloudModel extends DataModel {
    private static final String TAG = "CloudModel";
    private Context mContext;

    public CloudModel(Context context) {
        mContext = context;
    }

    @Override
    public void getMovies(int sortBy) {
        switch (sortBy) {
            case Constants.POPULAR:
                downloadList("https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=" + BuildConfig.THE_MOVIE_DB_API_KEY, sortBy);
                break;
            case Constants.HIGHEST_RATED:
                downloadList("https://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=" + BuildConfig.THE_MOVIE_DB_API_KEY, sortBy);
                break;
        }
    }

    public void downloadList(String url, final int sortBy) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        final JSONArray results;
                        int resultsSize;

                        try {
                            results = response.getJSONArray("results");
                            resultsSize = results.length();

                            for (int i = 0; i < results.length(); i++) {
                                String id = results.getJSONObject(i).getString("id");
                                String title = results.getJSONObject(i).getString("original_title");
                                String releaseDate = results.getJSONObject(i).getString("release_date");
                                String posterUrl = results.getJSONObject(i).getString("poster_path");
                                String voteAverage = results.getJSONObject(i).getString("vote_average");
                                String plot = results.getJSONObject(i).getString("overview");
                                String backdrop = results.getJSONObject(i).getString("backdrop_path");
                                MovieInterface movie = new Movie(id, title, releaseDate, posterUrl, voteAverage, plot, backdrop);
                                getPoster(posterUrl, sortBy, resultsSize, movie);
                            }
                        } catch (JSONException e) {
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

    public void getPoster(String url, final int sortBy, final int resultsSize, final MovieInterface movie) {
        final String BASE_URL = "https://image.tmdb.org/t/p";
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(Constants.POSTER_MEDIUM)
                .build();

        ImageRequest request = new ImageRequest(builtUri.toString() + url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        movie.setPoster(bitmap);
                        mDataResponseInterface.displayMovies(movie, sortBy, resultsSize);
                    }
                }, 0, 0, null, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Bitmap poster = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.no_image);
                        movie.setPoster(poster);
                        mDataResponseInterface.displayMovies(movie, sortBy, resultsSize);
                        Log.i(TAG, "onErrorResponse: " + error);
                    }
                });
        VolleyRequestQueue.getInstance(mContext).addToRequestQueue(request);
    }
}