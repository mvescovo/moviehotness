package com.michaelvescovo.android.moviehotness.data;

import java.io.Serializable;

/**
 * Created by Michael Vescovo on 12/01/16.
 *
 */
public class MovieReview implements MovieReviewInterface, Serializable {

    private String mAuthor;
    private String mContent;

    public MovieReview(String author, String content) {
        mAuthor = author;
        mContent = content;
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
}
