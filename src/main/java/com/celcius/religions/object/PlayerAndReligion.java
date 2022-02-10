package com.celcius.religions.object;

public class PlayerAndReligion {
    private String uuid;
    private String name;
    private String religionID;
    private int points;

    public PlayerAndReligion(String uuid, String name, String religionID, int points){
        this.setUuid(uuid);
        this.setName(name);
        this.setReligionID(religionID);
        this.setPoints(points);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReligionID() {
        return religionID;
    }

    public void setReligionID(String religionID) {
        this.religionID = religionID;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
