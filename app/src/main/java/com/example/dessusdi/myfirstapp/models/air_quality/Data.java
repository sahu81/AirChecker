package com.example.dessusdi.myfirstapp.models.air_quality;

/**
 * Created by dessusdi on 30/01/2017.
 * DESSUS Dimitri
 */
public class Data {
    private MessageObject msg;
    private String status;

    public MessageObject getMsg() {
        return msg;
    }

    public void setMsg(MessageObject msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
