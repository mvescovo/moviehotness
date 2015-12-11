package com.michaelvescovo.moviehotness.view_movies.entity;

import android.graphics.Bitmap;

/**
 * Created by Michael on 29/11/15.
 *
 */
public class MoviePreview implements MoviePreviewInterface {
    private String mId;
    private String mPosterUrl;
    private Bitmap mPoster;

    public MoviePreview(String id, String posterUrl) {
        mId = id;
        mPosterUrl = posterUrl;
    }

    @Override
    public void setId(String id) {
        mId = id;
    }

    @Override
    public String getId() {
        return mId;
    }

    @Override
    public void setPosterUrl(String posterUrl) {
        mPosterUrl = posterUrl;
    }

    @Override
    public String getPosterUrl() {
        return mPosterUrl;
    }

    @Override
    public void setPoster(Bitmap poster) {
        mPoster = poster;
    }

    @Override
    public Bitmap getPoster() {
        return mPoster;
    }
}
