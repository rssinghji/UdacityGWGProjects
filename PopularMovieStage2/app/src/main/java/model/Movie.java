package model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

//Class created to use as model / data container to extract valuable information from API.
public class Movie implements Parcelable{
    private String originalTitle;
    private String moviePosterImage;
    private String plotSynopsis;
    private String userRating;
    private String releaseDate;
    private String movieLength;
    private int id;

    //Empty Constructor for Serialization
    public Movie()
    {}

    protected Movie(Parcel input)
    {
        Bundle data = input.readBundle(getClass().getClassLoader());
        this.originalTitle = data.getString("originalTitle");
        this.moviePosterImage = data.getString("moviePosterImage");
        this.plotSynopsis = data.getString("plotSynopsis");
        this.userRating = data.getString("userRating");
        this.releaseDate = data.getString("releaseDate");
        this.movieLength = data.getString("movieLength");
        this.id = data.getInt("id");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Bundle data = new Bundle();
        data.putString("originalTitle",this.originalTitle);
        data.putString("moviePosterImage",this.moviePosterImage);
        data.putString("plotSynopsis",this.plotSynopsis);
        data.putString("userRating",this.userRating);
        data.putString("releaseDate",this.releaseDate);
        data.putString("movieLength",this.movieLength);
        data.putInt("id", this.id);

        parcel.writeBundle(data);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    //Create an object using the input values.
    public Movie(String originalTitle, String moviePosterImage, String plotSynopsis,
                 String userRating, String releaseDate, String movieLength, int id)
    {
        this.originalTitle = originalTitle;
        this.moviePosterImage = moviePosterImage;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.movieLength = movieLength;
        this.id = id;
    }

    //Define Setter and Getter methods
    public String getOriginalTitle(){
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getMoviePosterImage() {
        return moviePosterImage;
    }

    public void setMoviePosterImage(String moviePosterImage) {
        this.moviePosterImage = moviePosterImage;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieLength() {
        return movieLength;
    }

    public void setMovieLength(String movieLength) {
        this.movieLength = movieLength;
    }
}
