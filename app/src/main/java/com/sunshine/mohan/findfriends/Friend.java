package com.sunshine.mohan.findfriends;


public class Friend {

    //Properties
    private String first_name;
    private String last_name;
    private String email;
    private long number;
    private double distance;
    private double lat;
    private double lon;



    public Friend(){

    }
    //Behaviours
    public Friend(String first_name, String last_name, String email, long number, double distance) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.number = number;
        this.distance = distance;
    }
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getLat() { return lat; }

    public void setLat(double lat) { this.lat = lat; }

    public double getLon() { return lon; }

    public void setLon(double lon) { this.lon = lon; }

}
