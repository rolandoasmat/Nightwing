package com.asmat.rolando.popularmovies.models;

/**
 * Created by rolandoasmat on 5/18/17.
 */

public class Video {

    private static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    private static final String YOUTUBE_THUMBNAIL_PRE = "http://img.youtube.com/vi/";
    private static final String YOUTUBE_THUMBNAIL_POST = "/0.jpg";

    private String name;
    private String site; // YouTube
    private String key;
    private String type; // Trailer, Teaser

    public Video(String name, String site, String key, String type) {
        this.name = name;
        this.site = site;
        this.key = key;
        this.type = type;
    }

    public String getYouTubeURL() {
        return YOUTUBE_URL + key;
    }

    public String getYouTubeThumbnailURL() {
        return YOUTUBE_THUMBNAIL_PRE + key + YOUTUBE_THUMBNAIL_POST;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
