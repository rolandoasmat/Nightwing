package com.asmat.rolando.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.asmat.rolando.popularmovies.utilities.ImageURLUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class Cast implements Parcelable {

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
        this.profilePath = json.getString("profile_path");
    }

    public String getThumbnailURL() {
        return ImageURLUtil.getImageURL342(profilePath);
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

    //region Parcelable
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(castID);
        dest.writeString(character);
        dest.writeString(creditID);
        dest.writeInt(gender);
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(order);
        dest.writeString(profilePath);
    }

    private Cast(Parcel in) {
        castID = in.readInt();
        character = in.readString();
        creditID = in.readString();
        gender = in.readInt();
        id = in.readInt();
        name = in.readString();
        order = in.readInt();
        profilePath = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Cast> CREATOR = new Parcelable.Creator<Cast>() {
        public Cast createFromParcel(Parcel in) {
            return new Cast(in);
        }

        public Cast[] newArray(int size) {
            return new Cast[size];
        }
    };
    //endregion
}
