package com.asmat.rolando.popularmovies.utilities;

public class ImageURLUtil {

    /**
     * Sample widths "w92", "w154", "w185", "w342", "w500", "w780"
     * i.e. http://image.tmdb.org/t/p/w342
     */
    private static final String BASE_URL = "http://image.tmdb.org/t/p/";

    private static final String w92 = "w92";
    private static final String w154 = "w154";
    private static final String w185 = "w185";
    private static final String w342 = "w342";
    private static final String w500 = "w500";
    private static final String w780 = "w780";

    public static String getImageURL342(String relativeURL) {
        return builder()
                .append(w342)
                .append(relativeURL)
                .toString();
    }

    public static String getImageURL780(String relativeURL) {
        return builder()
                .append(w780)
                .append(relativeURL)
                .toString();
    }

    private static StringBuilder builder() {
        return new StringBuilder(BASE_URL);
    }
}
