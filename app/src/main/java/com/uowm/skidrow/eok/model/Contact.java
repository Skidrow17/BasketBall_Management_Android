package com.uowm.skidrow.eok.model;

public class Contact {

    public String name,surname;
    public int id;
    public String last_login;
    public String profile_pic;
    public String phone_number;
    public String profile;

    public Contact(int id , String name,String surname,String profile_pic,String phone_number,String last_login,String profile)
    {
        this.id=id;
        this.name=name;
        this.surname=surname;
        this.profile_pic=profile_pic;
        this.phone_number=phone_number;
        this.last_login=last_login;
        this.profile = profile;
    }

    public Contact(int id , String name,String surname,String profile_pic,String profile)
    {
        this.id=id;
        this.name=name;
        this.surname=surname;
        this.profile_pic=profile_pic;
        this.profile = profile;
    }

    public String getName()
    {
        return name;
    }

    public String getSurname()
    {
        return surname;
    }

    public String getPhone_number(){return phone_number;}

    public int getId()
    {
        return id;
    }

    public String getPic()
    {
        return profile_pic;
    }

    public String getLast_login(){return last_login;}

    public String toString()
    {
        return "Name : "+name+"\n ID : "+id;
    }

    public String getProfile() {
        return profile;
    }
}
