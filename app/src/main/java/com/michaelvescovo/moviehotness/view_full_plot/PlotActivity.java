package com.michaelvescovo.moviehotness.view_full_plot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.michaelvescovo.moviehotness.R;

public class PlotActivity extends AppCompatActivity {
    private static final String TAG = "PlotActivity";
    private static final String PLOT = "plot";
    private static final String TITLE = "title";

    private String mPlot;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null) {
            Log.i(TAG, "onCreate: plot: " + getIntent().getStringExtra(PLOT));
            mPlot = getIntent().getStringExtra(PLOT);
            mTitle = getIntent().getStringExtra(TITLE);
        }

        setContentView(R.layout.activity_plot);

        ((TextView) findViewById(R.id.plot_title)).setText(mTitle);
        ((TextView) findViewById(R.id.plot_text)).setText(mPlot);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);
            getSupportActionBar().setTitle("Plot");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Make it so the activity closes instead of going up to it's parent (like the back button)
        finish();
        return true;
    }
}
