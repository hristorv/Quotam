package com.quotam.model;


public class ImageItem {

    String url;
    String thumbUrl;
    String category;

    public ImageItem(String url, String thumbUrl, String category) {
        this.url = url;
        this.thumbUrl = thumbUrl;
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
