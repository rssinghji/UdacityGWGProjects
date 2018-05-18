package utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

//A class created to send HTTP request and get response in JSON
public final class NetworkUtils {
    //Create a place holder URL for movies retrieval API
    private static final String MOVIES_DATA_URL = "http://api.themoviedb.org/3/movie/";

    //Set your auth key here in place of 0's
    private static final String AUTH_KEY = "?api_key=00000";

    //Set Response Format
    private static final String format = "json";
    private final static String FORMAT_PARAM = "mode";

    //Build URL to fetch Movies according to popularity or top rated
    public static URL buildUrl(String input) {
        String basicURLRequest = MOVIES_DATA_URL + input + AUTH_KEY;
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
