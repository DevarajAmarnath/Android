package com.devaraj.amarnath.popularmovies.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class TmDbJsonUtilities {
    public static MovieInfo[] getMovieIdsFromJson(String queryResultJson) throws JSONException{
        JSONObject movie_result_pages = new JSONObject(queryResultJson);
        JSONArray movie_result_array = movie_result_pages.getJSONArray("results");

        MovieInfo[] movieInfo = new MovieInfo[movie_result_array.length()];
        for (int index = 0; index < movie_result_array.length(); index++) {
            JSONObject movie_info = movie_result_array.getJSONObject(index);
            movieInfo[index] = new MovieInfo(movie_info);
        }
        return movieInfo;
    }
}
