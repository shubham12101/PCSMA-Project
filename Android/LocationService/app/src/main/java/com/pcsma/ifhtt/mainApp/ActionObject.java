package com.pcsma.ifhtt.mainApp;

/**
 * Created by Shubham on 28 Mar 15.
 */
public class ActionObject {

    private String location;
    private long startTime;
    private long endTime;
    private String action;
    private String option = "";

    public ActionObject(String loc, long sTime, long eTime, String action, String opt)
    {
        this.location = loc;
        this.startTime = sTime;
        this.endTime = eTime;
        this.action = action;
        this.option = opt;
    }

    public String getLocation()
    {
        return location;
    }

    public long getStartTime()
    {
        return startTime;
    }

    public long getEndTime()
    {
        return endTime;
    }

    public String getAction()
    {
        return action;
    }

    public String getOption()
    {
        return option;
    }

    public void setLocation(String loc)
    {
        this.location = loc;
    }

    public void setStartTime(long sTime)
    {
        this.startTime = sTime;
    }

    public void setEndTime(long eTime)
    {
        this.endTime = eTime;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public void setOption(String opt)
    {
        this.option = opt;
    }


}
