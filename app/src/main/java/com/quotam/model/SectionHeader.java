package com.quotam.model;

public class SectionHeader {

    String name;
    String type;
    String time;

    public SectionHeader(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public SectionHeader(String name, String type, String time) {
        this(name, type);
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
