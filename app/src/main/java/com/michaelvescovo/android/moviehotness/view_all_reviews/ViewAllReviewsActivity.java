package com.michaelvescovo.android.moviehotness.view_all_reviews;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.michaelvescovo.android.moviehotness.R;
import com.michaelvescovo.android.moviehotness.data.MovieReviewInterface;
import com.michaelvescovo.android.moviehotness.view_full_review.ViewFullReviewActivity;

import java.util.ArrayList;


public class ViewAllReviewsActivity extends AppCompatActivity
        implements ViewAllReviewsFragment.Callback {

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

    @Override
    public void onFullReviewSelected(View sharedView, String author, String content) {
        Intent intent = new Intent(this, ViewFullReviewActivity.class);
        intent.putExtra(ViewFullReviewActivity.AUTHOR, author);
        intent.putExtra(ViewFullReviewActivity.CONTENT, content);
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                sharedView,
                ViewCompat.getTransitionName(sharedView)
        ).toBundle();
        startActivity(intent, bundle);
    }
}
