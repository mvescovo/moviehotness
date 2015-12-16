package com.michaelvescovo.moviehotness.view_movie_details.entity;

/**
 * Created by Michael on 16/12/15.
 *
 */
public class MovieTrailer implements MovieTrailerInterface {
    private String mYouTubeId;
    private String mName;

    public MovieTrailer(String youTubeId, String name) {
        setYouTubeId(youTubeId);
        setName(name);
    }

    @Override
    public void setYouTubeId(String youTubeId) {
        mYouTubeId = youTubeId;
    }

    @Override
    public String getYouTubeId() {
        return mYouTubeId;
    }

    @Override
    public void setName(String name) {
        mName = name;
    }

    @Override
    public String getName() {
        return mName;
    }
}
