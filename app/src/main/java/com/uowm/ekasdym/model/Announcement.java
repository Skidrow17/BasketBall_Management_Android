package com.uowm.ekasdym.model;

public class Announcement {

    private String title;
    private String text;
    private String dateTime;
    private String editor;
    private int id;

    public Announcement(int id,String title, String text, String dateTime, String editor)
    {
        this.title=title;
        this.text=text;
        this.dateTime=dateTime;
        this.id=id;
        this.editor=editor;
    }


    public String getEditor() {
        return editor;
    }
    public String getTitle()
    {
        return title;
    }

    public String getText()
    {
        return text;
    }

    public String getDateTime(){return dateTime;}

    public int getID(){return id;}


}
