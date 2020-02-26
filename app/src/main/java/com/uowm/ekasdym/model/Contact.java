package com.uowm.ekasdym.model;

public class Contact {

    public String name,surname;
    public int id;
    public String last_login;
    public String profile_pic;
    public String phone_number;

    public Contact(int id , String name,String surname,String profile_pic,String phone_number,String last_login)
    {
        this.id=id;
        this.name=name;
        this.surname=surname;
        this.profile_pic=profile_pic;
        this.phone_number=phone_number;
        this.last_login=last_login;
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




}
