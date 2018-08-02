package com.quotam.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable{

    private String name;
    private String backgroundUrl;

    public Category(String name, String backgroundUrl) {
        this.name = name;
        this.backgroundUrl = backgroundUrl;
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
    public Category(Parcel in) {
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
                public Category createFromParcel(Parcel in) {
                    return new Category(in);
                }

                public Category[] newArray(int size) {
                    return new Category[size];
                }
            };

}
