package com.michaelvescovo.moviehotness.view_movie_details.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.util.BitmapHelper;
import com.michaelvescovo.moviehotness.view_movie_details.entity.MovieInterface;

public class DetailFragment extends Fragment {
    private static final String TAG = "DetailFragment";
    BitmapHelper mBitmapHelper = new BitmapHelper();
    Fragment mTrailersFragment;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mTrailersFragment = new TrailersFragment();
        fragmentTransaction.add(R.id.fragment_container_trailers, mTrailersFragment);
        fragmentTransaction.commit();

        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    public void displayMovie(MovieInterface movie) {
        if (getView() != null) {
            ImageView imageView = (ImageView) getView().findViewById(R.id.fragment_detail_poster);
            TextView textViewTitle = (TextView) getView().findViewById(R.id.fragment_detail_title);
            TextView textViewPlot = (TextView) getView().findViewById(R.id.fragment_detail_plot);
            TextView textViewMore = (TextView) getView().findViewById(R.id.fragment_detail_read_more);
            textViewMore.setVisibility(View.VISIBLE);
            RatingBar ratingBar = (RatingBar) getView().findViewById(R.id.fragment_detail_rating);
            ratingBar.setVisibility(View.VISIBLE);
            TextView textviewReleaseDate = (TextView) getView().findViewById(R.id.fragment_detail_release_date);
            textviewReleaseDate.setVisibility(View.VISIBLE);
            Log.i(TAG, "displayMovie: path: " + getView().getContext().getFilesDir() + "/" + "detail_poster_" + movie.getId());
            mBitmapHelper.loadBitmap(imageView, getView().getContext().getFilesDir() + "/" + "detail_poster_" + movie.getId());
            textViewTitle.setText(movie.getTitle());
            textViewPlot.setText(movie.getPlot());
            ratingBar.setRating(Float.parseFloat(movie.getVoteAverage()) / 2);
            textviewReleaseDate.setText(movie.getReleaseDate());
            ((TrailersFragment)mTrailersFragment).displayMovie(movie);
            ((DetailActivity)getActivity()).disableProgressBar();
        }
    }
}