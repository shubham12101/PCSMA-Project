package com.pcsma.ifhtt.mainApp;

/**
 * Created by Shubham on 28 Mar 15.
 */
public class ActionObject {

    private String location;
    private String startTime;
    private String endTime;
    private String action;
    private String option_1 = "";
    private String option_2 = "";

    public ActionObject(String loc, String sTime, String eTime, String action, String opt1,String opt2)
    {
        this.location = loc;
        this.startTime = sTime;
        this.endTime = eTime;
        this.action = action;
        this.option_1 = opt1;
        this.option_2 = opt2;
    }

    public String getLocation()
    {
        return location;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public String getAction()
    {
        return action;
    }

    public String getOption1()
    {
        return option_1;
    }

    public String getOption2()
    {
        return option_2;
    }

    public void setLocation(String loc)
    {
        this.location = loc;
    }

    public void setStartTime(String sTime)
    {
        this.startTime = sTime;
    }

    public void setEndTime(String eTime)
    {
        this.endTime = eTime;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public void setOption1(String opt)
    {
        this.option_1 = opt;
    }

    public void setOption2(String opt)
    {
        this.option_2 = opt;
    }
}
