package com.michaelvescovo.moviehotness.view_full_review;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.michaelvescovo.moviehotness.R;

public class ViewFullReviewActivity extends AppCompatActivity {

    public static final String EXTRA_AUTHOR = "AUTHOR";
    public static final String EXTRA_CONTENT = "CONTENT";

    private String mAuthor;
    private String mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_full_review);

        if (getIntent() != null) {
            mAuthor = getIntent().getStringExtra(EXTRA_AUTHOR);
            mContent = getIntent().getStringExtra(EXTRA_CONTENT);
        }

        ((TextView) findViewById(R.id.full_review_author)).setText(mAuthor);
        ((TextView) findViewById(R.id.full_review_content)).setText(mContent);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);
            getSupportActionBar().setTitle("Review");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Make it so the activity closes instead of going up to it's parent (like the back button)
        finish();
        return true;
    }
}
