package com.michaelvescovo.android.moviehotness.view_full_plot;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.michaelvescovo.android.moviehotness.R;


/**
 * Created by Michael Vescovo on 22/02/16.
 *
 */
public class ViewFullPlotFragment extends Fragment implements ViewFullPlotContract.View {

    private ViewFullPlotContract.UserActionsListener mActionsListener;
    private ViewFullPlotFragment.Callback mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (ViewFullPlotFragment.Callback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement Callback");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActionsListener = new ViewFullPlotPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View root = inflater.inflate(R.layout.fragment_view_full_plot, container, false);

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.full_plot);

        if (!getResources().getBoolean(R.bool.two_pane)) {
            if (getActivity().getClass().isInstance(AppCompatActivity.class)) {
                ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
                ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    actionBar.setHomeAsUpIndicator(R.drawable.ic_close_24dp);
                }
            }
        }

        NestedScrollView nestedScrollView = (NestedScrollView) root.findViewById(R.id.full_plot_shared_view);
        ViewCompat.setTransitionName(nestedScrollView, getString(R.string.transition_plot));
        TextView title = (TextView) root.findViewById(R.id.plot_title);
        title.setText(getArguments().getString(ViewFullPlotActivity.TITLE));
        TextView plot = (TextView) root.findViewById(R.id.plot_text);
        plot.setText(getArguments().getString(ViewFullPlotActivity.PLOT));

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            if (!getResources().getBoolean(R.bool.two_pane)) {
                mActionsListener.openAttribution();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showAttributionUi() {
        mCallback.onAboutSelected();
    }

    public interface Callback {
        void onAboutSelected();
    }
}
