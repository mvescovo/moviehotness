package com.michaelvescovo.moviehotness.view_movie_details.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Michael on 17/12/15.
 *
 */
public class TrailerViewHolder extends RecyclerView.ViewHolder {
    private View mView;

    public TrailerViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public View getView() {
        return mView;
    }
}
