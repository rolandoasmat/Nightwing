package com.asmat.rolando.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;


/**
 * Created by rolandoasmat on 2/10/17.
 */

public final class NetworkUtils {

    private static String TAG = NetworkUtils.class.getSimpleName();

    public static URL buildUrl(String endpoint, Hashtable<String, String> queryParameters) {
        // Build Uri
        Uri.Builder builder = Uri.parse(endpoint).buildUpon();
        for(Enumeration<String> keys = queryParameters.keys(); keys.hasMoreElements();) {
            String key = keys.nextElement();
            String value = (String) queryParameters.get(key);
            builder.appendQueryParameter(key, value);
        }
        Uri uri = builder.build();
        // Create URL
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URL: " + url);

        return url;
    }

    public static String httpRequest(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }


    }

}
