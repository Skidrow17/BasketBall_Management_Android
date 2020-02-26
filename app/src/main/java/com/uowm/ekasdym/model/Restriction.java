package com.uowm.ekasdym.model;

public class Restriction {

    private String time_from;
    private String time_to;
    private String date;
    private int id;


    public Restriction(int id, String time_from, String time_to, String date) {
        this.time_from = time_from;
        this.time_to = time_to;
        this.date = date;
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public String getTime_from() {
        return time_from;
    }

    public String getTime_to() {
        return time_to;
    }

    public String getDate() {
        return date;
    }
}
