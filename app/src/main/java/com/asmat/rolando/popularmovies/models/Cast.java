package com.asmat.rolando.popularmovies.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Cast {
    private int castID;
    private String character;
    private String creditID;
    private int gender;
    private int id;
    private String name;
    private int order;
    private String profilePath;

    public Cast(JSONObject json) throws JSONException {
        this.castID = json.getInt("cast_id");
        this.character = json.getString("character");
        this.creditID = json.getString("credit_id");
        this.gender = json.getInt("gender");
        this.id = json.getInt("id");
        this.name = json.getString("name");
        this.order = json.getInt("order");
        this.profilePath = json.getString("profilePath");
    }

    //region Getters

    public int getCastID() {
        return castID;
    }

    public String getCharacter() {
        return character;
    }

    public String getCreditID() {
        return creditID;
    }

    public int getGender() {
        return gender;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }

    public String getProfilePath() {
        return profilePath;
    }

    //endregion
}
