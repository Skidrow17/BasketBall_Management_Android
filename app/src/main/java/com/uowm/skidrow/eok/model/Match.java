package com.uowm.skidrow.eok.model;

public class Match {

    public int match_id;
    public String team1,team2;
    public int team_1_score,team_2_score;
    public String date_time;
    public String latitude,longitude;
    public int state;

    public Match( String team1, String team2,int team_1_score, int team_2_score,String date_time,String latitude,String longitude,int match_id,int state)
    {
        this.team1=team1;
        this.team2=team2;
        this.team_1_score=team_1_score;
        this.team_2_score=team_2_score;
        this.date_time=date_time;
        this.latitude=latitude;
        this.longitude=longitude;
        this.match_id=match_id;
        this.state = state;
    }

    public String getTeam1()
    {
        return team1;
    }

    public String getTeam2()
    {
        return team2;
    }

    public String getLatitude(){return latitude;}

    public String getLongitude(){return longitude;}

    public int getTeam1_score()
    {
        return team_1_score;
    }

    public int getTeam2_score()
    {
        return team_2_score;
    }

    public String getDateTime()
    {
        return date_time;
    }

    public int getMatch_id(){return match_id;}

    public int getState() {
        return state;
    }
}
