package com.asmat.rolando.popularmovies.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Crew {

    private String creditID;
    private String department;
    private int gender;
    private int id;
    private String job;
    private String name;
    private String profilePath;

    public Crew(JSONObject json) throws JSONException {
        this.creditID = json.getString("credit_id");
        this.department = json.getString("department");
        this.gender = json.getInt("gender");
        this.id = json.getInt("id");
        this.job = json.getString("job");
        this.name = json.getString("name");
        this.profilePath = json.getString("profilePath");
    }

    //region Getters

    public String getCreditID() {
        return creditID;
    }

    public String getDepartment() {
        return department;
    }

    public int getGender() {
        return gender;
    }

    public int getId() {
        return id;
    }

    public String getJob() {
        return job;
    }

    public String getName() {
        return name;
    }

    public String getProfilePath() {
        return profilePath;
    }

    //endregion
}
