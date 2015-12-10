package com.michaelvescovo.moviehotness.view_movie_details.data;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.michaelvescovo.moviehotness.BuildConfig;
import com.michaelvescovo.moviehotness.util.VolleyRequestQueue;
import com.michaelvescovo.moviehotness.view_movies.entity.Movie;
import com.michaelvescovo.moviehotness.view_movies.entity.MovieInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Michael on 7/12/15.
 *
 */
public class CloudModel extends DataModel {
    private Context mContext;

    public CloudModel(Context context) {
        mContext = context;
    }

    @Override
    public void getMovie(int movieId) {
        downloadList("http://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + BuildConfig.THE_MOVIE_DB_API_KEY);
    }

    public void downloadList(String url) {
        // Show user something is happening
        // mView.findViewById(R.id.fragment_main_progress).setVisibility(View.VISIBLE);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        final JSONArray results;

                        try {
                            results = response.getJSONArray("results");

                            for (int i = 0; i < results.length(); i++) {
                                String id = results.getJSONObject(i).getString("id");
                                String title = results.getJSONObject(i).getString("original_title");
                                String releaseDate = results.getJSONObject(i).getString("release_date");
                                String poster = results.getJSONObject(i).getString("poster_path");
                                String voteAverage = results.getJSONObject(i).getString("vote_average");
                                String plot = results.getJSONObject(i).getString("overview");
                                String backdrop = results.getJSONObject(i).getString("backdrop_path");
//                                Log.i(TAG, "onResponse: " + backdrop);
//                                movies.add(new Movie(id, title, releaseDate, poster, voteAverage, plot, backdrop));
                                MovieInterface movie = new Movie(id, title, releaseDate, poster, voteAverage, plot, backdrop);
                                mDataResponseInterface.displayMovie(movie);
                            }

//                            mAdapter.notifyDataSetChanged();
//                            mView.findViewById(R.id.fragment_main_progress).setVisibility(View.INVISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();

//                            try {
//                                fragment.getActivity().findViewById(R.id.search_progress).setVisibility(View.INVISIBLE);
//                                ((TextView) fragment.getActivity().findViewById(R.id.volley_error_msg)).setText(fragment.getText(R.string.no_matches));
//                                MovieGridFragment.mImdbIdList.clear();
//                                fragment.getLoaderManager().restartLoader(MovieClubConstants.MOVIE_LOADER_PREVIEW_ID, null, (LoaderManager.LoaderCallbacks) fragment);
//                            } catch (NullPointerException e2) {
//                                // The view doesn't exist.
//                            }
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
        // Access the RequestQueue through your singleton class.
        VolleyRequestQueue.getInstance(mContext).addToRequestQueue(jsObjRequest);
    }
}
