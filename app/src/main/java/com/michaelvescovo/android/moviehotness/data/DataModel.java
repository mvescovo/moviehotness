package com.michaelvescovo.android.moviehotness.data;

/**
 * Created by Michael on 4/12/15.
 *
 * Use chain of responsibility pattern to get data.
 *
 */
public abstract class DataModel implements MovieRepository {
    protected DataModel successor;

    public void setSuccessor(DataModel successor) {
        this.successor = successor;
    }
}