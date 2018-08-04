package com.asmat.rolando.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;

public final class NetworkUtils {

    private static String TAG = NetworkUtils.class.getSimpleName();


    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Create a URL object given a string endpoint and a map of parameters.
     *
     * @param endpoint Endpoint to transform.
     * @param queryParameters Key and value pairs of query parameters.
     *
     * @return URL representation of endpoint + parameters.
     */
    public static URL buildUrl(String endpoint, Hashtable<String, String> queryParameters) throws IOException {
        // Build Uri
        Uri.Builder builder = Uri.parse(endpoint).buildUpon();
        for(Enumeration<String> keys = queryParameters.keys(); keys.hasMoreElements();) {
            String key = keys.nextElement();
            String value = queryParameters.get(key);
            builder.appendQueryParameter(key, value);
        }
        Uri uri = builder.build();
        // Create URL
        URL url = new URL(uri.toString());
        Log.v(TAG, "Built URL: " + url);
        return url;
    }

    /**
     * GET request the provided URL.
     *
     * @param url URL to query.
     *
     * @return String json response.
     */
    public static String httpRequest(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                String response = scanner.next();
                return response;
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
