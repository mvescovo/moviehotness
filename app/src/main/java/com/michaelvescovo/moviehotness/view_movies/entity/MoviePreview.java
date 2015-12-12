package com.michaelvescovo.moviehotness.view_movies.entity;

import java.io.Serializable;

/**
 * Created by Michael on 29/11/15.
 *
 */
public class MoviePreview implements MoviePreviewInterface, Serializable {
    private String mId;
    private String mPosterUrl;

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
}
