package utilities;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.project.android.popularmoviestage2.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

//A class created to send HTTP request and get response in JSON
public final class NetworkUtility {
    //Create a place holder URL for movies retrieval API
    private static final String MOVIES_DATA_URL = "http://api.themoviedb.org/3/movie/";

    //Crate a place holder URL for Movie trailers
    private static final String MOVIES_TRAILERS_REVIEWS_URL = "http://api.themoviedb.org/3/movie/";
    private static final String TRAILERS_KEY = "/videos";

    //Create a place holder for Movie Reviews key
    private static final String REVIEWS_KEY = "/reviews";

    //Set your auth key here in BuildConfig and add your own key in gradle.properties
    private static final String AUTH_KEY = "?api_key=" + BuildConfig.API_KEY;

    //Set Response Format
    private static final String format = "json";
    private final static String FORMAT_PARAM = "mode";

    //Build URL to fetch Movies according to popularity or top rated
    public static URL buildUrl(String input) {
        String basicURLRequest = MOVIES_DATA_URL + input + AUTH_KEY;
        return getUrl(basicURLRequest);
    }

    //Build URL to fetch movie trailers
    public static URL buildURLTrailers(int id){
        String basicURLRequest = MOVIES_TRAILERS_REVIEWS_URL + String.valueOf(id) + TRAILERS_KEY + AUTH_KEY;
        return getUrl(basicURLRequest);
    }

    //Build URL to fetch movie reviews
    public static URL buildURLReviews(int id){
        String basicURLRequest = MOVIES_TRAILERS_REVIEWS_URL + String.valueOf(id) + REVIEWS_KEY + AUTH_KEY;
        return getUrl(basicURLRequest);
    }

    @Nullable
    private static URL getUrl(String basicURLRequest) {
        Uri builtUri = Uri.parse(basicURLRequest).buildUpon()
        .appendQueryParameter(FORMAT_PARAM, format)
        .build();
        URL urlRequest = null;
        try {
            urlRequest = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return urlRequest;
    }

    //Get response and return it as a string
    public static String getResponseFromHttpUrl(URL url) throws IOException {
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
