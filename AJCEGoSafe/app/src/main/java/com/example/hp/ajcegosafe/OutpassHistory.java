package com.example.hp.ajcegosafe;

/**
 * Created by hp on 28-09-2017.
 */

public class OutpassHistory {
    String date;
    String purpose;
    String status;

    public OutpassHistory(String Date, String Pur, String Stus){
        date = Date;
        purpose = Pur;
        status = Stus;
    }
    public OutpassHistory(){}
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
