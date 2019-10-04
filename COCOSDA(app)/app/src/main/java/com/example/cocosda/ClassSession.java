package com.example.cocosda;

public class ClassSession {

    int ndx;
    String room;
    String timeStart;
    String timeEnd;

    public ClassSession(int ndx, String room, String timeStart, String timeEnd) {
        this.ndx = ndx;
        this.room = room;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public int getNdx() {
        return ndx;
    }

    public void setNdx(int ndx) {
        this.ndx = ndx;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }
}
