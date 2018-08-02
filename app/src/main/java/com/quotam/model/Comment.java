package com.quotam.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {

    String profileAvatarUrl;
    String profileName;
    String commentText;

    public Comment(String profileAvatarUrl, String profileName, String commentText) {
        this.profileAvatarUrl = profileAvatarUrl;
        this.profileName = profileName;
        this.commentText = commentText;
    }

    public String getProfileAvatarUrl() {
        return profileAvatarUrl;
    }

    public void setProfileAvatarUrl(String profileAvatarUrl) {
        this.profileAvatarUrl = profileAvatarUrl;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    /**
     * Constructor to use when re-constructing object
     * from a parcel
     *
     * @param in a parcel from which to read this object
     */
    public Comment(Parcel in) {
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
        profileAvatarUrl = in.readString();
        profileName = in.readString();
        commentText=in.readString();
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
        parcel.writeString(profileAvatarUrl);
        parcel.writeString(profileName);
        parcel.writeString(commentText);
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
                public Comment createFromParcel(Parcel in) {
                    return new Comment(in);
                }

                public Comment[] newArray(int size) {
                    return new Comment[size];
                }
            };



}
