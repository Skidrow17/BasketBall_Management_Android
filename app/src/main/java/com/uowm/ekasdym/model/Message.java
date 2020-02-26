package com.uowm.ekasdym.model;

public class Message {

    private String name;
    private String surname;
    private String dateTime;
    private int id;
    private int sender_id;
    private String message;
    private String profile_pic;
    private String message_status;

    public Message(int id,String name,String surname,String message, String profile_pic,String dateTime,String message_status,int sender_id)
    {
        this.id=id;
        this.profile_pic=profile_pic;
        this.name=name;
        this.message=message;
        this.surname=surname;
        this.dateTime=dateTime;
        this.message_status=message_status;
        this.sender_id = sender_id;
    }

    public Message(int id,String name,String surname,String message, String profile_pic,String dateTime,String message_status) {
        this.id = id;
        this.profile_pic = profile_pic;
        this.name = name;
        this.message = message;
        this.surname = surname;
        this.dateTime = dateTime;
        this.message_status = message_status;
    }

    public String getName()
    {
        return name;
    }

    public String getPic()
    {
        return profile_pic;
    }

    public String getMessage()
    {
        return message;
    }

    public String getDateTime(){return dateTime;}

    public String getSurname()
    {
        return surname;
    }

    public int getSender_id() { return sender_id; }

    public int getId() { return id; }

    public String getMessage_status(){return message_status;}

    public void setMessage_status(String message_status) {
        this.message_status = message_status;
    }
}
