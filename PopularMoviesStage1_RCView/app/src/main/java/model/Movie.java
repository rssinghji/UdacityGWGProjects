package model;

//Class created to use as model / data container to extract valuable information from API.
public class Movie {
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
