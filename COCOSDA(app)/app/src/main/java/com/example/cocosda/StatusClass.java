package com.example.cocosda;

public class StatusClass {

    String user_ID;
    String time;
    String status;

    public StatusClass(String user_ID, String time, String status) {
        this.user_ID = user_ID;
        this.time = time;
        this.status = status;
    }

    public String getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
