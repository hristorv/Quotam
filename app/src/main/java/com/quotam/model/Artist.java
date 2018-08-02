package com.quotam.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Artist implements Parcelable{

    String name;
    String backgroundUrl;
    String avatarUrl;
    String likesCount;
    String picturesCount;
    String albumsCount;

    public Artist(String name, String backgroundUrl, String avatarUrl, String likesCount, String albumsCount, String picturesCount) {
        this.name = name;
        this.backgroundUrl = backgroundUrl;
        this.avatarUrl = avatarUrl;
        this.likesCount = likesCount;
        this.albumsCount = albumsCount;
        this.picturesCount = picturesCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
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

    public String getAlbumsCount() {
        return albumsCount;
    }

    public void setAlbumsCount(String albumsCount) {
        this.albumsCount = albumsCount;
    }

    /**
     * Constructor to use when re-constructing object
     * from a parcel
     *
     * @param in a parcel from which to read this object
     */
    public Artist(Parcel in) {
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
        avatarUrl=in.readString();
        likesCount=in.readString();
        picturesCount=in.readString();
        albumsCount=in.readString();
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
        parcel.writeString(avatarUrl);
        parcel.writeString(likesCount);
        parcel.writeString(picturesCount);
        parcel.writeString(albumsCount);
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
                public Artist createFromParcel(Parcel in) {
                    return new Artist(in);
                }

                public Artist[] newArray(int size) {
                    return new Artist[size];
                }
            };

}
