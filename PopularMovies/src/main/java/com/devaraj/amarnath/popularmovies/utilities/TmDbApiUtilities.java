package com.devaraj.amarnath.popularmovies.utilities;

import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

// Utility class to access the The Movie Database
public final class TmDbApiUtilities {
    private static final String TMDB_API_BASE_URL  = "https://api.themoviedb.org";
    private static final String TMDB_API_VERSION   = "3";
    //private static final String TMDB_CONFIGURATION_PATH = "configuration";
    private static final String TMDB_MOVIE_PATH    = "movie";
    private static final String TMDB_API_KEY_PARAM = "api_key";
    // TODO : Place your API key here
    private static final String TMDB_API_KEY_VALUE = "<my_api_key_string>";

    public static final String TMDB_MOVIE_TYPE_POPULAR   = "popular";
    public static final String TMDB_MOVIE_TYPE_TOP_RATED = "top_rated";
    public static final String TMDB_MOVIE_TYPE_UPCOMING  = "upcoming";

    private static final String TMDB_MOVIE_IMAGE_URL_PREFIX = "http://image.tmdb.org/t/p";
    public static final String TMDB_MOVIE_IMAGE_SIZE_780    = "/w780";
    public static final String TMDB_MOVIE_IMAGE_SIZE_500    = "/w500";

    public static String getImageUrl(String imageSize, String path) {
        return (TMDB_MOVIE_IMAGE_URL_PREFIX + imageSize + path);
    }

// --Commented out by Inspection START (5/24/2018 12:16 AM):
//    public static URL getConfigUrl() throws IllegalStateException {
//        Uri tmdbUri = Uri.parse(TMDB_API_BASE_URL).buildUpon()
//                .appendPath(TMDB_API_VERSION)
//                .appendPath(TMDB_CONFIGURATION_PATH)
//                .appendQueryParameter(TMDB_API_KEY_PARAM, TMDB_API_KEY_VALUE)
//                .build();
//        URL url = null;
//        try {
//            url = new URL(tmdbUri.toString());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        return url;
//    }
// --Commented out by Inspection STOP (5/24/2018 12:16 AM)

    public static URL getMovieListUrl(String type) throws IllegalStateException {
        Uri tmdbUri = Uri.parse(TMDB_API_BASE_URL).buildUpon()
                .appendPath(TMDB_API_VERSION)
                .appendPath(TMDB_MOVIE_PATH)
                .appendPath(type)
                .appendQueryParameter(TMDB_API_KEY_PARAM, TMDB_API_KEY_VALUE)
                .build();
        URL url = null;
        try {
            url = new URL(tmdbUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

// --Commented out by Inspection START (5/24/2018 12:16 AM):
//    public static URL getMovieDetailUrl(int movieId) throws IllegalStateException {
//        Uri tmdbUri = Uri.parse(TMDB_API_BASE_URL).buildUpon()
//                .appendPath(TMDB_API_VERSION)
//                .appendPath(TMDB_MOVIE_PATH)
//                .appendPath(Integer.toString(movieId))
//                .appendQueryParameter(TMDB_API_KEY_PARAM, TMDB_API_KEY_VALUE)
//                .build();
//        URL url = null;
//        try {
//            url = new URL(tmdbUri.toString());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        return url;
//    }
// --Commented out by Inspection STOP (5/24/2018 12:16 AM)

    public static String getHttpResponse(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = connection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            connection.disconnect();
        }
    }

}
