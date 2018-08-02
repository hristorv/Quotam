package com.quotam.model;


import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class CreateImage {

    String primaryText = "";
    String author = "";
    Bitmap bitmap;
    Bitmap fullImage;
    List<String> keywords = new ArrayList<>();
    boolean isPublic = true;

    public CreateImage() {

    }

    public Bitmap getFullImage() {
        return fullImage;
    }

    public void setFullImage(Bitmap fullImage) {
        this.fullImage = fullImage;
    }

    public String getPrimaryText() {
        return primaryText;
    }

    public void setPrimaryText(String primaryText) {
        this.primaryText = primaryText;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
