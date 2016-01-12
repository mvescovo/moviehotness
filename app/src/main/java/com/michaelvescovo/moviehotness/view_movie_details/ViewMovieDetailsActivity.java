package com.michaelvescovo.moviehotness.view_movie_details;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.util.EspressoIdlingResource;

public class ViewMovieDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE_ID = "MOVIE_ID";
    public static final String EXTRA_SORT_BY = "SORT_BY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_movie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String movieId = getIntent().getStringExtra(EXTRA_MOVIE_ID);
        int sortBy = getIntent().getIntExtra(EXTRA_SORT_BY, -1);
        initFragment(ViewMovieDetailsFragment.newInstance(sortBy, movieId));
    }

    private void initFragment(Fragment detailFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_scroll_view, detailFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}