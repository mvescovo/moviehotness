package com.michaelvescovo.android.moviehotness.view_full_plot;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Michael Vescovo on 17/12/16.
 *
 */

class ViewFullPlotPresenter implements ViewFullPlotContract.UserActionsListener {

    private ViewFullPlotContract.View mView;

    ViewFullPlotPresenter(@NonNull ViewFullPlotContract.View view) {
        mView = checkNotNull(view, "viewFullPlotView cannot be null");
    }

    @Override
    public void openAttribution() {
        mView.showAttributionUi();
    }
}
