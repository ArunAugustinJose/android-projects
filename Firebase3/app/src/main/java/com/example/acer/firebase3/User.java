package com.example.acer.firebase3;

public class User {
    private  String id;
    private  String name;
    private  String subject;
    private String count;

    public User(String id, String name, String subject, String count) {
        this.id = id;
        this.name = name;
        this.subject = subject;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(){

    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
