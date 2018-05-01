package com.android.movies.utils;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.movies.model.Movie;

public class JsonUtils {

    public static Movie getMovieInstance(JSONObject movieJsonObject) throws JSONException {

        Movie movie = new Movie(movieJsonObject.getInt("id"), movieJsonObject.getString("original_title"), movieJsonObject.getString("poster_path"),
                                movieJsonObject.getString("overview"), movieJsonObject.getDouble("vote_average"),
                                movieJsonObject.getString("release_date"));
        //System.out.println(movie);
        return movie;

    }

    public static Movie parseMovieJson(String json) throws JSONException {

        Movie movie = null;
        if (json != null && !(json = json.trim()).isEmpty()) {
            movie = getMovieInstance(new JSONObject(json));
        }
        return movie;

    }

}