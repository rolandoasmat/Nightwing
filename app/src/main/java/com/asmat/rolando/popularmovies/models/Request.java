package com.asmat.rolando.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rolandoasmat on 2/11/17.
 */

public class Request implements Parcelable {
    private int requestType;
    private int page;
    private int popularMoviesPage;
    private int topRatedMoviesPage;

    public static final String PARCEL_KEY = "request";

    public Request(int requestType, int page, int popularMoviesPage, int topRatedMoviesPage) {
        this.requestType = requestType;
        this.page = page;
        this.popularMoviesPage = popularMoviesPage;
        this.topRatedMoviesPage = topRatedMoviesPage;
    }

    public void setPopularMoviesPage(int popularMoviesPage) {
        this.popularMoviesPage = popularMoviesPage;
    }

    public void nextPopularMoviesPage() {
        popularMoviesPage++;
    }

    public void setTopRatedMoviesPage(int topRatedMoviesPage) {
        this.topRatedMoviesPage = topRatedMoviesPage;
    }

    public void nextTopRatedMoviesPage() {
        this.topRatedMoviesPage++;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public int getPage() {
        switch (requestType) {
            case RequestType.POPULAR:
                return popularMoviesPage + 1;
            case RequestType.TOP_RATED:
                return topRatedMoviesPage + 1;
            default:
                return 1;
        }
    }

    public void nextPage(){
        this.page++;
    }

    // PARCELABLE

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(requestType);
        dest.writeInt(page);
        dest.writeInt(popularMoviesPage);
        dest.writeInt(topRatedMoviesPage);
    }

    protected Request(Parcel in) {
        requestType = in.readInt();
        page = in.readInt();
        popularMoviesPage = in.readInt();
        topRatedMoviesPage = in.readInt();
    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };
}