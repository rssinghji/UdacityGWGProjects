package utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import model.Movie;
import model.MovieReview;
import model.MovieTrailer;

//JSON Utility class to help in parsing details
public final class JsonUtility{

    //Create a place holder URL for a movie image
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";

    //Set a size for the image which could be altered in future if needed
    private static final String BASE_IMAGE_SIZE = "w185/";

    //Create a place holder for the trailer path
    private static final String TRAILER_URL = "https://www.youtube.com/watch?v=";

    //Get all the results of the movies ny parsing JSON response form website
    public static String[] getMoviesResults(String httpResponse)  throws JSONException
    {
        JSONObject root = new JSONObject(httpResponse);
        JSONArray resultsArray = root.getJSONArray("results");
        String[] resultStrings = new String[resultsArray.length()];
        for(int index = 0; index < resultsArray.length(); index++)
        {
            resultStrings[index] = resultsArray.get(index).toString();
        }
        return resultStrings;
    }

    //Get the image path from a movie result sent in JSON
    public static String getImagePath(String resultJson) throws JSONException
    {
        JSONObject result = new JSONObject(resultJson);
        String imagePath = result.getString("poster_path");
        return imagePath;
    }

    //Extract Trailers information from JSON
    public static MovieTrailer[] getParsedTrailers(String trailersJSON) throws JSONException {
        JSONObject trailersData = new JSONObject(trailersJSON);
        JSONArray results = trailersData.getJSONArray("results");
        MovieTrailer[] trailers = new MovieTrailer[results.length()];
        for(int index = 0; index < results.length(); index++)
        {
            JSONObject trailer = results.getJSONObject(index);
            String id = trailer.getString("id");
            String name = trailer.getString("name");
            String key = trailer.getString("key");
            String site = trailer.getString("site");
            site = TRAILER_URL + key;
            String type = trailer.getString("type");
            String size = trailer.getString("size");
            trailers[index] = new MovieTrailer(key, name, site, size, type, id);
        }
        return  trailers;
    }

    //Extract Reviews information from JSON
    public static MovieReview[] getParsedReviews(String reviewsJSON) throws JSONException {
        JSONObject reviewsData = new JSONObject(reviewsJSON);
        JSONArray results = reviewsData.getJSONArray("results");
        MovieReview[] reviews = new MovieReview[results.length()];
        for(int index = 0; index < results.length(); index++)
        {
            JSONObject review = results.getJSONObject(index);
            String id = review.getString("id");
            String author = review.getString("author");
            String content = review.getString("content");
            reviews[index] = new MovieReview(id, author, content);
        }
        return  reviews;
    }

    //Extract all the valuable information for a movie
    private static Movie getParsedMovieDetails(String movieJson) throws JSONException{
        JSONObject movie = new JSONObject(movieJson);
        String originalTitle = movie.getString("original_title");
        String plotSynopsis = movie.getString("overview");
        String userRating = movie.getString("vote_average");
        userRating += "/10";
        String releaseDate = movie.getString("release_date");
        int movieId = movie.getInt("id");

        //Setting a default length for now as no API used to get length() as per UX mock up
        String movieLength = "120 mins";

        String imagePath = movie.getString("poster_path");
        String imageUrl = BASE_IMAGE_URL + BASE_IMAGE_SIZE + imagePath;

        return new Movie(originalTitle, imageUrl, plotSynopsis, userRating, releaseDate,
        movieLength, movieId/*, trailers*/);
    }

    //Extract all valuable info for a movie and put it into a Movie Array
    public static Movie[] getParsedMovieDetailsInMovieArray(String httpResponse) throws JSONException
    {
        JSONObject root = new JSONObject(httpResponse);
        JSONArray resultsArray = root.getJSONArray("results");
        Movie[] movieArray = new Movie[resultsArray.length()];
        for(int index = 0; index < resultsArray.length(); index++)
        {
            movieArray[index] = getParsedMovieDetails(resultsArray.get(index).toString());
        }
        return movieArray;
    }
}
