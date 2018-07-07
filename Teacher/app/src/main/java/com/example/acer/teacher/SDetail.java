package com.example.acer.teacher;

public class SDetail {
    private  String stud_name;
    private String phone;

    public SDetail(String stud_name, String phone) {
        this.stud_name = stud_name;
        this.phone = phone;
    }

    public String getStud_name() {
        return stud_name;
    }

    public void setStud_name(String stud_name) {
        this.stud_name = stud_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
