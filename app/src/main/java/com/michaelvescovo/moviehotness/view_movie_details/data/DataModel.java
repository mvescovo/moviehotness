package com.michaelvescovo.moviehotness.view_movie_details.data;

/**
 * Created by Michael on 9/12/15.
 *
 */
public abstract class DataModel implements DataRequestInterface {
    protected DataModel successor;
    protected DataResponseInterface mDataResponseInterface;

    public void setSuccessor(DataModel successor) {
        this.successor = successor;
    }

    public void setDataResponseInterface(DataResponseInterface dataResponseInterface) {
        this.mDataResponseInterface = dataResponseInterface;
    }
}
