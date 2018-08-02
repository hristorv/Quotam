package com.quotam.model;


public class SectionFooter {

    boolean isActive;
    String count;

    public SectionFooter() {

    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public SectionFooter(boolean isActive, String count) {
        this.isActive = isActive;
        this.count = count;
    }
}
