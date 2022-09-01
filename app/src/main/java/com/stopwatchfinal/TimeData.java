package com.stopwatchfinal;

public class TimeData {
    private String runningTime;
    private String date;
    private String time;
    private String key;
    private String Uid;

    public TimeData(){}

    public TimeData(String Uid, String runningTime, String date, String time, String key) {
        this.Uid= Uid;
        this.runningTime = runningTime;
        this.date = date;
        this.time= time;
        this.key= key;
        //this.dateTime= dateTime;
    }

    public String getUid() {return Uid;}

    public String getKey() {
        return key;
    }

    public String getRunningTime() {
        return runningTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setRunningTime(String runningTime) {
        this.runningTime = runningTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
