package com.uowm.ekasdym.model;

public class TeamCategory {

    public int id;
    public String name;

    public TeamCategory(int id, String name)
    {
        this.name=name;
        this.id=id;
    }

    public String getName()
    {
        return name;
    }

    public int getId()
    {
        return id;
    }

}
