package com.michaelvescovo.moviehotness.view_movie_details.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.util.BitmapHelper;
import com.michaelvescovo.moviehotness.view_movie_details.entity.MovieInterface;
import com.michaelvescovo.moviehotness.view_movies.Constants;

public class DetailFragment extends Fragment {
    private static final String TAG = "DetailFragment";
    BitmapHelper mBitmapHelper = new BitmapHelper();
//    Fragment mTrailersFragment;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        mTrailersFragment = new TrailersFragment();
//        fragmentTransaction.add(R.id.fragment_container_trailers, mTrailersFragment);
//        fragmentTransaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    public void displayMovie(MovieInterface movie) {
        if (getView() != null) {
            ImageView imageView = (ImageView) getView().findViewById(R.id.fragment_detail_poster);
            TextView textviewReleaseDate = (TextView) getView().findViewById(R.id.fragment_detail_release_date);
            TextView textViewPlot = (TextView) getView().findViewById(R.id.fragment_detail_plot);
            RatingBar ratingBar = (RatingBar) getView().findViewById(R.id.fragment_detail_rating);
            Button button = (Button) getView().findViewById(R.id.more_trailers);

            mBitmapHelper.loadBitmap(imageView, getView().getContext().getFilesDir() + "/" + "detail_poster_" + movie.getId());
            textviewReleaseDate.setText(movie.getReleaseDate());
            textViewPlot.setText(movie.getPlot());
            if (movie.getPlot().length() > Constants.SHORT_PLOT_MAX_CHARS) {
                TextView textViewMore = (TextView) getView().findViewById(R.id.fragment_detail_read_more);
                textViewMore.setVisibility(View.VISIBLE);
            }
            ratingBar.setRating(Float.parseFloat(movie.getVoteAverage()) / 2);
            ratingBar.setVisibility(View.VISIBLE);
            if (movie.getTrailerCount() > 0) {
                button.setVisibility(View.VISIBLE);
            }
            ((DetailActivity)getActivity()).disableProgressBar();
        }
    }
}