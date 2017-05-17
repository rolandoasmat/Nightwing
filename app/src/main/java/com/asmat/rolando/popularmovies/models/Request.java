package com.asmat.rolando.popularmovies.models;

/**
 * Created by rolandoasmat on 2/11/17.
 */

public class Request {
    int requestType;
    int page;

    public Request(int requestType, int page) {
        this.requestType = requestType;
        this.page = page;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void nextPage(){
        this.page++;
    }

    public void resetPage(){
        this.page = 1;
    }
}