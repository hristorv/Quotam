package com.quotam.model;


import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class ImageDefault  implements Parcelable {

    String url;
    Type type;
    Uri uri;

    public enum Type {
        WEB,COLLECTION,UPLOAD
    }

    public ImageDefault(String url, Type type) {
        this.url = url;
        this.type = type;
    }
    public ImageDefault(Uri uri, Type type) {
        this.uri = uri;
        this.type = type;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Constructor to use when re-constructing object
     * from a parcel
     *
     * @param in a parcel from which to read this object
     */
    public ImageDefault(Parcel in) {
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
        url = in.readString();
        type = Type.valueOf(in.readString());
        uri = in.readParcelable(Uri.CREATOR.getClass().getClassLoader());
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
        parcel.writeString(url);
        parcel.writeString(type.toString());
        Uri.writeToParcel(parcel,uri);

    }

    /**
     * This field is needed for Android to be able to
     * create new objects, individually or as arrays.
     * <p>
     * This also means that you can use use the default
     * constructor to create the object and use another
     * method to hyrdate it as necessary.
     */
    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public ImageDefault createFromParcel(Parcel in) {
                    return new ImageDefault(in);
                }

                public ImageDefault[] newArray(int size) {
                    return new ImageDefault[size];
                }
            };
}
