package model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class MovieTrailer implements Parcelable{
        private String mKey;
        private String mName;
        private String mSite;
        private String mSize;
        private String mType;
        private String mId;

        //This constructor would be needed to create an initialized object
        public MovieTrailer(String key, String name, String site, String size, String type, String id) {
            this.mKey = key;
            this.mName = name;
            this.mSite = site;
            this.mSize = size;
            this.mType = type;
            this.mId = id;
        }

        //Generic constructor
        public MovieTrailer(){}

    protected MovieTrailer(Parcel input)
    {
        Bundle data = input.readBundle(getClass().getClassLoader());
        this.mId = data.getString("id");
        this.mType = data.getString("type");
        this.mSize = data.getString("size");
        this.mSite = data.getString("site");
        this.mName = data.getString("name");
        this.mKey = data.getString("key");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Bundle data = new Bundle();
        data.putString("id",this.mId);
        data.putString("type",this.mType);
        data.putString("size",this.mSize);
        data.putString("site",this.mSite);
        data.putString("name",this.mName);
        data.putString("key",this.mKey);

        parcel.writeBundle(data);
    }

    public static final Parcelable.Creator<MovieTrailer> CREATOR = new Parcelable.Creator<MovieTrailer>() {
        public MovieTrailer createFromParcel(Parcel in) {
            return new MovieTrailer(in);
        }

        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };

    public String getKey() {
        return mKey;
    }

    public String getName() {
        return mName;
    }

    public String getSite() {
        return mSite;
    }

    public String getSize() {
        return mSize;
    }

    public String getType() {
        return mType;
    }

    public String getId() {
        return mId;
    }

    public void setKey(String mKey) {
        this.mKey = mKey;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setSite(String mSite) {
        this.mSite = mSite;
    }

    public void setSize(String mSize) {
        this.mSize = mSize;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public void setId(String mId) {
        this.mId = mId;
    }
}
