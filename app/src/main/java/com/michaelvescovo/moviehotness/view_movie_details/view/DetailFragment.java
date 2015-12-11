package com.michaelvescovo.moviehotness.view_movie_details.view;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.michaelvescovo.moviehotness.R;

public class DetailFragment extends Fragment {

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int position = getActivity().getIntent().getIntExtra("position", -1);
        String title = getActivity().getIntent().getStringExtra("title");
        String plot = getActivity().getIntent().getStringExtra("plot");
        String rating = getActivity().getIntent().getStringExtra("rating");
        String releaseDate = getActivity().getIntent().getStringExtra("release_date");
        Bitmap poster = getActivity().getIntent().getParcelableExtra("poster");

        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.fragment_detail_poster);
        TextView textViewTitle = (TextView) view.findViewById(R.id.fragment_detail_title);
        TextView textViewPlot = (TextView) view.findViewById(R.id.fragment_detail_plot);
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.fragment_detail_rating);
        TextView textviewReleaseDate = (TextView) view.findViewById(R.id.fragment_detail_release_date);

        if (position != -1) {
            imageView.setImageBitmap(poster);
            textViewTitle.setText(title);
            textViewPlot.setText(plot);
            ratingBar.setRating(Float.parseFloat(rating) / 2);
            textviewReleaseDate.setText(releaseDate);
        }

        return view;
    }
}