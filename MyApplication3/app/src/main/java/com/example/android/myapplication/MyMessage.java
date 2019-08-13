package com.example.android.myapplication;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String message;
    private Date time;

    public MyMessage(String username, String message, Date time){
        this.username = username;
        this.message = message;
        this.time = time;
    }

    public String getMessage(){
        return message;
    }

    public String getTime(){
        SimpleDateFormat sd = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        return sd.format(time);
    }

    public String getUsername(){
        return username;
    }
}
