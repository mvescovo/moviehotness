package com.michaelvescovo.moviehotness.view_all_reviews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.data.MovieReviewInterface;

import java.util.ArrayList;

public class ViewAllReviewsActivity extends AppCompatActivity implements ViewAllReviewsFragment.ReviewSelectedCallback {

    public static final String REVIEWS = "REVIEWS";
    private ArrayList<MovieReviewInterface> mReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_reviews);

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
    public void onFullReviewSelected(String author, String content) {

    }
}
