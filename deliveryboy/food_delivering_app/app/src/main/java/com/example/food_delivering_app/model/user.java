package com.example.food_delivering_app.model;

public class user {
    String name,address,mobileno;
    public user(){}

    public user(String name, String address, String mobileno) {
        this.name = name;
        this.address = address;
        this.mobileno = mobileno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }
}