package com.michaelvescovo.android.moviehotness.view_all_reviews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.michaelvescovo.android.moviehotness.R;
import com.michaelvescovo.android.moviehotness.data.MovieReviewInterface;

import java.util.ArrayList;


public class ViewAllReviewsActivity extends AppCompatActivity implements ViewAllReviewsFragment.ReviewSelectedCallback {

    public static final String REVIEWS = "REVIEWS";
    private ArrayList<MovieReviewInterface> mReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_reviews);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);
        }

        if (getIntent() != null) {
            mReviews = (ArrayList<MovieReviewInterface>)getIntent().getSerializableExtra(REVIEWS);
        }

        initFragment(ViewAllReviewsFragment.newInstance(mReviews));
    }

    private void initFragment(Fragment detailFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_reviews, detailFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Make it so the activity closes instead of going up to it's parent (like the back button)
        finish();
        return true;
    }

    @Override
    public void onFullReviewSelected(View sharedView, String author, String content) {

    }
}
