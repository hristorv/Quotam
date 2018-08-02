package com.quotam.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Parcelable {
	public Image(String url, String likes, String artist) {
		this.url = url;
		this.likes = likes;
		this.artist = artist;
	}

	String url;
	String likes;
	String artist;

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getUrl() {
		return url;
	}

	public String getLikes() {
		return likes;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setLikes(String likes) {
		this.likes = likes;
	}

	/**
	 * Constructor to use when re-constructing object
	 * from a parcel
	 *
	 * @param in a parcel from which to read this object
	 */
	public Image(Parcel in) {
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
		likes = in.readString();
		artist=in.readString();
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
		parcel.writeString(likes);
		parcel.writeString(artist);
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
				public Image createFromParcel(Parcel in) {
					return new Image(in);
				}

				public Image[] newArray(int size) {
					return new Image[size];
				}
			};

}
