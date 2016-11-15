package com.michaelvescovo.android.moviehotness.view_full_plot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.michaelvescovo.android.moviehotness.R;
import com.michaelvescovo.android.moviehotness.view_movies.ViewMoviesActivity;


/**
 * Created by Michael Vescovo on 22/02/16.
 *
 */
public class ViewFullPlotFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_view_full_plot, container, false);
        TextView title = (TextView) root.findViewById(R.id.plot_title);
        title.setText(getArguments().getString(ViewFullPlotActivity.TITLE));
        TextView plot = (TextView) root.findViewById(R.id.plot_text);
        plot.setText(getArguments().getString(ViewFullPlotActivity.PLOT));

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.full_plot);

        if (!ViewMoviesActivity.mTwoPane) {
            ((ViewFullPlotActivity)getActivity()).setSupportActionBar(toolbar);
            if (((ViewFullPlotActivity)getActivity()).getSupportActionBar() != null) {
                ((ViewFullPlotActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ((ViewFullPlotActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);
            }
        }
        return root;
    }

    public static ViewFullPlotFragment newInstance(String title,String plot) {
        Bundle arguments = new Bundle();
        arguments.putString(ViewFullPlotActivity.TITLE, title);
        arguments.putString(ViewFullPlotActivity.PLOT, plot);
        ViewFullPlotFragment fragment = new ViewFullPlotFragment();
        fragment.setArguments(arguments);
        return fragment;
    }
}
