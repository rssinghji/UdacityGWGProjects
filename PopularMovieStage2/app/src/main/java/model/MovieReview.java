package model;

public class MovieReview {
    private String mAuthor;
    private String mContent;
    private String mId;

    //This constructor would be needed to create an initialized object
    public MovieReview(String id, String author, String content) {
        this.mId = id;
        this.mAuthor = author;
        this.mContent = content;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }
}
