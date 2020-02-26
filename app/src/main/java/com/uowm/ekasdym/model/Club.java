package com.uowm.ekasdym.model;

public class Club
{
    public int id;
    public String name;
    public int wins;
    public int loses;
    public int points;
    public int totalGames;

    public Club(int id, String name, int wins, int loses, int points) {
        this.id = id;
        this.name = name;
        this.wins = wins;
        this.loses = loses;
        this.points = points;
        this.totalGames = this.loses + this.wins;
    }
}
