package com.uowm.skidrow.eok.events;


public class MessageEvent {

    public final String message;
    public final String user;

    public MessageEvent(String user,String message) {
        this.message = message;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public String getUser() {
        return user;
    }
}