package com.android.movies.utils;

import java.util.Scanner;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.StrictMode;

import android.net.Uri;

import android.util.Log;

/**
 * Utility class to communicate with REST APIs.
 */
public final class RestUtils {

    private static final String TAG = RestUtils.class.getSimpleName();

    //Put your API_KEY
    public static final String API_KEY = "?api_key=9a50b0845f2b7383172ceea82cddc33c";

    public static final String MOVIE_IMAGES_BASE_URL = "http://image.tmdb.org/t/p/w185";
    public static final String BASE_MOVIE_URL = "http://api.themoviedb.org/3/movie/";
    public static final String POPULAR_MOVIES_URL = BASE_MOVIE_URL + "popular" + API_KEY;
    public static final String TOP_RATED_MOVIES_URL = BASE_MOVIE_URL + "top_rated" + API_KEY;

    public static String getPagedUrl(String url, int pageNumber) {

        if (url.indexOf("&") > 0) {
            url = url.substring(0, url.lastIndexOf("&"));
        }

        return (url + "&page=" + pageNumber);

    }

    public static void setStrictMode() {

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    public static String getJsonPayload(String urlString) throws IOException {

        URL url = null;
        Uri builtUri = Uri.parse(urlString).buildUpon().build();
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built URI " + url);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {

            Scanner scanner = new Scanner(urlConnection.getInputStream());
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