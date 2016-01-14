package com.michaelvescovo.moviehotness.view_all_reviews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.model.MovieReviewInterface;

import java.util.ArrayList;

public class ViewAllReviewsActivity extends AppCompatActivity {

    public static final String EXTRA_REVIEWS = "REVIEWS";
    private ArrayList<MovieReviewInterface> mReviews;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_reviews);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);
            getSupportActionBar().setTitle("Reviews");
        }

        mReviews = (ArrayList<MovieReviewInterface>)getIntent().getSerializableExtra(EXTRA_REVIEWS);
        mAdapter = new ReviewAdapter(mReviews);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Make it so the activity closes instead of going up to it's parent (like the back button)
        finish();
        return true;
    }
}
