package com.quotam.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Album implements Parcelable {

    String name;
    String backgroundUrl;
    String likesCount;
    String picturesCount;

    public Album(String name, String backgroundUrl, String likesCount, String picturesCount) {
        this.name = name;
        this.backgroundUrl = backgroundUrl;
        this.likesCount = likesCount;
        this.picturesCount = picturesCount;
    }

    public String getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(String likesCount) {
        this.likesCount = likesCount;
    }

    public String getPicturesCount() {
        return picturesCount;
    }

    public void setPicturesCount(String picturesCount) {
        this.picturesCount = picturesCount;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Constructor to use when re-constructing object
     * from a parcel
     *
     * @param in a parcel from which to read this object
     */
    public Album(Parcel in) {
        readFromParcel(in);
    }

    /**
     * Called from the constructor to create this
     * object from a parcel.
     *
     * @param in parcel from which to re-create object
     */
    private void readFromParcel(Parcel in) {

        // We just need to read back each
        // field in the order that it was
        // written to the parcel
        name = in.readString();
        backgroundUrl = in.readString();
        likesCount=in.readString();
        picturesCount=in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        // We just need to write each field into the
        // parcel. When we read from parcel, they
        // will come back in the same order
        parcel.writeString(name);
        parcel.writeString(backgroundUrl);
        parcel.writeString(likesCount);
        parcel.writeString(picturesCount);
    }

    /**
     *
     * This field is needed for Android to be able to
     * create new objects, individually or as arrays.
     *
     * This also means that you can use use the default
     * constructor to create the object and use another
     * method to hyrdate it as necessary.
     *
     */
    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Album createFromParcel(Parcel in) {
                    return new Album(in);
                }

                public Album[] newArray(int size) {
                    return new Album[size];
                }
            };

}
