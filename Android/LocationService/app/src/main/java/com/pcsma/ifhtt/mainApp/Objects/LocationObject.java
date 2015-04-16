package com.pcsma.ifhtt.mainApp.Objects;

/**
 * Created by Shubham on 15 Apr 15.
 */
public class LocationObject {
    private String building;
    private String wing;
    private String floor;
    private String room;

    public void setBuilding(String b)
    {
        this.building = b;
    }

    public void setWing(String w)
    {
        this.wing = w;
    }

    public void setFloor(String f)
    {
        this.floor = f;
    }

    public void setRoom(String r)
    {
        this.room = r;
    }

    public String getBuilding()
    {
        return this.building;
    }

    public String getWing()
    {
        return this.wing;
    }

    public String getFloor()
    {
        return this.floor;
    }

    @Override
    public String toString() {
        return (this.getBuilding()+" "+this.getFloor()+" "+this.getWing()+" "+this.getRoom());
    }

    @Override
    public boolean equals(Object o) {
        if(this.getBuilding().equals(((LocationObject) o).getBuilding()) &&
                this.getFloor().equals(((LocationObject) o).getFloor()) &&
                this.getRoom().equals(((LocationObject) o).getRoom()) &&
                this.getWing().equals(((LocationObject) o).getWing()))
            return true;
        else
            return false;
    }

    public String getRoom()
    {
        return this.room;
    }
}
