package com.asmat.rolando.popularmovies.models;

public class Credit {
    private int id;
    private Cast[] cast;
    private Crew[] crew;

    public int getId() {
        return id;
    }

    //region Getters

    public Cast[] getCast() {
        return cast;
    }

    public Crew[] getCrew() {
        return crew;
    }

    public Credit(int id, Cast[] cast, Crew[] crew) {
        this.id = id;
        this.cast = cast;
        this.crew = crew;
    }

    //endregion
}
