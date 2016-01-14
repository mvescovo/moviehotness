package com.michaelvescovo.moviehotness.model;

import java.io.Serializable;

/**
 * Created by Michael Vescovo on 12/01/16.
 *
 */
public class MovieReview implements MovieReviewInterface, Serializable {

    private String mId;
    private String mAuthor;
    private String mContent;
    private String mUrl;

    public MovieReview(String id, String author, String content, String url) {
        mId = id;
        mAuthor = author;
        mContent = content;
        mUrl = url;
    }

    @Override
    public String getId() {
        return mId;
    }

    @Override
    public void setId(String id) {
        mId = id;
    }

    @Override
    public String getAuthor() {
        return mAuthor;
    }

    @Override
    public void setAuthor(String author) {
        mAuthor = author;
    }

    @Override
    public String getContent() {
        return mContent;
    }

    @Override
    public void setContent(String content) {
        mContent = content;
    }

    @Override
    public String getUrl() {
        return mUrl;
    }

    @Override
    public void setUrl(String url) {
        mUrl = url;
    }
}
