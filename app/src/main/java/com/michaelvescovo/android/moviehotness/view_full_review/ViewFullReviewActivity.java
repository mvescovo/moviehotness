package com.michaelvescovo.android.moviehotness.view_full_review;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.michaelvescovo.android.moviehotness.R;


public class ViewFullReviewActivity extends AppCompatActivity {

    public static final String AUTHOR = "AUTHOR";
    public static final String CONTENT = "CONTENT";
    private String mAuthor;
    private String mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_full_review);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
           getSupportActionBar().setDisplayHomeAsUpEnabled(true);
           getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);
        }

        if (getIntent() != null) {
            mAuthor = getIntent().getStringExtra(AUTHOR);
            mContent = getIntent().getStringExtra(CONTENT);
        }

        initFragment(ViewFullReviewFragment.newInstance(mAuthor, mContent));
    }

    private void initFragment(Fragment detailFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_full_review, detailFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Make it so the activity closes instead of going up to it's parent (like the back button)
        finish();
        return true;
    }
}
