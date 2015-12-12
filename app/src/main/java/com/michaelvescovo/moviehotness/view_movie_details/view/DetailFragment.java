package com.michaelvescovo.moviehotness.view_movie_details.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.view_movie_details.entity.MovieInterface;

public class DetailFragment extends Fragment {

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    public void displayMovie(MovieInterface movie) {
        if (getActivity() != null) {
            ImageView imageView = (ImageView) getActivity().findViewById(R.id.fragment_detail_poster);
            TextView textViewTitle = (TextView) getActivity().findViewById(R.id.fragment_detail_title);
            TextView textViewPlot = (TextView) getActivity().findViewById(R.id.fragment_detail_plot);
            RatingBar ratingBar = (RatingBar) getActivity().findViewById(R.id.fragment_detail_rating);
            TextView textviewReleaseDate = (TextView) getActivity().findViewById(R.id.fragment_detail_release_date);

            imageView.setImageBitmap(movie.getPoster());
            textViewTitle.setText(movie.getTitle());
            textViewPlot.setText(movie.getPlot());
            ratingBar.setRating(Float.parseFloat(movie.getVoteAverage()) / 2);
            textviewReleaseDate.setText(movie.getReleaseDate());
            ((DetailActivity)getActivity()).disableProgressBar();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((DetailActivity)getActivity()).getMovie();
    }
}