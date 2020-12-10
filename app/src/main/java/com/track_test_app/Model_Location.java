package com.track_test_app;

public class Model_Location {

    private double lang;
    private double lat;
    private String name;
    private String tag;
    private String color;

    public Model_Location() {
    }


    public Model_Location(double lang, double lat, String name, String tag, String color) {
        this.lang = lang;
        this.lat = lat;
        this.name = name;
        this.tag = tag;
        this.color = color;
    }

    public double getLang() {
        return lang;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
