package com.michaelvescovo.android.moviehotness.view_all_trailers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.michaelvescovo.android.moviehotness.R;
import com.michaelvescovo.android.moviehotness.data.MovieTrailerInterface;

import java.util.ArrayList;

public class ViewAllTrailersActivity extends AppCompatActivity implements ViewAllTrailersFragment.Callback {

    public static final String TRAILERS = "trailers";
    ArrayList<MovieTrailerInterface> mTrailers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_trailers);

        if (getIntent() != null) {
            mTrailers = (ArrayList<MovieTrailerInterface>) getIntent().getSerializableExtra(TRAILERS);
        }

        initFragment(ViewAllTrailersFragment.newInstance(mTrailers));
    }

    private void initFragment(Fragment detailFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_trailers, detailFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Make it so the activity closes instead of going up to it's parent (like the back button)
        finish();
        return true;
    }

    @Override
    public void onSetSupportActionbar(@NonNull Toolbar toolbar, @NonNull Boolean upEnabled, @Nullable Integer homeAsUpIndicator) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(upEnabled);
            if (homeAsUpIndicator != null) {
                actionBar.setHomeAsUpIndicator(homeAsUpIndicator);
            }
        }
    }

    @Override
    public void onAboutSelected() {

    }
}
